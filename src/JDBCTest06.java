import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

public class JDBCTest06 {

    public static void main(String[] args){
        // 1. 初始化界面
        Map<String, String> userLoginInfo = initUI();

        // 2. 验证用户和密码
        boolean success = login(userLoginInfo);

        if(success){
            System.out.println("登陆成功!");
        }else{
            System.out.println("登陆失败!");
        }
    }

    private static boolean login(Map<String, String> userLoginInfo) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("jdbc");
        String url = resourceBundle.getString("url");
        String username = resourceBundle.getString("user");
        String password = resourceBundle.getString("password");
        String sql = "select * from admin where username = '" + userLoginInfo.get("username") + "' and password = '" + userLoginInfo.get("password") + "'";
        System.out.println(sql);
        // 打印一下这个sql语句，并且这些关键字参与了sql语句的编译过程，导致sql语句的原意被扭曲，进而达到了sql注入

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 建立连接
            connection = DriverManager.getConnection(url, username, password);

            // 3. 获取数据库操作对象
            statement = connection.createStatement();

            // 4. 执行sql语句
            resultSet = statement.executeQuery(sql);

            // 5. 处理结果集
            return resultSet.next();

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


        return false;
    }


    /**
     * 初始化用户界面
     * return 用户输入的用户名和密码
     */
    private static Map<String,String> initUI() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("用户名：");
        String username = scanner.nextLine();
        System.out.print("密码：");
        String password = scanner.nextLine();

        Map<String, String> userLoginInfo = new HashMap<>();
        userLoginInfo.put("username", username);
        userLoginInfo.put("password", password);

        return userLoginInfo;
    }

}
