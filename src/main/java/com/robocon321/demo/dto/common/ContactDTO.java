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
	private Integer province;
	private Integer district;
	private Integer ward;
	private String detailAddress;
	private Integer priority;
	private Integer status;
	private UserDTO modifiedUser;
	private Date modifiedTime;
}
