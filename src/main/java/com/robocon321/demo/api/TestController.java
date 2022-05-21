package com.robocon321.demo.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObjectDomain;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.repository.UserRepository;

@RestController("/test")
public class TestController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@PostMapping("/user")
	public ResponseEntity a(@RequestBody String pathname) {
		pathname = pathname.replace("=", "");
		String[] paths = pathname.split("%2F");
		for(String path : paths) {
			System.out.println(path);
		}
		return null;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity b(@PathVariable Integer id) {
		ResponseObjectDomain obj = new ResponseObjectDomain<>();
		try {
			userRepository.deleteById(id);
			obj.setSuccess(true);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(obj);
	}
}
	