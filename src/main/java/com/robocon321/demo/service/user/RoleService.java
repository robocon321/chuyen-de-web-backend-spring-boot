package com.robocon321.demo.service.user;

import java.util.List;

import com.robocon321.demo.dto.user.RoleDTO;
import com.robocon321.demo.entity.user.Role;

public interface RoleService {
	public List<RoleDTO> getAll();
}
