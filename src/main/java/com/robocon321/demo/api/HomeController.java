package com.robocon321.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.UserAccountDTO;
import com.robocon321.demo.dto.UserDTO;
import com.robocon321.demo.service.HomeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@Slf4j
public class HomeController {
	@Autowired
	private HomeService homeService;
	
	@GetMapping("/users")
	public ResponseEntity<ResponseObject<List<UserDTO>>> getAllUser() {
		ResponseObject<List<UserDTO>> response = new ResponseObject<List<UserDTO>>(homeService.getAllUsers(), null, false);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<ResponseObject<UserAccountDTO>> getUserAccount(@PathVariable int id) {
		ResponseObject<UserAccountDTO> response = new ResponseObject<UserAccountDTO>(homeService.getUserAccount(id), null, false);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/abc")
	public ResponseEntity<ResponseObject<List<UserDTO>>> post() {
		log.error("Hello world");
		ResponseObject<List<UserDTO>> response = new ResponseObject<List<UserDTO>>(homeService.getAllUsers(), null, false);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
