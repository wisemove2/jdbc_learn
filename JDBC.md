# JDBC总结
### 1. JDBC是什么？
java Database Connectivity（Java 语言连接数据库）

### 2. JDBC的本质是什么？
JDBC是SUN公司制定的一套接口（interface）。接口都有调用者和实现者。面向接口调用、面向接口写实现类，这都是属于面向接口编程。

##### 为什么要面向接口编程？
解耦合：降低程序的耦合度，提高程序的扩展力。多态机制就是非常典型的：面向抽象编程。（不要面向具体编程）

### 3. JDBC过程
1. 注册驱动（告诉java程序，即将要连接的是哪个品牌的数据库）
2. 获取连接（表示jvm的进程和数据库进程之间的通道打开了，这属于进程之间的通信，重量级的，使用完成之后一定要关闭）
3. 获取数据库操作对象（用来执行sql语句的）
4. 执行SQL语句（DQL，DML）
5. 处理查询结果集（只有当第四步是select语句的时候，才有第五步的结果集）
6. 释放资源（使用完资源之后一定要关闭资源。java和数据库属于进程间的通信，开启之后一定要关闭。）

仓库中给了13份代码、一个jdbc.properties，下面简单介绍一下这些在干啥。

##### JDBCTest01.java
可以看到这个程序在做的主要工作就是连接数据库，然后往admin表中添加了一行数据。当然我们需要事先知道表的具体构造，然后才能成功执行sql语句。这个程序运行完成之后，可以打开mysql查看，确实增加了一行数据。

**String url = "jdbc:mysql://127.0.0.1:3306/genesis?characterEncoding=UTF-8&&serverTimezone=GMT";**

注意：字符串中的**genesis**是mysql中的**仓库名**，表示此次连接了那个数据库。然后后面的characterEncoding=UTF-8表示编码字符集，&&serverTimezone=GMT这个在之前的mysql5中还不需要。我是用的mysql是8.0.18。现在不加就会连不上，表示北京东八区的时间意思。

然后要注意一定要释放资源，并且释放资源的顺序是从小到大。先是数据库操作对象，然后再是本次连接。如果有结果集的话需要首先释放结果集。但是在释放的过程中会发现需要实现定义变量Statement和Connection，不然如果只是在try catch中，finall中获取不到。


##### JDBCTest02.java
这个程序是在删除用户名为***的数据。代码结构和JDBCTest01.java一样。主要就是为了重新默写一遍JDBC的六大流程。


##### JDBCTest03.java
学习类反射机制来注册驱动。并且为04做准备。


##### JDBCTest04.java
前面03代码也看到了，我们可以通过类反射机制去选择我们到底要加载那种类型的数据库，只需要更改字符串就行。还有01和02中有很多固定的字符串是可以添加依赖来完成的。这样可以在依赖中修改，不用编译可以直接运行。使用idae创建new 一个source Bundler，然后在里面可以预先定义好固定的字符串，然后再通过ResourceBundle.getBundle获取对象，来得到具体的参数值。使得程序封装更好。


##### JDBCTest05.java
前面讲过了增加、删除，但是没讲如何进行查找。这份代码就是讲解如何查找的。注，这个ResultSet并和java.util中的set并没有任何关系，不是继承之类的关系。仅仅只是叫这个名。ResultSet表示的是我们查询的结果集，比如下图：


![](/image/selectResult.png)

返回的resultSet.next()返回的是一个boolean类型，表示当前位置是否有数据，如果有返回true，如果没有返回false。并且执行完毕之后会向下移动一位。注意jdbc中都是从0开始的，所以调用了resultSet之后才走到第一个位置，然后就可以获取到相应的字段。然后获取的列名字段也是从1开始的，所以第一个字段是查询语句中的前面一个，第二个字段是查询语句中第二个。如果没有就是mysql中的表名。

注意：在resultSet.getString()中的值要和程序中写的sql语句要相同，不是和mysql表中的列名相同。


##### JDBCTest06.java
这个程序是一个模拟登录的功能。数据库有的人在上图表中已经给定了，接收键盘输入username和password，然后去做查询语句，判断是否可以查询到这个用户。如果查询得到，那么结果集肯定是有下一个的（第一个结果在index = 1）。这个是可以初步完成功能的，但是存在的问题就是sql注入的问题。sql字符串为：**"select * from admin where username = '" + userLoginInfo.get("username") + "' and password = '" + userLoginInfo.get("password") + "'"**。当username = wisemove，密码是sa' or '1'='1，这样这个字符串就会变成**select * from admin where username='wisemove' and password = 'sa' or '1' = '1'**。这样可以看到or前面是一个false，但是or后面就是true了。所以整个查询结果就是true。进过验证，resultSet为全表。那么resultSet.next肯定就是true了。最终也就成功登陆了。关于sql注入的原理注释中已经将的很清楚了。07是一个解决sql注入的方式。当时我想到的解决sql注入的方式就是根据判断这个reslutSet的大小，因为也知道这样注入的sql返回是全表。所以我想到的方式是根据结果集判断。



##### JDBCTest07.java
这个程序主要解决了06的注入问题。解决方式为更换一个数据库操作对象，使用预编译的数据库操作对象。PreparedStatement preparedStatement = connection.preparedStatement(sql); 注意sql语句在这个地方传递了，然后sql语句中将需要传值的地方用？号站位，然后后面通过setString()或者setInt方法给第几个站位符来传值。注意JDBC中是从1开始计数的。然后第四步去执行查询。这个方式和前面的方法的区别在于sql语句在第三步就传入了，前面的都是第四步传入的。这样就可以有效解决sql注入问题。通过预编译的数据库操作对象解决了sql注入的问题。



###对比Statement和PreparedStatement
Statement存在sql注入问题，PreparedStatement解决了sql注入问题。
Statement是编译一次执行一次。PreparedStatement是编译一次可以执行n次。PreparedStatment效率更高。（但是好像新版本的不支持缓存了，这个还存疑）。PreparedStatement会在编译阶段做类型安全检查。


综上所述。PreparedStatement使用较多，极少情况下需要使用Statement。当业务方面需求必须支持sql注入的时候，凡是业务方面需要支持sql拼接的时候，需要使用Statement。比如淘宝当中商品的升序、降序的功能。sql语句肯定是需要传递desc或者asc。但是当使用PreparedStatement的时候，desc会被封装成'desc'，这样会导致关键字不可用。所以有的字符串拼接场景还是需要用到Statement。


！！！经过测试，好像高版本的jdk和mysql可以使用PreparedStatement来做这个。视频中的使用的老版本的jdk和mysql5.几。高版本已经解决了上述情况。
![](/image/08search.png)

##### JDBCTest08.java
这个代码就是在演示PreparedStatement已经解决了mysql5中有的关键字不能使用的问题。这里留一个todo，后面去研究一下如何解决的。


##### JDBCTest09.java
演示了使用PreparedStatement来实现增删改的功能。


##### JDBCTest10.java
引出了事务这样的一个概念。在jdbc中，每一条DML语句都是执行一次就会自动提交一次，这样在有些场景下是非常不利的。引出mysql中的事务，并且在下一个程序中实现了事务。


##### JDBCTest11.java
因为前面已经知道了jdbc是每一次执行完dml语句就会自动提交，那么在获取完连接之后需要手动关闭自动提交功能。然后当所有的dml语句执行完成之后在执行提交。如果在这些dml语句中遇到异常，则会跳转到异常当中。如果异常中连接已经建立过了，那么肯定是需要将事务进行回滚。整个事务的三层逻辑就理清楚了。也就是三行代码注意添加的位置。


##### DBUtil.java & JDBCTest12.java
写了11个JDBC程序，发现里面很多代码都是重复的。所以可以写一个工具类来简化JDBC整个流程的代码书写。这边学到了JDBC的工具类DBUtils的构造函数写成私有的，这样外部就不能new这个对象，只能通过类名加静态方法来调用这个工具类。常用的Arrays，Collections都是类似的操作。Arrays.sort()等。还有一点就是驱动注册只需要一次就好了，我们不需要每一次都去注册驱动，所以可以写一个静态代码块，当来加载的时候就会去注册驱动。然后第一步和第六步的代码都可以封装成方法，提供给实体类去调用。


所有的程序讲解完毕！

**todo：**
### 之前都是用 父类 x = new 子类();来实现多态，这次碰到了接口 x = new 子类()
### 研究一下PreparedStatement如何解决了关键字问题


<br>
<br>
###  1. 用接口实现多态
回顾一下多态：1. 子类继承父类；2. 子类重写父类中的方法；3. 父类引用子类对象。多态的好处：可以是使程序有良好的扩展，并可以对所有类的对象进行通用处理。

**当使用多态的形式调用方法时，首先检查父类中是否有该方法，如果没有，则编译错误；如果有，再去调用同名方法。**


这次学习到的代码第一段是Driver dirver = new Driver();
![](/image/DriverSql.png)

这句话不去仔细研究跳过去就太失败了。在idae中写这句话的时候，第一个Driver输入到一半，它会提示你导包java.sql。当你写到第二个Driver的时候，它也会提示你：1. 导java.sql的包。确定的话默认跳出了很多方法要你来实现。之前得到的知识是接口不能new的。但是实际new的结果是，new之后的大括号起始可以看做你写了一个类，实现了这个接口，只不过这个类是匿名的，没有名字，所以称为匿名内部类。如果不这么写的话，那么必须写一个类实现接口，然后创建对象调用方法，这样就会变得非常复杂；2. 当使用com.mysql.cj.jdbc下面的Drver包中的Driver文件后，这句话就变成了接口 dirver = new 实现类();这应该也是多态的具体表现形式。这样的优点就是一个方法有一个接口类型的参数，则该接口的实现类都可以使用，如果使用某一具体实现类的话就只能这个实现类使用了；官方的说法也是依赖抽象，而不应该依赖具体，从而更具有扩展性；


### 2. PreparedStatement解决了关键字注入的问题
具体的问题描述看上面06，07的介绍。说白了就是有些字段条目（关键字、列名等）手动输入的时候会被编译成字符串报错。比如下面这行：select * from admin where usernaeme = 'wisemove' order by age 'desc'; 这边的wisemove和desc都是?号接受手动输入的。 在mysql5.0是有问题的，但是mysql8.0测试没问题。一直没找到源码怎么看，有点烦。