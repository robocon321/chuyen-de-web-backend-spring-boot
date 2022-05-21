package com.robocon321.demo.dto.user;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
	private Integer id;
	@NotBlank(message = "Name is not null")
	private String name;
	private List<UserDTO> users = new ArrayList<UserDTO>();
}
