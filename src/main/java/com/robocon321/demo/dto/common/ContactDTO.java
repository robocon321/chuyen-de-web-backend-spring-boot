package com.robocon321.demo.dto.common;

import java.util.Date;

import com.robocon321.demo.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
	private Integer id;
	private String fullname;
	private String phone;
	private int province;
	private int district;
	private int ward;
	private String detailAddress;
	private int priority;
	private int status;
	private UserDTO modifiedUser;
	private Date modifiedTime;
}
