package com.robocon321.demo.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.entity.user.UserAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
	private UserAccount userAccount;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantAuthorities = new ArrayList<GrantedAuthority>();
		try {
			userAccount.getUser().getRoles().stream().forEach(role -> {
				GrantedAuthority grant = new SimpleGrantedAuthority(role.getName());
				grantAuthorities.add(grant);
			});			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return grantAuthorities;
	}
	
	public User getUser() {
		return userAccount.getUser();
	}
	
	@Override
	public String getPassword() {		
		return userAccount.getPassword();
	}

	@Override
	public String getUsername() {
		return userAccount.getUsername();
	}
		
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
