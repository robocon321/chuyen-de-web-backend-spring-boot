package com.robocon321.demo.dto.user;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialDTO {	
	private Integer id;
	
	@NotBlank(message = "Key not null")
	private String key;
	
	@Min(value = 0, message = "type not null")
	private Integer type;
	
	@NotBlank(message = "UID not null")
	private String uid;
	
	private UserDTO user;
}
