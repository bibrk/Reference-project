package com.avatar.dto;

import com.avatar.configurations.ValidPassword;

public class PasswordDto {


    private  String token;
    
    @ValidPassword
    private String newPassword;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
    
    
}
