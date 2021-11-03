package com.avatar.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.avatar.model.AllUserDetails;
import com.avatar.model.User;
import com.avatar.repository.UserRepository;
@Service
public class AllUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if(user==null) {
			throw new UsernameNotFoundException("User not found with this email.");
		}
		
		
		return new AllUserDetails(user);
	}

	
}
