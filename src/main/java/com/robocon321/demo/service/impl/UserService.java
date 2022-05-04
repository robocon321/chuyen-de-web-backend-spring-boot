package com.robocon321.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.CustomUserDetailsDomain;
import com.robocon321.demo.entity.user.UserAccount;
import com.robocon321.demo.repository.UserAccountRepository;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccount user = userAccountRepository.findByUname(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetailsDomain(user);
	}

	public UserDetails loadUserById(int userId) {
		UserAccount user = userAccountRepository.findByUser_id(userId);
		if (user == null) {
			throw new UsernameNotFoundException(userId+"");
		}
		return new CustomUserDetailsDomain(user);
	}

}
