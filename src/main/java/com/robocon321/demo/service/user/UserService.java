package com.robocon321.demo.service.user;

import java.util.Optional;

import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.User;

public interface UserService {
	public UserDTO update(UserDTO userDTO);
	public Optional<User> findById(Integer id);
}
