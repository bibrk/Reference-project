package com.avatar.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.avatar.configurations.PasswordMatches;
import com.avatar.configurations.ValidPassword;


@PasswordMatches
public class UserDto {
	
	@NotNull(message = "First name cannot be null")
	@NotEmpty(message = "First name cannot be empty")
	private String firstName;
	
	@NotNull(message = "Last name cannot be null")
	@NotEmpty(message = "Last name cannot be empty")
	private String lastName;
	
	@ValidPassword
	private String password;
	
	@ValidPassword
	private String matchingPassword;
	
	@NotNull(message = "Email cannot be null")
	@NotEmpty(message = "Email cannot be empty")
	@Email(regexp=".*@.*\\..*", message = "Email should be valid")
	private String email;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lasttName) {
		this.lastName = lasttName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
