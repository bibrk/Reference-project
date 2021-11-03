package com.avatar.controller;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.avatar.configurations.SecurityConstants;
import com.avatar.dto.LoginDto;
import com.avatar.model.JwtUtility;
import com.avatar.model.User;
import com.avatar.repository.UserRepository;
import com.avatar.service.AllUserDetailsService;

@RestController
public class LoginController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AllUserDetailsService userDetailsService;
	@Autowired
	JwtUtility jwtUtility;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String,String> login(@RequestBody LoginDto userRequest) {
	    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword()));
	    boolean isAuthenticated = isAuthenticated(authentication);
	    if (isAuthenticated) {
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	    }
	    final UserDetails userDetails = userDetailsService.loadUserByUsername(userRequest.getUsername());
	    final String token = jwtUtility.generateToken(userDetails);
	    Map<String,String> jwt = new HashMap<String, String>();
	    jwt.put("jwt", token);
	    jwt.put("role", userDetails.getAuthorities().toString());
	    return jwt;
	    //return authentication;
	    
	    
	}

	private boolean isAuthenticated(Authentication authentication) {
	    return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}
	
	@GetMapping("/logout")
	public void logout(HttpRequest request){
		
	}
}
