package com.avatar.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avatar.dto.ChangePasswordDto;
import com.avatar.dto.PasswordDto;
import com.avatar.exception.InvalidOldPasswordException;
import com.avatar.exception.UserNotFoundException;
import com.avatar.model.User;
import com.avatar.repository.UserRepository;
import com.avatar.service.SecurityService;
import com.avatar.service.UserService;
@Validated
@RestController
@RequestMapping("/avatar")
public class PasswordController {
	
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	JavaMailSender mailSender;
	@Autowired
	SecurityService securityService;
	private static final Logger logger = 
	        LoggerFactory.getLogger(PasswordController.class);
	
	//Forgot password
	@PostMapping("/user/resetPassword")
	public String resetPassword(HttpServletRequest request, 
	  @RequestParam("email") String userEmail) {
	    User user = userRepository.findByEmail(userEmail);
	    if (user == null) {
	        throw new UserNotFoundException("User not found");
	        
	    }
	    String token = UUID.randomUUID().toString();
	    userService.createPasswordResetTokenForUser(user, token);
	    mailSender.send(constructResetTokenEmail(getAppUrl(request), token, user));
	    logger.debug("Password reset token sent to " + userEmail + "with details: "+ constructResetTokenEmail(getAppUrl(request), token, user));
	    return "Password reset token sent.";
	}
	//Checking password reset token
	@GetMapping("/user/changePassword")
	public String showChangePasswordPage( Model model, @RequestParam("token") String token) {
	    String result = securityService.validatePasswordResetToken(token);
	    if(result != null) {
	        
	        return "Invalid token";
	            
	    } else {
	        model.addAttribute("token", token);
	        return "Password token valid.";
	    }
	}
	//Saving new password for user
	@PostMapping("/user/savePassword")
	public String savePassword( @Valid PasswordDto passwordDto) {

	    String result = securityService.validatePasswordResetToken(passwordDto.getToken());

	    if(result != null) {
	        return result;
	    }

	    User user = userService.getUserByPasswordResetToken(passwordDto.getToken());
	    if(user !=null) {
		    logger.debug("Password succesfully changed for user: " + user.getEmail());

	        userService.changeUserPassword(user, passwordDto.getNewPassword());
	        return "Password succesfully changed";
	        
	    } else {
	    	throw new UserNotFoundException("User not found");
	    }

	}
	//User changing own password
	@PostMapping("/user/updatePassword")
	@PreAuthorize("hasRole('READ_PRIVILEGE')")
	public String changeUserPassword(@Valid @RequestBody ChangePasswordDto dto) {
	    User user = userRepository.findByEmail(
	      SecurityContextHolder.getContext().getAuthentication().getName());
	    
	    if (!securityService.checkIfValidOldPassword(user, dto.getOldPasssword())) {
	        throw new InvalidOldPasswordException("Invalid old password");
	    }
	    userService.changeUserPassword(user, dto.getNewPassword());
	    logger.debug("Password succesfully updated by: " + user.getEmail());

	    return "Password changed succesfully";
	}
	
	
	
	//Functions
	private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
	private SimpleMailMessage constructResetTokenEmail(
			  String contextPath, String token, User user) {
			    String url = contextPath + "/user/changePassword?token=" + token;
			    ;
			    return constructEmail("Reset Password", "Your link:" + " \r\n" + url + " \r\n" + "Your token: " + token, user);
			}
	private SimpleMailMessage constructEmail(String subject, String body, 
			  User user) {
			    SimpleMailMessage email = new SimpleMailMessage();
			    email.setSubject(subject);
			    email.setText(body);
			    email.setTo(user.getEmail());
			    return email;
			}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        

        return errors;
    }
}
