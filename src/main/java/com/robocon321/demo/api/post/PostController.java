package com.robocon321.demo.api.post;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.service.post.PostService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

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
	
	@PostMapping("")
	public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) throws URISyntaxException {
		Post newPost = postService.savePost(post);
		return ResponseEntity.created(new URI("/posts/"+newPost.getId())).body(newPost);
	}
	@PutMapping("")
	public ResponseEntity<Post> updatePost(@Valid @RequestBody Post post){
		Post newPost = postService.savePost(post);
		return ResponseEntity.ok().body(newPost);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Post> deletePost(@PathVariable Integer proPostId){
		postService.deletePost(proPostId);
		return ResponseEntity.ok().build();

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
}
