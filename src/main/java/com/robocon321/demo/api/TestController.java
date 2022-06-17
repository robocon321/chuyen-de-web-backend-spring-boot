package com.robocon321.demo.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.service.post.PostService;

@RestController
@RequestMapping("/test")
public class TestController {	
	@Autowired
	private PostService postRepository;
	
	@GetMapping("/{slug}")
	public ResponseEntity b(@PathVariable String slug) {
		
		ResponseObject obj = new ResponseObject<>();
		try {
			obj.setData(postRepository.getDetailPostBySlug(slug));
			obj.setSuccess(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(obj);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity a(@PathVariable Integer id) {
		ResponseObject obj = new ResponseObject<>();
		try {
//			postRepository.deleteById(id);
			obj.setSuccess(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(obj);
	}
}