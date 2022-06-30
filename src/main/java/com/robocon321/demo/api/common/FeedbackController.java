package com.robocon321.demo.api.common;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.common.FeedbackDTO;
import com.robocon321.demo.service.common.FeedbackService;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {
	@Autowired
	private FeedbackService feedbackService;

	@PostMapping("")
	public ResponseEntity add(@Valid @RequestBody List<FeedbackDTO> dtos, BindingResult result) {
		ResponseObject response = new ResponseObject<>();
		if (result.hasErrors()) {
			String message = "";
			for (ObjectError error : result.getAllErrors()) {
				message += error.getDefaultMessage() + ". ";
			}
			response.setMessage(message.trim());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} else {
			try {
				List<FeedbackDTO> data = feedbackService.insert(dtos);
				response.setData(data);
				response.setSuccess(true);
				return ResponseEntity.ok(response);
			} catch (Exception ex) {
				response.setMessage(ex.getMessage());
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}
	}
}
