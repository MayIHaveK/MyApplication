package com.mayihavek.myapplication.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库工具类，连接数据库用
 */
public class JDBCUtils {

    private static final String URL = "jdbc:mysql://43.139.222.77:3306/user?useUnicode=true&useSSL=false&characterEncoding=Utf8&serverTimezone=Asia/Shanghai";
    private static final String USERNAME = "MayIHaveK";
    private static final String PASSWORD = "2534226689";

    public static Connection getConn(){

        Connection connection = null;
        try{
            // MySql驱动
            String driver = "com.mysql.jdbc.Driver";
            // 动态加载类
            Class.forName(driver);
            // 尝试建立到给定数据库URL的连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);


        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeResource(Connection conn, Statement ps){

        try {
            if (ps != null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void closeResource(Connection conn, Statement ps, ResultSet rs){

        try {
            if (ps != null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (rs != null){
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}