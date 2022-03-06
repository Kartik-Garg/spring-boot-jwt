package com.example.jwtapi.service;
import java.util.ArrayList;

import com.example.jwtapi.model.UserDao;
import com.example.jwtapi.model.UserDto;
import com.example.jwtapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//using hardcoded userdetails to check working of jwt
//will use sql later on

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	//this method basically interacts with the DB and returns user name and password
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserDao user = userDao.findByuserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + userName);
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				new ArrayList<>());
	}

	//this method saves data to the DB
	//Dto layer gets data from user sets it in dao layer and dao layer sets data to the db
	public UserDao save(UserDto user){
		UserDao newUser = new UserDao();
		newUser.setUserName(user.getUsername());
		newUser.setPassword(user.getPassword());
		return userDao.save(newUser);
	}
}
