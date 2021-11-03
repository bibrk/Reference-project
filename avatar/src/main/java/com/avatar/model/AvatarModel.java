package com.avatar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AvatarModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long headId;
	private Long bodyId;
	private Long legId;
	private Long userId;
	
	public AvatarModel(Long headId, Long bodyId, Long legId, Long userId) {
		super();
		this.headId = headId;
		this.bodyId = bodyId;
		this.legId = legId;
		this.userId = userId;
	}

	public AvatarModel() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHeadId() {
		return headId;
	}

	public void setHeadId(Long headId) {
		this.headId = headId;
	}

	public Long getBodyId() {
		return bodyId;
	}

	public void setBodyId(Long bodyId) {
		this.bodyId = bodyId;
	}

	public Long getLegId() {
		return legId;
	}

	public void setLegId(Long legId) {
		this.legId = legId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	

}
