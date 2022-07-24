package com.robocon321.demo.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.util.Util;

@RestController
@RequestMapping("/test")	
public class TestController {
	@GetMapping("")
	public String get() {		
		return Util.generatePassayPassword();
	}
}