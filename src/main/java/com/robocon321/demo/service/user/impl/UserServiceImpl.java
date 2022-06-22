package com.robocon321.demo.service.user.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDTO update(UserDTO userDTO) throws RuntimeException {
		Optional<User> opt = userRepository.findById(userDTO.getId());
		if(opt.isPresent()) {
			User user = opt.get();
			BeanUtils.copyProperties(userDTO, user);
			
			try {
				if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
					UserDTO userModDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Optional<User> optModDTO = userRepository.findById(userModDTO.getId());
					if(optModDTO.isPresent()) {
						user.setModifiedUser(optModDTO.get());
					}			
				}				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Your session not valid");
			}
			
			user = userRepository.save(user);
			BeanUtils.copyProperties(user, userDTO);
			return userDTO;	
		} else {
			throw new RuntimeException("Your account not found");
		}
	}


}
