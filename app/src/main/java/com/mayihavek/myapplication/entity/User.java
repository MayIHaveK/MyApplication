package com.mayihavek.myapplication.entity;

/**
 * @author MayIHaveK
 * @Date 2023/6/16 13:06
 */
public class User {

    private String userName;
    private String userPassword;
    private String userAccount;

    public User(String userName,String userPassword,String userAccount)
    {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userAccount = userAccount;

    }

    public User(String userPassword,String userAccount)
    {
        this.userPassword = userPassword;
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }


}
