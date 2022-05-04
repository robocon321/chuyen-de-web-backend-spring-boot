package com.robocon321.demo.dto.user;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private int id;
	private String fullname;
	private String email;
	private String phone;
	private String avatar;
	private int status;
	private UserDTO modifiedUserRef;
	private Date modifiedTime;
	private List<RoleDTO> roles;
}
