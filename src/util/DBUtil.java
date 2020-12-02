package util;

/*
* 简化JDBC
*
* */

import java.sql.*;

public class DBUtil {
    /*
    * 工具类中的构造方法都是私有的，因为工具类当中的方法都是静态的，不需要new对象，直接类名进行调用。
    * 比如Arrays类，它的构造方法就是私有的。我们经常使用Arrays.sort()来排序。还有Collection等。
    * 工具类建议私有化。采用类名去调。
    * */

    private DBUtil(){
    }

    // 注册驱动代码只需要执行一次就好了，所以只需要放在静态代码块中就好了。
    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 这边不用抓取异常，我们再主程序中去抓，这边直接抛出就好了
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/genesis?characterEncoding=UTF-8&&serverTimezone=GMT", "root", "wisemove");
    }

    // 关闭资源
    public static void close(Connection connection, Statement statement, ResultSet resultSet){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

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
