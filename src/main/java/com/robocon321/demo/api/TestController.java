package com.robocon321.demo.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.entity.user.UserSocial;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.repository.UserSocialRepository;

@RestController("/test")
public class TestController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserSocialRepository userSocialRepository;


	@GetMapping("/user")
	public ResponseEntity a() {
		User user = userRepository.findById(1).get();
		UserSocial userSocial = new UserSocial(null, "This is key", 1, "kldKoekLKiie", user);
		userSocialRepository.save(userSocial);
		return ResponseEntity.ok().body(userSocial);
	}
}
	