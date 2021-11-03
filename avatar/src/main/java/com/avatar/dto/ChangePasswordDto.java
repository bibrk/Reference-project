package com.avatar.dto;

import com.avatar.configurations.ValidPassword;

public class ChangePasswordDto {

	
	private String oldPasssword;
	@ValidPassword	
	private String newPassword;
	public String getOldPasssword() {
		return oldPasssword;
	}
	public void setOldPasssword(String oldPasssword) {
		this.oldPasssword = oldPasssword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
