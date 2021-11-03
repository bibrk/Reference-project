package com.avatar.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.avatar.model.FileDescriptor;
import com.avatar.model.User;
import com.avatar.service.FileDescriptorService;
import com.avatar.util.CodeStoreKeys;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/file")
public class FileDescriptorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileDescriptorController.class);
	
	@Autowired
	private FileDescriptorService descriptorService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${avatar.upload.dir}")
	private String uploadDirProperty;
	
	@GetMapping("/findAll")
	public ResponseEntity<String> findAll() {
		List<FileDescriptor> resultList = this.descriptorService.findAll();
		try {
			return new ResponseEntity<>(this.objectMapper.writeValueAsString(resultList), HttpStatus.OK);
		} catch (JsonProcessingException e) {
			return new ResponseEntity<>("{\"reason\": \"Query error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getHeadImages")
	public ResponseEntity<String> getHeadImages() {
		List<FileDescriptor> resultList = this.descriptorService.findAllByTypeId(CodeStoreKeys.ImageType.HEAD);
		try {
			return new ResponseEntity<>(this.objectMapper.writeValueAsString(resultList), HttpStatus.OK);
		} catch (JsonProcessingException e) {
			return new ResponseEntity<>("{\"reason\": \"Query error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getBodyImages")
	public ResponseEntity<List<FileDescriptor>> getBodyImages() {
		return ResponseEntity.ok(this.descriptorService.findAllByTypeId(CodeStoreKeys.ImageType.BODY));
	}
	
	@GetMapping("/getLegImages")
	public ResponseEntity<List<FileDescriptor>> getLegImages() {
		return ResponseEntity.ok(this.descriptorService.findAllByTypeId(CodeStoreKeys.ImageType.LEG));
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	public ResponseEntity<String> upload(MultipartHttpServletRequest fileData) {
		
		try {
			String userHome = System.getProperty("user.home");
			File uploadDir = new File(userHome + uploadDirProperty);
			uploadDir.mkdirs();
	
			File uploadedFile = new File(fileData.getFile("file").getOriginalFilename());
			fileData.getFile("file").transferTo(uploadedFile);
	
			File savedFile = new File(uploadDir.getAbsolutePath() + File.separator, uploadedFile.getName());
			if (savedFile.exists() && !savedFile.isDirectory()) {
				int c = 1;
				do {
					savedFile = new File(uploadDir.getAbsolutePath() + File.separator, c + "_" + uploadedFile.getName());
					c++;
				} while (savedFile.exists());
			}
			savedFile.createNewFile();
			
			FileDescriptor fileDescriptor = new FileDescriptor();
			fileDescriptor.setFileName(savedFile.getName());
			fileDescriptor.setTypeId(Long.valueOf(fileData.getParameter("typeId")));
			fileDescriptor.setUrl(savedFile.getAbsolutePath());
			
			this.descriptorService.save(fileDescriptor);
		} catch (Exception e) {
			LOGGER.error("upload - ", e);
			return new ResponseEntity<>("{\"reason\": \"Upload error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>("{\"reason\": \"Upload succesful\"}", HttpStatus.OK);
	}
}
