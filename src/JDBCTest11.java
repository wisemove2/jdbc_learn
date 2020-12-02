import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCTest11 {

    /*
    * jdbc事务的三行控制：注意观察三行代码出现的位置。
    *
    * connection.setAutoCommit(false);
    * connection.commit();
    * connection.rollback();
    *
    * */

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
            connection.setAutoCommit(false);

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
            connection.commit();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            // 如果抓取到异常，并且已经建立连接，那么之前的dml语句都得在没有提交前回滚回去。
            if(connection != null){
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
