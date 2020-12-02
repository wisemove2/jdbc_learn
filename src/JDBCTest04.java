import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class JDBCTest04 {
    // 使用配置文件配置；实际开发中，不建议把数据库信息数据库写死到java程序中。
    public static void main(String[] args){
        Connection connection = null;
        Statement statement = null;

        try {
//            String url = "jdbc:mysql://127.0.0.1:3306/genesis?characterEncoding=UTF-8&&serverTimezone=GMT";
//            String username = "root";
//            String password = "wisemove";
//            String driverstr = "com.mysql.cj.jdbc.Driver";

            // 使用配置文件
            // 这样以后就可以直接修改properties进行修改。都可以不用编译，直接运行。
            ResourceBundle resourceBundle = ResourceBundle.getBundle("jdbc");
            String driverstr = resourceBundle.getString("driver");
            String url = resourceBundle.getString("url");
            String username = resourceBundle.getString("user");
            String password = resourceBundle.getString("password");

            // 1. 注册驱动
            Class.forName(driverstr);

            // 2. 连接数据库
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("数据连接对象为: " + connection);

            // 3. 获取数据库操作对象
            statement = connection.createStatement();

            // 4. 执行sql语句
            String sql = "delete from admin where username = 'suncheng'";
            int x = statement.executeUpdate(sql);
            System.out.println("受到影响的条目数为: " + x);
            // 5. 结果集，暂无
        }catch (SQLException | ClassNotFoundException throwables) {
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
