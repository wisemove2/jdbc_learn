import java.sql.*;
import java.util.ResourceBundle;

public class JDBCTest05 {
    // 查询功能
    public static void main(String[] args){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        ResourceBundle resourceBundle = ResourceBundle.getBundle("jdbc");
        String driverstr = resourceBundle.getString("driver");
        String url = resourceBundle.getString("url");
        String user = resourceBundle.getString("user");
        String password = resourceBundle.getString("password");

        try {
            // 1. 注册驱动
            Class.forName(driverstr);

            // 2. 建立连接
            connection = DriverManager.getConnection(url, user, password);
            System.out.println(connection);

            // 3. 获取数据库操作对象
            statement = connection.createStatement();

            // 4. 编写sql语句
            String sql = "select * from admin";
            // 专门执行查询语句的
            resultSet = statement.executeQuery(sql);
            // int executeUpdate(insert/delete/update) 返回影响条目数
            // ResultSet executeQuery(select)          返回查询结果集

            // 5. 处理结果集
            while(resultSet.next()){
                // jdbc中下表从1开始，然后遍历每一行，知道遍历到最后。
                // 或者在getString中写上面sql语句中查询列名进行查找。注意一定要是和上面查询语句一样，而不是和数据库中的列名一样。
                // 注意，不止有getString，还有getInt, getDouble。但是一定要匹配。
//                String user_name = resultSet.getString(1);
//                String pass_word = resultSet.getString(2);
                String user_name = resultSet.getString("username");
                String pass_word = resultSet.getString("password");
                System.out.println("username: " + user_name + " password: " + pass_word);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
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
}
