package com.robocon321.demo.dto.user;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDTO {
	private Integer id;
	private UserDTO user;	
	private RoleDTO role;
}
