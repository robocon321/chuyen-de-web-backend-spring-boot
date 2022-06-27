package com.robocon321.demo.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.repository.FavoriteRepository;

@RestController
@RequestMapping("/test")
public class TestController {	
	@Autowired
	private FavoriteRepository repository;
	
	@GetMapping("")
	public ResponseEntity get() {
		return ResponseEntity.ok(repository.findAll());
	}
}