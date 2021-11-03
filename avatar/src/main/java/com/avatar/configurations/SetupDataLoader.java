package com.avatar.configurations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avatar.model.Privilege;
import com.avatar.model.Role;
import com.avatar.model.User;
import com.avatar.repository.PrivilegeRepository;
import com.avatar.repository.RoleRepository;
import com.avatar.repository.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent>{
	
	boolean alreadySet = false;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PrivilegeRepository privilegeRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	 @Override
	    @Transactional
	    public void onApplicationEvent(ContextRefreshedEvent event) {
	 
	        if (alreadySet)
	            return;
	        Privilege readPrivilege
	          = createPrivilegeIfNotFound("READ_PRIVILEGE");
	        Privilege writePrivilege
	          = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
	 
	        List<Privilege> adminPrivileges = Arrays.asList(
	          readPrivilege, writePrivilege);
	        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
	        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

	        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
	        Role userRole = roleRepository.findByName("ROLE_USER");
	        User admin = new User();
	        admin.setFirstName("admin");
	        admin.setLastName("admin");
	        admin.setPassword(passwordEncoder.encode("admin"));
	        admin.setEmail("admin@test.com");
	        admin.setRoles(Arrays.asList(adminRole));
	        admin.setEnabled(true);
	        userRepository.save(admin);
	        
	        User user = new User();
	        user.setFirstName("user");
	        user.setLastName("user");
	        user.setPassword(passwordEncoder.encode("user"));
	        user.setEmail("user@test.com");
	        user.setRoles(Arrays.asList(userRole));
	        user.setEnabled(true);
	        userRepository.save(user);
	        
	        User user2 = new User();
	        user2.setFirstName("user_two");
	        user2.setLastName("user_two");
	        user2.setPassword(passwordEncoder.encode("user"));
	        user2.setEmail("user_two@test.com");
	        user2.setRoles(Arrays.asList(userRole));
	        user2.setEnabled(true);
	        userRepository.save(user2);
	        
			
			
	        alreadySet = true;
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
	    
	}


