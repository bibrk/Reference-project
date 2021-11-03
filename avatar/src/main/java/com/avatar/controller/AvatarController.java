package com.avatar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avatar.dto.ModelDto;
import com.avatar.model.AvatarModel;
import com.avatar.model.User;
import com.avatar.repository.AvatarModelRepository;
import com.avatar.repository.UserRepository;

@RestController
@RequestMapping("/builder")
public class AvatarController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	AvatarModelRepository avatarRepository;
	
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/save")
	public String saveModel(@RequestBody ModelDto modelDto ) {
		
		User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		
		AvatarModel avatarModel = new AvatarModel();
		avatarModel.setHeadId(modelDto.getHeadId());
		avatarModel.setBodyId(modelDto.getBodyId());
		avatarModel.setLegId(modelDto.getLegId());
		avatarModel.setUserId(user.getId());
		avatarRepository.save(avatarModel);
		
		return "Avatar saved successfully";
		
	}
	
	

}
