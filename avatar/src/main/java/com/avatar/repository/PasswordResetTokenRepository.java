package com.avatar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avatar.model.PasswordResetToken;
import com.avatar.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

}
