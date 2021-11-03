package com.avatar.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.avatar.model.AvatarModel;
import com.avatar.model.User;
import com.avatar.model.VerificationToken;
import com.avatar.repository.AvatarModelRepository;
import com.avatar.repository.UserRepository;
import com.avatar.service.UserService;


@Controller
@RequestMapping("/avatar")
@Validated
public class UserController {
	
	

		
		@Autowired
		private UserRepository userRepository;
		@Autowired
		private UserService userService;
		@Autowired
		private AvatarModelRepository modelRepository;
		@Autowired
	    private MessageSource messages;
		private static final Logger logger = 
		        LoggerFactory.getLogger(UserController.class);
		
		
		
	     
	    @PreAuthorize("hasRole('ROLE_USER')")
	    @GetMapping("/user/profile")
		public ResponseEntity<User> userProf() {
	    	User user = userRepository.findByEmail(
	    			SecurityContextHolder.getContext().getAuthentication().getName());
			return ResponseEntity.ok(user);
		}
	    @PreAuthorize("hasRole('ROLE_USER')")
	    @GetMapping("/user/profile/details")
	    @ResponseBody
		public Map<String, String> userProfDetails() {
	    	User user = userRepository.findByEmail(
	    			SecurityContextHolder.getContext().getAuthentication().getName());
	    	
	    	Map<String, String> map = new HashMap<>();
	    	map.put("firstName", user.getFirstName());
	    	map.put("lastName", user.getLastName());
	    	map.put("email", user.getEmail());
	    	map.put("password", user.getPassword());
			return map;
		}
	    
	    @PreAuthorize("hasRole('ROLE_USER')")
	    @GetMapping("/user/profile/saved")
	    @ResponseBody
		public List<Map<String,String>> showSavedModels() {
	    	User user = userRepository.findByEmail(
	    			SecurityContextHolder.getContext().getAuthentication().getName());
	    	List<Map<String, String>> re = new ArrayList<Map<String,String>>();
	    	List<AvatarModel> avatars = modelRepository.findByUserId(user.getId());
	    	
	    	
	    	for(AvatarModel avatar: avatars) {
	    		String head = avatar.getHeadId().toString();
	    		String body = avatar.getBodyId().toString();
	    		String leg = avatar.getLegId().toString();
		    	Map<String, String> map = new HashMap<>();
	    		map.put("head", head);
	    		map.put("body", body);
	    		map.put("leg", leg);
	    		re.add(map);
	    	}
	    
	    	
	    	return re;
		}
		
	    
		
	    
}
