package com.avatar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.avatar.model.AvatarModel;

@Repository
public interface AvatarModelRepository extends JpaRepository<AvatarModel,Long>{

	List<AvatarModel> findByUserId(Long userId);
}
