package com.example.jwtapi.repository;

import com.example.jwtapi.model.UserDao;

import org.springframework.data.repository.CrudRepository;

//CrudRepository basically allows crud operations to performed on a specific repository
public interface UserRepository extends CrudRepository<UserDao, Integer> {
    UserDao findByuserName(String userName);
}
