package com.robocon321.demo.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long id;
	private String fullname;
	private String email;
	private String phone;
	private String avatar;
	private int status;
	private UserDTO modifiedUser;
	private Date modifiedTime;
}
