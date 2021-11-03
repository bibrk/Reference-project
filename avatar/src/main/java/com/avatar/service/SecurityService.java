package com.avatar.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.avatar.model.PasswordResetToken;
import com.avatar.model.User;
import com.avatar.repository.PasswordResetTokenRepository;
import com.avatar.repository.UserRepository;

@Service
public class SecurityService {
	@Autowired
	PasswordResetTokenRepository passwordTokenRepository;
	@Autowired
	PasswordEncoder passEncoder;
	@Autowired
	UserRepository userRepository;
	
	public String validatePasswordResetToken(String token) {
	    final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

	    return !isTokenFound(passToken) ? "invalidToken"
	            : isTokenExpired(passToken) ? "expired"
	            : null;
	}

	private boolean isTokenFound(PasswordResetToken passToken) {
	    return passToken != null;
	}

	private boolean isTokenExpired(PasswordResetToken passToken) {
	    final Calendar cal = Calendar.getInstance();
	    return passToken.getExpiryDate().before(cal.getTime());
	}
	
	//PASSWORD CHANGE

    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passEncoder.matches(oldPassword, user.getPassword());
    }

}
