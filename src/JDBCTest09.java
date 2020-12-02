import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCTest09 {
    // 使用PreparedStatement完成增删改
    public static void main(String[] args){
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        String url = "jdbc:mysql://127.0.0.1:3306/genesis?characterEncoding=UTF-8&&serverTimezone=GMT";
        String username = "root";
        String password = "wisemove";
        // sql语句中进行换增删改即可
        String sql = "insert into admin(username, password) values(?,?)";

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 获取连接
            connection = DriverManager.getConnection(url, username, password);

            // 3. 获取数据库操作对象
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "aaa");
            preparedStatement.setString(2, "aaa");

            // 4. 执行sql
            int x = preparedStatement.executeUpdate();
            System.out.println("受影响的结果有 " + x + " 条");

            // 5. 获取结果集

        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            // 6. 释放资源
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
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
