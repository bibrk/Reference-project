package com.avatar.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "c_code_store_item")
public class CodeStoreItem {

	@Id
	private Long id;

	@Column(name = "type_id", insertable = false, updatable = false)
	private Long typeId;
	
	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "type_id")
	private CodeStoreType type;

	@Column(name = "text")
	private String text;

}
