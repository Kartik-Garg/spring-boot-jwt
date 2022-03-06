package com.example.jwtapi.model;

//UserDto gets info from user, passes it to the DAO and then DAO puts it in database
public class UserDto {
    
    private String userName;
    private String password;

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
