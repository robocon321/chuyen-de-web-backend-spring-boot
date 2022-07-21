package com.robocon321.demo.api;


import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.taxomony.TaxomonyDTO;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.repository.TaxomonyRepository;
import com.robocon321.demo.service.post.PostService;

@RestController
@RequestMapping("/test")	
public class TestController {	
	@Autowired
	private PostService postService;
	
	@Autowired
	private TaxomonyRepository taxomonyRepository;
	
	@GetMapping("")
	public ResponseEntity get() {
		PostDTO postDTO = new PostDTO();
		postDTO.setId(200);
		postDTO.setTitle("Title");
		postDTO.setContent("Content");
		postDTO.setDescription("Description");
		postDTO.setThumbnail("This is thumbnail");
		postDTO.setGalleryImage("This is gallery");
		postDTO.setSlug("title");
		postDTO.setType("post");
		postDTO.setModifiedTime(new Date());
		postDTO.setStatus(1);
		

		TaxomonyDTO t1 = new TaxomonyDTO();
		t1.setId(57);
		TaxomonyDTO t2 = new TaxomonyDTO();
		t2.setId(58);
		
		postDTO.setTaxomonies(List.of(t1, t2));
				

		return ResponseEntity.ok(postService.update(List.of(postDTO)));
	}
}