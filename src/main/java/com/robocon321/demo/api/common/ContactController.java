package com.robocon321.demo.api.common;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/byUser")
	public ResponseEntity getAllByUser(@RequestParam Map<String, String> request) {
		ResponseObject response = new ResponseObject<>();
		try {
			UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (userDTO == null) {
				response.setMessage("Not found your account");
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			} else {
				String sort = "";
				if (request.containsKey("sort")) {
					sort = request.get("sort");
					request.remove("sort");
				}

				request.put("OR_modifiedUser.id", userDTO.getId() + "");
				List<ContactDTO> contactsDTO = contactService.getAll(request, sort);
				response.setData(contactsDTO);
				response.setSuccess(true);
				return ResponseEntity.ok(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Not found your account");
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}

	@PostMapping("")
	public ResponseEntity add(@Valid @RequestBody List<ContactDTO> contactDTOs, BindingResult result) {
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
				List<ContactDTO> responseData = contactService.insert(contactDTOs);
				response.setData(responseData);
				response.setMessage("Successful!");
				response.setSuccess(true);
				return ResponseEntity.ok(response);
			} catch (Exception e) {
				e.printStackTrace();
				response.setMessage(e.getMessage());
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}
	}

	@PutMapping("")
	public ResponseEntity update(@Valid @RequestBody List<ContactDTO> contactDTOs, BindingResult result) {
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
				List<ContactDTO> responseData = contactService.update(contactDTOs);
				response.setData(responseData);
				response.setMessage("Successful!");
				response.setSuccess(true);
				return ResponseEntity.ok(response);
			} catch (Exception e) {
				e.printStackTrace();
				response.setMessage(e.getMessage());
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}
	}
}
