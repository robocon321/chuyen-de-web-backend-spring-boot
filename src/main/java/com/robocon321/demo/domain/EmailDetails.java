package com.robocon321.demo.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//Importing required classes
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

//Class
public class EmailDetails {

	@NotNull
	private String sendTo;
	@NotBlank
	private String subject;
	@NotBlank
	private String content;
}