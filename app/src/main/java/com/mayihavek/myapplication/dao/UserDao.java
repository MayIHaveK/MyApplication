package com.mayihavek.myapplication.dao;

import com.mayihavek.myapplication.entity.User;
import com.mayihavek.myapplication.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author MayIHaveK
 * @Date 2023/6/16 13:06
 */
public class UserDao {

    public static void addUser(User user) throws SQLException {
        Connection conn = JDBCUtils.getConn();
        String sql = "INSERT INTO `user`.`user` (`userName`, `userPassword`, `userAccount`) VALUES (?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1,user.getUserName());
        ps.setString(2,user.getUserPassword());
        ps.setString(3,user.getUserAccount());

        ps.executeUpdate();
        JDBCUtils.closeResource(conn,ps);
    }

    public static boolean hasUserAccount(String userAccount) throws SQLException {
        Connection conn = JDBCUtils.getConn();
        String sql = "select * from user.user WHERE userAccount=?;";
        PreparedStatement pst=conn.prepareStatement(sql);

        pst.setString(1,userAccount);

        ResultSet rs = pst.executeQuery();
        return rs.next();
    }

    public static boolean login(String userAccount,String userPassword) throws SQLException {
        Connection conn = JDBCUtils.getConn();
        String sql = "select * from user.user WHERE userAccount=? and userPassword=?;";

        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1,userAccount);
        pst.setString(2,userPassword);

        ResultSet resultSet = pst.executeQuery();
        return resultSet.next();
    }

}
