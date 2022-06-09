package com.robocon321.demo.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObjectDomain;
import com.robocon321.demo.repository.ProductRepository;

@RestController
@RequestMapping("/test")
public class TestController {	
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/")
	public ResponseEntity b(String search) {
		
		ResponseObjectDomain obj = new ResponseObjectDomain<>();
		try {
			obj.setData(productRepository.findById(1).get());
			obj.setSuccess(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(obj);
	}
}