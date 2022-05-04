package com.robocon321.demo.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterDomain {
	@NotBlank(message = "Username not null")
	@Size(max = 25, min = 5,  message = "Length password must from 5 - 25 letters")
	private String username;

	@NotBlank(message = "Password not null")
	@Size(max = 25, min = 5, message = "Length password must from 5 - 25 letters")
	private String password;
	
	@NotBlank(message = "Email not null")
	@Email(message = "Not email format")
	private String email;
}
