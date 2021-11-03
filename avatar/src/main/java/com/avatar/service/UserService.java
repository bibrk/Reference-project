package com.avatar.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import com.avatar.dto.UserDto;
import com.avatar.exception.ErrorDetails;
import com.avatar.exception.GlobalExceptionHandler;
import com.avatar.exception.UserAlreadyExistException;
import com.avatar.model.PasswordResetToken;
import com.avatar.model.Privilege;
import com.avatar.model.Role;
import com.avatar.model.User;
import com.avatar.model.VerificationToken;
import com.avatar.repository.PasswordResetTokenRepository;
import com.avatar.repository.PrivilegeRepository;
import com.avatar.repository.RoleRepository;
import com.avatar.repository.UserRepository;
import com.avatar.repository.VerificationTokenRepository;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@Transactional
@Validated
public class UserService {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passEncoder;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    PasswordResetTokenRepository passwordTokenRepository;
    
    @Autowired
    private PrivilegeRepository privilegeRepository;
    
    @Autowired
    private VerificationTokenRepository tokenRepository;
    
    
    
    public User registerNewUserAccount(final @Valid UserDto accountDto) {
        
        Privilege readPrivilege
        = createPrivilegeIfNotFound("READ_PRIVILEGE");
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));
        Role userRole = roleRepository.findByName("ROLE_USER");
        final User user = new User();

        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(passEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setEnabled(Boolean.FALSE);
        user.setTokenExpired(false);      
        user.setRoles(Arrays.asList(userRole));
        return userRepository.save(user);
    }
    
    
    

    
    public boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }
    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
 
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
      String name, Collection<Privilege> privileges) {
 
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }


	public void createVerificationToken(final User user, final String token) {
		 final VerificationToken newToken = new VerificationToken(token, user);
	        tokenRepository.save(newToken);
	}
	public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

	public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }
	
	//Generating new verification token from existing one
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
            .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

	//Finding User by verification token
	public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }
	
	
	//Password Reset Functions --------------------------------
	public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken passToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(passToken);
    }
	public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }
	public User getUserByPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token) .getUser();
    }
	public void changeUserPassword(User user, String password) {
	    user.setPassword(passEncoder.encode(password));
	    userRepository.save(user);
	}
	
}



