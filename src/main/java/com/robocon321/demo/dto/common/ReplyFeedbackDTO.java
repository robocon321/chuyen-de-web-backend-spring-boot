package com.robocon321.demo.dto.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReplyFeedbackDTO {
	@NotNull
	private Integer feedbackId;
	@NotBlank
	private String subject;
	@NotBlank
	private String content;
}
