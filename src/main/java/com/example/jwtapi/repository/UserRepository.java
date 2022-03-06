package com.example.jwtapi.repository;

import com.example.jwtapi.model.UserDao;
import org.springframework.data.repository.CrudRepository;
//CrudRepo basically lets us perform crud operations on given class here it is DAO class
public interface UserRepository extends CrudRepository<UserDao, Integer> {
    UserDao findByUsername(String username);
}
