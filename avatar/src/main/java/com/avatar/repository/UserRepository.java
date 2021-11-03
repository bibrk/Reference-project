package com.avatar.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.avatar.model.Role;
import com.avatar.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
	
	
	 User findByEmail(String email);
	 
	 
	 List<User> findAllByEnabled(Boolean enabled);
	 
	 List<User> findByRoles(Role roles);

	    @Override
	    void delete(User user);
}
