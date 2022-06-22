package com.robocon321.demo.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.CustomUserDetails;
import com.robocon321.demo.entity.user.UserAccount;
import com.robocon321.demo.repository.UserAccountRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount userAccount = userAccountRepository.findByUsername(username);
		if (userAccount == null) {
			throw new UsernameNotFoundException(username);
		}
		
		CustomUserDetails userDetails = new CustomUserDetails();
		userDetails.setUserAccount(userAccount);
		return userDetails;
	}

	public CustomUserDetails loadUserById(int userId) {
		UserAccount userAccount = userAccountRepository.findByUser_id(userId);
		if (userAccount == null) {
			throw new UsernameNotFoundException(userId + "");
		}
		CustomUserDetails userDetails = new CustomUserDetails(userAccount);
		return userDetails;
	}

}
