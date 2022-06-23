package com.robocon321.demo.service.user.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.entity.user.UserAccount;
import com.robocon321.demo.repository.UserAccountRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.user.UserAccountService;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserAccountDTO update(UserAccountDTO userAccountDTO) throws RuntimeException {
		Optional<User> optional = userRepository.findById(userAccountDTO.getUser().getId());
		if(optional.isEmpty()) throw new RuntimeException("User not found");
		else {
			User user = optional.get();
			try {
				if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
					throw new RuntimeException("You don't have session");
				}
				
				UserDTO userSessionDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				if(userSessionDTO.getId() != user.getId()) {
					throw new RuntimeException("You are not allowed change this account");
				} else {
					UserAccount userAccount = userAccountRepository.findByUser_id(userAccountDTO.getUser().getId());
					if(userAccount == null) {
						if(user.getEmail() == null) throw new RuntimeException("You dont have email to make username, please add email to possible login by account");
						userAccount = new UserAccount();
						userAccount.setUser(user);
						userAccount.setUsername(user.getEmail());
						userAccount.setPassword(userAccountDTO.getPassword());
					} else {
						userAccount.setPassword(userAccountDTO.getPassword());				
					}
					userAccount = userAccountRepository.save(userAccount);
					BeanUtils.copyProperties(userAccountDTO, userAccount);					
				}
			} catch(Exception e) {
				throw new RuntimeException("Your session not valid");
			}
		}
		return userAccountDTO;
	}

}
