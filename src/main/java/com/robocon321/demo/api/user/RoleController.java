package com.robocon321.demo.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.service.user.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@GetMapping("")
	public ResponseEntity get() {
		ResponseObject response = new ResponseObject<>();
		try {
			response.setData(roleService.getAll());
			response.setSuccess(true);
			response.setMessage("Successful!");
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setMessage("Load role fail");
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		
	}
}
