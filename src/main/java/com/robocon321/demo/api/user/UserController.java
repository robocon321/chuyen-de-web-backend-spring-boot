package com.robocon321.demo.api.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.service.user.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PutMapping("")
	public ResponseEntity update(@Valid @RequestBody UserDTO dto, BindingResult result) {
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
			if(dto.getId() == null) {
				response.setMessage("Id not null");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);				
			}
			
						
			try {
				UserDTO userDTO = userService.update(dto);
				if(userDTO == null) {
					response.setMessage("Not found user with id: " + dto.getId());
					response.setSuccess(false);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
				} else {
					response.setData(userService.update(dto));
					response.setSuccess(true);					
				}
				return ResponseEntity.ok(response);
			} catch (Exception e) {
				response.setSuccess(false);
				response.setMessage(e.getMessage());
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}
	}
}
