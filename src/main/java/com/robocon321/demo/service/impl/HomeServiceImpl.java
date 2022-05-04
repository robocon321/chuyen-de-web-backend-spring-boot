package com.robocon321.demo.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robocon321.demo.entity.user.UserAccount;
import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.UserAccountRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService {
    private ModelMapper mapper = new ModelMapper();
    
    @Autowired
	UserRepository userRepository;

    @Autowired
    UserAccountRepository userAccountRepository;
    
	@Override
	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDTO> userDTOs = users.stream().map(userEntity -> mapper.map(userEntity, UserDTO.class)).toList();
		return userDTOs;
	}

	@Override
	public UserAccountDTO getUserAccount(int id) {
		UserAccount entity = userAccountRepository.findByUser_id(id);		
		UserAccountDTO dto = mapper.map(entity, UserAccountDTO.class);
		return dto;
	}	
	
}
