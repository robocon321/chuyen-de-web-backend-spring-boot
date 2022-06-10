package com.robocon321.demo.api;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.CustomSpecification;
import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.type.FilterOperate;

@RestController
@RequestMapping("/test")
public class TestController {	
	@Autowired
	private PostRepository postRepository;
	
	@GetMapping("")
	public ResponseEntity b() {
		
		ResponseObject obj = new ResponseObject<>();
		try {
			CustomSpecification spec1 = new CustomSpecification(new FilterCriteria("totalComment", FilterOperate.GREATER, 2));
			obj.setData(postRepository.findAll(Specification.where(spec1)));
			obj.setSuccess(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(obj);
	}
}