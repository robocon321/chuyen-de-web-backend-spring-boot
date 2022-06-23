package com.robocon321.demo.api.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.common.ContactDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.service.common.ContactService;

@RestController
@RequestMapping("/contacts")
public class ContactController {
	@Autowired
	private ContactService contactService;
	
	@GetMapping("")
	public ResponseEntity getAllByUser(Map<String, String> filter) {
		ResponseObject response = new ResponseObject<>();
		try {
			UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(userDTO == null) {
				response.setMessage("Not found your account");
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			} else {
				filter.put("OR_modifiedUser.id", userDTO.getId() + "");
				List<ContactDTO> contactsDTO = contactService.getAll(filter);
				response.setData(contactsDTO);
				response.setSuccess(true);
				return ResponseEntity.ok(response);
			}
		} catch(Exception e) {
			e.printStackTrace();
			response.setMessage("Not found your account");
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}
}
