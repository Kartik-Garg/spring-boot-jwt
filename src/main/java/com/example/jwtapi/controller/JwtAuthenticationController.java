package com.example.jwtapi.controller;

import java.util.Objects;

import com.example.jwtapi.conig.JwtTokenUtil;
import com.example.jwtapi.model.JwtRequest;
import com.example.jwtapi.model.JwtResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


//cross origin allows requests from all the origin 
//this class is basically for authenticating user name and password passed in the body
@RestController
@CrossOrigin
public class JwtAuthenticationController {
    
    //authentication manager is used to authenticate the username and password
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
    throws Exception{
        //response enity allows us to access response header, body everything but responsebody 
        //allows us to just add response to the body of the HTTP response
        
            authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());

            //basically this takes in memory jwt and gets username 
            final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUserName);

            //call the generateToken method in JwtTokenUtil class
            final String token = jwtTokenUtil.generateToken(userDetails);
            
           // return ResponseEntity.ok(new JwtResponse(token));
           return ResponseEntity.ok(new JwtResponse(token));
        }

        private void authenticate(String userName, String password) throws Exception{
            Objects.requireNonNull(userName);
            Objects.requireNonNull(password);

            try{
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            }catch(DisabledException e){
                throw new Exception("USER_DISABLED", e);
            } catch(BadCredentialsException e){
                throw new Exception("INVALID_CREDENTIALS", e);
            }
        }
}
