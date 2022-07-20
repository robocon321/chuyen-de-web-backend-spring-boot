package com.robocon321.demo.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.taxomony.TaxomonyDTO;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.repository.TaxomonyRepository;
import com.robocon321.demo.service.post.PostService;

@RestController
@RequestMapping("/test")	
public class TestController {	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private TaxomonyRepository taxomonyRepository;
	
	@GetMapping("")
	public ResponseEntity get() {
//		PostDTO postDTO = new PostDTO();
//		postDTO.setTitle("Title");
//		postDTO.setContent("Content");
//		postDTO.setDescription("Description");
//		postDTO.setThumbnail("This is thumbnail");
//		postDTO.setGalleryImage("This is gallery");
//		postDTO.setSlug("title");
//		postDTO.setType("post");
//
//		TaxomonyDTO t1 = new TaxomonyDTO();
//		t1.setId(57);
//
//		TaxomonyDTO t2 = new TaxomonyDTO();
//		t2.setId(58);
//		
//		List<TaxomonyDTO> ts = List.of(t1, t2);
//
//		postDTO.setTaxomonies(ts);
//
//		return ResponseEntity.ok(postService.save(List.of(postDTO)));
		return null;
	}
}