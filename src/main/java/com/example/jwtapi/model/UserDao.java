package com.example.jwtapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

//DAO classes are basically to put stuff in database
//so this class inserts data into database
@Entity
@Table(name = "user")
public class UserDao {
    
    // id defines primary key and generated value is basically what automatic value is generated for primar key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String userName;

    @Column
    @JsonIgnore
    private String password;

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}

