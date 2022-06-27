package com.robocon321.demo.dto.common;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.robocon321.demo.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {
	private Integer id;
	@NotBlank
	@NotNull
	private String fullname;
	
	@NotBlank
	@NotNull
	private String phone;

	@NotBlank
	@NotNull
	private String province;

	@NotBlank
	@NotNull
	private String district;

	@NotBlank
	@NotNull
	private String ward;

	@NotBlank
	@NotNull
	private String detailAddress;

	private Integer priority;
	private Integer status;
	private UserDTO modifiedUser;
	private Date modifiedTime;
}
