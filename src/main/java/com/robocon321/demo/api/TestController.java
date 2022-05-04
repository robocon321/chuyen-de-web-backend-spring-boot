package com.robocon321.demo.api;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObjectDomain;
import com.robocon321.demo.dto.user.RoleDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.UserRepository;

@RestController("/test")
public class TestController {
	@Autowired
	private UserRepository userRepository;
	
	private ModelMapper mapper = new ModelMapper();
	
	@GetMapping("/user/{id}")
	public ResponseEntity<ResponseObjectDomain> a(@PathVariable int id) {
		ResponseObjectDomain response = new ResponseObjectDomain<>();
		Optional<User> opt = userRepository.findById(id);
//		UserDTO dto = new UserDTO();
		UserDTO dto = mapper.map(opt.get(), UserDTO.class);
//		BeanUtils.copyProperties(opt.get(), dto);
		response.setData(dto);
		return ResponseEntity.ok().body(response);
	}
}
