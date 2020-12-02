import java.sql.*;
import java.util.Scanner;

public class JDBCTest08 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入desc或asc：");
        String keyWords = scanner.nextLine();

        String url = "jdbc:mysql://127.0.0.1:3306/genesis?characterEncoding=UTF-8&&serverTimezone=GMT";
        String username = "root";
        String password = "wisemove";
        String sql = "select username from admin order by ?";

        // JDBC变成六步
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            // 1. 驱动注册
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 获取连接
            connection = DriverManager.getConnection(url, username, password);

            // 3. 获取数据库操作对象
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, keyWords);

            // 4. 执行sql
            resultSet = preparedStatement.executeQuery();

            // 5. 处理结果集合
            while(resultSet.next()){
                System.out.println("username = " + resultSet.getString(1));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

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
