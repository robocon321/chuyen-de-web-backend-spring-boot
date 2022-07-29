package com.robocon321.demo.dto.common;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
	private Integer id;
	@NotBlank
	private String fullname;

	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String subject;

	@NotBlank
	private String message;

	private Date modifiedTime;
	private Integer status;
	private UserDTO user;
}
