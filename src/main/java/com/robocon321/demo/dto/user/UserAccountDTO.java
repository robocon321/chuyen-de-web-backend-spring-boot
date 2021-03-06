package com.robocon321.demo.dto.user;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDTO {
	private Integer id;
	
	@NotBlank(message = "Username not null")
	private String username;
	
	@NotBlank(message = "Password not null")
	private String password;
	
	private UserDTO user;
}
