package com.robocon321.demo.dto.user;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Integer id;
	private String fullname;
	@Email(message = "Not email format")
	private String email;
	private String phone;
	@URL(message = "Not url format")
	private String avatar;
	private Boolean sex;
	private Date birthday;
	private Integer status;
	private UserDTO modifiedUser;
	private Date modifiedTime;
	private List<RoleDTO> roles = new ArrayList<>();
}
