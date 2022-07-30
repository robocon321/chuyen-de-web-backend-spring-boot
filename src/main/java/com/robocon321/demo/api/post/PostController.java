package com.robocon321.demo.api.post;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.service.post.PostService;


@RestController
@RequestMapping("/posts")
public class PostController {
	@Autowired
	private PostService postService;

	@GetMapping("")
	public ResponseEntity get(@RequestParam Map<String, String> request) {
		String search = "";
		String sort = "";
		Integer page = 0;
		Integer size = 10;
		ResponseObject response = new ResponseObject<>();

		if (request.containsKey("search")) {
			search = request.get("search");
			request.remove("search");
		}
		if (request.containsKey("sort")) {
			sort = request.get("sort");
			request.remove("sort");
		}

		try {
			if (request.containsKey("size")) {
				size = Integer.parseInt(request.get("size"));
				request.remove("size");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);			
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}

		try {

			if (request.containsKey("page")) {
				page = Integer.parseInt(request.get("page")) - 1;
				if (page < 0)
					page = 0;

				request.remove("page");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}

		Page<PostDTO> pageResponse = postService.getPage(search, size, page, sort, request);
		response.setData(pageResponse);
		response.setMessage("Successful!");
		response.setSuccess(true);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/ids")
	public ResponseEntity get(@RequestParam List<Integer> ids) {
		ResponseObject response = new ResponseObject<>();
		
		try {
			response.setMessage("Successful!");
			response.setSuccess(true);
			response.setData(postService.getPostByIds(ids));
			return ResponseEntity.ok(response);
		} catch(Exception ex) {
			ex.printStackTrace();
			response.setMessage("Your ids are invalid");
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}
	
	@DeleteMapping("")
	public ResponseEntity delete(@RequestBody List<Integer> ids) {
		ResponseObject response = new ResponseObject<>();
		try {
			postService.delete(ids);
			response.setMessage("Successful!");
			response.setSuccess(true);
			return ResponseEntity.ok(response);
		} catch(Exception ex) {
			response.setMessage(ex.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);			
		}
	}
	
	@GetMapping("/{slug}")
	public ResponseEntity get(@PathVariable String slug) {
		ResponseObject response = new ResponseObject<>();
		try {
			response.setData(postService.getDetailPostBySlug(slug));
			response.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("")
	public ResponseEntity post(@RequestBody @Valid List<PostDTO> posts, BindingResult result) {
		ResponseObject response = new ResponseObject<>();
		if (result.hasErrors()) {
			String message = "";
			for (ObjectError error : result.getAllErrors()) {
				message += error.getDefaultMessage() + ". ";
			}
			response.setMessage(message.trim());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} else {
			try {
				return ResponseEntity.ok(postService.save(posts));
			} catch (Exception ex) {
				response.setSuccess(false);
				response.setMessage(ex.getMessage());
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}
	}

	@PutMapping("")
	public ResponseEntity put(@RequestBody @Valid List<PostDTO> posts, BindingResult result) {
		ResponseObject response = new ResponseObject<>();
		if (result.hasErrors()) {
			String message = "";
			for (ObjectError error : result.getAllErrors()) {
				message += error.getDefaultMessage() + ". ";
			}
			response.setMessage(message.trim());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} else {
			try {
				return ResponseEntity.ok(postService.save(posts));
			} catch (Exception ex) {
				response.setSuccess(false);
				response.setMessage(ex.getMessage());
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}
	}
}
