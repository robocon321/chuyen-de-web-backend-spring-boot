package com.robocon321.demo.service.user.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robocon321.demo.dto.user.RoleDTO;
import com.robocon321.demo.entity.user.Role;
import com.robocon321.demo.repository.RoleRepository;
import com.robocon321.demo.service.user.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<RoleDTO> getAll() {
		List<Role> roles = roleRepository.findAll();		
		return roles.stream().map(role -> {
			RoleDTO roleDTO = new RoleDTO();
			BeanUtils.copyProperties(role, roleDTO);
			return roleDTO;
		}).collect(Collectors.toList());
	}

}
