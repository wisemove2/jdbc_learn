import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCTest10 {
    /* JDBC事务机制
        1. JDBC中的事务是自动提交的，什么是自动提交？
            只要执行任意一条DML语句，则自动提交一次。这是JDBC默认的事务行为。但是在实际的业务中，通常都是由n条DML语句共同联合完成的，
            必须保证他们这些DML语句在同一个事务中同时成功或同时失败。
        2.
    */
    public static void main(String[] args) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        String url = "jdbc:mysql://127.0.0.1:3306/genesis?characterEncoding=UTF-8&&serverTimezone=GMT";
        String username = "root";
        String password = "wisemove";

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 获取连接
            connection = DriverManager.getConnection(url, username, password);

            // 3. 获取数据库操作对象
            String sql = "update admin set password = ? where username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "bbb");
            preparedStatement.setString(2, "aaa");

            // 4. 执行sql
            int count = preparedStatement.executeUpdate();
            System.out.println(count);
            // 在上面这一行打上断点，然后查看此时的count时，结果已经发生了改变。并且程序没有运行结束，数据库中的内容就结束了。这不应该使我们期望看到的。万一后面出现问题的话，这一行修改应该不成立。
            // 所以得出了一个结论，jdbc中只要执行一条dml语句，就提交一次。

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
