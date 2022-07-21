package com.robocon321.demo.service.user;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.robocon321.demo.dto.user.UserDTO;

public interface UserService {
	public UserDTO update(UserDTO userDTO);
	public Page<UserDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter);
	public void delete(List<Integer> ids);

}
