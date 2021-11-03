package com.avatar.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;

import com.avatar.dto.UserDto;
import com.avatar.exception.ErrorDetails;
import com.avatar.exception.UserAlreadyExistException;
import com.avatar.listener.OnRegistrationCompleteEvent;
import com.avatar.model.User;
import com.avatar.model.VerificationToken;
import com.avatar.service.UserService;

@RestController
@Validated
public class RegistrationController {

	@Autowired
	private UserService userService;
	@Autowired
	ApplicationEventPublisher eventPublisher;
	@Autowired
    private JavaMailSender mailSender;
	@Autowired
	private UserService service;
	private static final Logger logger = 
	        LoggerFactory.getLogger(RegistrationController.class);
	
	
	
	//firstName, lastName, password, email
	@PostMapping("/avatar/registration")
	public String registrateNewUser(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
	    logger.debug("Registering user account with information: {}", userDto);
	    if (service.emailExists(userDto.getEmail())) {
        	throw new UserAlreadyExistException(LocalDate.now(),"Error","There is already an account with this email: " + userDto.getEmail());        	
        }
	    else {
		User registered = userService.registerNewUserAccount(userDto);
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
	    }
		
		return "registration succesfull";
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        
        if(errors.isEmpty()) {
        	errors.put("password", "Passwords must match");
        }
        
        return errors;
    }
	
	
	@GetMapping("/user/resendRegistrationToken")
	public String resendRegistrationToken(
	  HttpServletRequest request, @RequestParam("token") String existingToken) {
	    VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
	    
	    User user = userService.getUser(newToken.getToken());
	    String appUrl = 
	      "http://" + request.getServerName() + 
	      ":" + request.getServerPort() + 
	      request.getContextPath();
	    SimpleMailMessage email = 
	      constructResendVerificationTokenEmail(appUrl, newToken, user);
	    mailSender.send(email);
	    logger.debug("New registration token sent to user with context: " + email);

	    return "Verification token sent.";
	}
	
	@GetMapping("/avatar/registrationConfirm")
	public String confirmRegistration
	  (WebRequest request,HttpServletResponse response, Model model, @RequestParam("token") String token) throws IOException {
	 
	    
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {
	    	logger.debug("confirmRegistration() executed with invalid input.");
	    	
	        model.addAttribute("message", "Invalid Token");
	        return (String) model.getAttribute("message");
	    }
	    
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	logger.debug("confirmRegistration executed, input token has already expired.");
	    	
	        model.addAttribute("message", "Token has already expired");
	        return (String) model.getAttribute("message");
	    }
	    user.setEnabled(true);
	    logger.debug("confirmRegistration executed, user succesfully activated");
	    userService.saveRegisteredUser(user); 
	    model.addAttribute("message", "Account successfully activated");
	    
	    return (String)model.getAttribute("message");
	}
	
	
	private SimpleMailMessage constructResendVerificationTokenEmail
	  (String contextPath, VerificationToken newToken, User user) {
	    String confirmationUrl = 
	      contextPath + "/avatar/registrationConfirm?token=" + newToken.getToken();
	    String message = "New verification Token: ";
	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setSubject("Resend Registration Token");
	    email.setText(message + "\r\n" + confirmationUrl);
	    email.setTo(user.getEmail());
	    return email;
	}
	
}