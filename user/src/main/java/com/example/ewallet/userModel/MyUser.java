package com.example.ewallet.userModel;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.ewallet.utilsEnums.UserIdentifier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyUser implements Serializable, UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@Column(unique = true)
	private String phoneNumber;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	private String dob;
	
	private String authorities;
	
	@Enumerated(value = EnumType.STRING)
	private UserIdentifier userIdentifier;
	
	private String identifierValue;
	
	@CreationTimestamp
	private Date createdOn;
	
	@UpdateTimestamp
	private Date updatedOn;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String[] authorities = this.authorities.split(UserConstants.AUTHORITIES_DELIMITER);
		return Arrays.stream(authorities).map(x -> new SimpleGrantedAuthority(x))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
