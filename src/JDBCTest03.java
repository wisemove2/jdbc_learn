import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCTest03 {
    // 注册驱动的另一种方式
    public static void main(String[] args){
        Connection connection = null;
        try{
            // 1. 常用的注册方式
//            Driver driver = new Driver();
//            DriverManager.registerDriver(driver);

            // 第二种注册方式：通过类反射机制。去源码，打开com.mysql.cj.jdbc.Driver 可以看到这个代码是在静态代码块中加载的。通过反射机制可以直接调用。
            // 为什么这种方式常用。因为参数是一个字符串，字符串可以写到*****.properties文件中去。
            // 下面这个方法也不需要返回值，我们只需要这个加载动作即可。
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 获取连接
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/genesis?characterEncoding=UTF-8&&serverTimezone=GMT",
                    "root",
                    "wisemove");
            System.out.println(connection);

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }finally {
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
