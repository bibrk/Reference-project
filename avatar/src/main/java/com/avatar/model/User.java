package com.avatar.model;

import java.util.Collection;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="users")
public class User {
	
	private Long id;
	@NotNull(message = "First name cannot be null")
	private String firstName;
	@NotNull(message = "Last name cannot be null")
	private String lastName;
	@Email(regexp=".*@.*\\..*", message = "Email should be valid")
	private String email;
	private String password;
	private boolean enabled;
	private boolean tokenExpired;
	
	
	
    private Collection<Role> roles;
	
	public User(){}

	public User(Long id, String firstName, String lastName, String email, String password, boolean enabled,
			boolean tokenExpired, Collection<Role> roles) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.enabled = false;
		this.tokenExpired = tokenExpired;
		this.roles = roles;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	//@Column(name="firstname", nullable = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	//@Column(name="lastname", nullable = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	//@Column(name="email", nullable = false)

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	//@Column(name="password", nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	//@Column(name="active", nullable = false)
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	//@Column(name="token_valid", nullable = false)
	public boolean isTokenExpired() {
		return tokenExpired;
	}

	public void setTokenExpired(boolean tokenExpired) {
		this.tokenExpired = tokenExpired;
	}
	
	@Access(AccessType.PROPERTY)
	
	@ManyToMany (fetch = FetchType.EAGER)
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
          
	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}


	
	
	
	/*
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	@Email(regexp=".*@.*\\..*", message = "Email should be valid")
	@Column(name="email", nullable = false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "deleted", nullable = true)
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
	
	
	@Override
	public String toString() {
		
		return"User {" + "id: "+ id + ", email: "+ email + "is_deleted: " + deleted + "}";
		
	}
	*/
}
