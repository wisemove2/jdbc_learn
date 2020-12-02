import java.sql.*;

public class JDBCTest01 {
    public static void main(String[] args){

        String url = "jdbc:mysql://127.0.0.1:3306/genesis?characterEncoding=UTF-8&&serverTimezone=GMT";
        String user = "root";
        String password = "wisemove";
        Statement statement = null;
        Connection connection = null;

        try {
            // 1. 注册驱动
            // 前一个是sql包下的driver，后面是mysql实现的Driver类。多态，父类型引用指向子类型对象。
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);

            // 2. 获取连接
            // 这个connection正好也是SQLException。并且这个也是多态，一个是java.sql下的接口，一个是mysql下的具体实现。
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("数据库连接对象: " + connection);

            // 3. 获取数据库操作对象
            statement = connection.createStatement();

            // 4. 执行sql
            String search = "insert into admin(username,password) values('wisemove','wisemove')";
            // 专门执行DML语句，返回值是影像数据库中的记录条数;下面语句不能监控select语句
            int x = statement.executeUpdate(search);
            System.out.println("影像的结果记录条数为: " + x);

            // 5. 处理结果集

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            // 6. 释放资源；为了保证资源一定释放，在finally中关闭资源。遵循从小到大依次关闭；
            // 关闭的时候发现变量必须创建在外部才可以关闭。注意，不能一次try... catch
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
