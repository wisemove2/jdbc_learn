import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

public class JDBCTest07 {

    /*
    * 1. 解决sql注入问题
    *   只要用户提供的信息不参与sql语句的编译过程，问题就解决了。即使用户提供的信息中含有sql关键字，但是没有参与编译，不起作用
    *   要想用户信息不参与sql语句的编译，那么必须使用java.sql.PreparedStatement
    *   PreparedStatement继承了Statement，PreparedStatement是属于预编译的数据库操作对象。预先对SQl语句框架进行了编译。
    *   然后只能给sql语句传值。
    *
    * */

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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResourceBundle resourceBundle = ResourceBundle.getBundle("jdbc");
        String url = resourceBundle.getString("url");
        String username = resourceBundle.getString("user");
        String password = resourceBundle.getString("password");
        // 先写sql语句的框架，一个问号代表一个占位符。一个问号后面将接收一个值
        String sqlzhuru = "select * from admin where username = '" + userLoginInfo.get("username") + "' and password = '" + userLoginInfo.get("password") + "'";
        String sql = "select * from admin where username = ? and password = ?";
        System.out.println(sqlzhuru);
        System.out.println(sql);
        // 打印一下这个sql语句，并且这些关键字参与了sql语句的编译过程，导致sql语句的原意被扭曲，进而达到了sql注入

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 建立连接
            connection = DriverManager.getConnection(url, username, password);

            // 3. 获取预编译的数据库操作对象
            preparedStatement = connection.prepareStatement(sql);

            // 给占位符传值；这里也有setInt,setDouble之类的。
            preparedStatement.setString(1, userLoginInfo.get("username"));
            preparedStatement.setString(2, userLoginInfo.get("password"));

            // 4. 执行sql语句
            resultSet = preparedStatement.executeQuery();

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
