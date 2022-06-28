package com.robocon321.demo.api;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.repository.FavoriteRepository;
import com.robocon321.demo.service.common.FavoriteService;
import com.robocon321.demo.service.post.PostService;

@RestController
@RequestMapping("/test")
public class TestController {	
	@Autowired
	private FavoriteService favoriteService;
	
	@GetMapping("")
	public ResponseEntity get(@RequestParam Map<String, String> request) {
		String sort = "";
		ResponseObject response = new ResponseObject<>();

		if (request.containsKey("sort")) {
			sort = request.get("sort");
			request.remove("sort");
		}
		
		return ResponseEntity.ok(favoriteService.getAll(sort, request));
	}
}