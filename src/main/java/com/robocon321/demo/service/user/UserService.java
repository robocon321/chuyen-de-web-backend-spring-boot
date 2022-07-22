package com.robocon321.demo.service.user;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import java.util.Optional;

import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.User;

public interface UserService {
	public Page<UserDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter);
	public void delete(List<Integer> ids);
	public List<UserDTO> save(List<UserDTO> userDTOs);
	public List<UserDTO> update(List<UserDTO> userDTOs);
	public UserDTO findById(Integer id);
}
