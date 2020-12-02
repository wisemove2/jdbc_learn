import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCTest12 {
    /*
    * 1. 测试DBUtil是否好用
    * 2. 测试模糊查询
    * */

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            // 包含第一步和第二步，第一步通过静态代码块隐藏起来了。
            connection = DBUtil.getConnection();

            // 3. 获取数据库操作对象
            String sql = "select username from admin where username like ?";
            preparedStatement = connection.prepareStatement(sql);
            // 查询最后一个用户名中最后一个字符为g的
            preparedStatement.setString(1, "%g");

            // 4. 执行sql
            resultSet = preparedStatement.executeQuery();

            // 5. 处理结果集
            while(resultSet.next()){
                System.out.println(resultSet.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            // 第六步：释放资源
            DBUtil.close(connection, preparedStatement, resultSet);
        }

    }
}
