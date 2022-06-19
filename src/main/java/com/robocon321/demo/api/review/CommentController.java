package com.robocon321.demo.api.review;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.review.CommentDTO;
import com.robocon321.demo.service.review.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	@GetMapping("")
	public ResponseEntity getAll(@RequestParam Map<String, String> request) {
		ResponseObject response = new ResponseObject<>();
				
		response.setData(commentService.getAll(request));
		response.setMessage("Successful!");
		response.setSuccess(true);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("")
	public ResponseEntity add(@RequestBody CommentDTO commentDTO) {
		ResponseObject response = new ResponseObject<>();
		response.setData(commentService.add(commentDTO));
		return ResponseEntity.ok(response);
	}
}
