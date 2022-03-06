package com.example.jwtapi.conig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.jwtapi.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

//OncePerRequestFilter provides single execution per request also provides doFilterInternal Method
//this class is basically used to filter the http request,
//take out the header and take out the token
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException{
        final String requestTokenHeader = request.getHeader("Autherization");
        String userName = null;
        String jwtToken = null;

        //jwt has bearer, so we remove bearer and retrieve just the token from the header
        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")){
            //.substring gives begining index, bearer + space is 6 so token starts at 7th index
            jwtToken = requestTokenHeader.substring(7);
            try{
                userName = jwtTokenUtil.getUserNameFromToken(jwtToken);
            }catch(IllegalArgumentException e){
                System.out.println("Unable to get JWT token");
            } catch(ExpiredJwtException e){
                System.out.println("JWT token has expired");
            }
        }
        else{
            logger.warn("JWT does not begin with Bearer String");
        }

        //after getting the token we validate it
        //Interface defining the minimum security information associated with the current thread
        //of execution.
        if(userName !=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(userName);
        
            //if token is valid configure Spring Security to manually set authentication
            if(jwtTokenUtil.validateToken(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // After setting the Authentication in the context, we specify
				    // that the current user is authenticated. So it passes the Spring Security Configurations successfully.

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        //chain invoked next filter in chain or if its the last fiter it will just invoke resource
        chain.doFilter(request, response);
    }
}
