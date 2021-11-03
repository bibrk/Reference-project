package com.avatar.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.avatar.model.FileDescriptor;

public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, Long> {
	
	List<FileDescriptor> findAllByTypeId(Long typeId);
		
}
