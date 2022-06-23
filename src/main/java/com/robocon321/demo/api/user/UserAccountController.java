package com.robocon321.demo.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.service.user.UserAccountService;

@RestController
@RequestMapping("/userAccounts")
public class UserAccountController {
	@Autowired
	private UserAccountService userAccountService;
	
	@PutMapping("")
	public ResponseEntity update(@RequestBody UserAccountDTO dto) {
		ResponseObject response = new ResponseObject<>();
		if(dto.getPassword() == null || dto.getPassword().equals("")) {
			response.setMessage("Invalid password");
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		
		try {
			UserAccountDTO userAccountDTO = userAccountService.update(dto);
			response.setData(userAccountDTO);
			response.setSuccess(true);
			return ResponseEntity.ok(response);
		} catch(Exception e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}		
		
	}
}
