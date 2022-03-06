package com.example.jwtapi.conig;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


//config file for jwt for generating and validating jwt token
@Component
public class JwtTokenUtil implements Serializable{

    private static final long serialVersionUID = -2550185165626007488L;
    //time for which a jwt token will be valid.
    public static final long JWT_TOKEN_VALIDITY = 5*60*60;
    
    @Value("${jwt.secret}")
    private String secret;

    public String getUserNameFromToken(String token){
        //pass token and subject part of claim from token to the given method
        return getClaimFromToken(token, Claims::getSubject);
    }

    //get the time when token was issues/generated
    public Date getIssuedAtDateFromToken(String token){
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //using generics here as we will return different data from the claims part of the JWT
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //to return the claims from the given jwt token,
    //here we are setting a key called secret and parsing through the token and returning the body 
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //to check if the token is expired or not
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        //.before returns true if expiration date is ealier then the given argument
        return expiration.before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token){
        //here we can specify the tokens for which we want to ignore the expirationTime
        return false;
    }

    //method to pass claims map and userdetails to create JWT token
    public String generateToken(UserDetails userDetails){
        //userdetails in the parameter represents the username and passwod i.e. the payload
        //user name and password used to login or hit the particular api
        Map<String, Object> claims = new HashMap<>();
        //pass the claims map and the userName to given method.
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //method to create JWT token
    private String doGenerateToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //we pass the token here which will not expire for time
    public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

    //this method is used to validate token,that is if its correct or not
    public boolean validateToken(String token, UserDetails userDetails){
        final String userName = getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
