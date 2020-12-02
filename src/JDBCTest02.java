import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTest02 {
    // 实现删除
    public static void main(String[] args){
        Connection connection = null;
        Statement statement = null;

        try {
            // 1. 注册驱动
            String url = "jdbc:mysql://127.0.0.1:3306/genesis?characterEncoding=UTF-8&&serverTimezone=GMT";
            String username = "root";
            String password = "wisemove";

            Driver driver = new Driver();
            DriverManager.registerDriver(driver);

            // 2. 连接数据库
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("数据连接对象为: " + connection);

            // 3. 获取数据库操作对象
            statement = connection.createStatement();

            // 4. 执行sql语句
            String sql = "delete from admin where username = 'wisemove'";
            int x = statement.executeUpdate(sql);
            System.out.println("收到影像的条目数为: " + x);
            // 5. 结果集，暂无
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
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
