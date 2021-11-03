package com.avatar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avatar.model.FileDescriptor;
import com.avatar.repository.FileDescriptorRepository;

@Service
@Transactional
public class FileDescriptorService {

	@Autowired
	private FileDescriptorRepository descriptorRepository;
	
	public List<FileDescriptor> findAll() {
		return this.descriptorRepository.findAll();
	}
	
	public List<FileDescriptor> findAllByTypeId(Long typeId) {
		return this.descriptorRepository.findAllByTypeId(typeId);
	}
	
	public FileDescriptor save(FileDescriptor data) {
		return this.descriptorRepository.save(data);
	}
	
}
