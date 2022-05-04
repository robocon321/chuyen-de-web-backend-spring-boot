package com.robocon321.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.repository.UserRepository;

public interface HomeService {
	public List<UserDTO> getAllUsers();
	public UserAccountDTO getUserAccount(int id);
}
