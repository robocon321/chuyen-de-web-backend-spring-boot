package com.robocon321.demo.api;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObjectDomain;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.repository.ProductRepository;

@RestController
@RequestMapping("/test")
public class TestController {	
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/")
	public ResponseEntity b(@RequestParam(name = "filter[*]") Map<String, String> filter) {
//		String[] arrSort = sort.split("__");
		
		ResponseObjectDomain obj = new ResponseObjectDomain<>();
		try {
//			Page<Product> page = productRepository.findAll(PageRequest.of(0, 10, Sort.by(arrSort[1].equals("ASC") ? Direction.ASC : Direction.DESC, arrSort[0])));
//			obj.setData(page);
			obj.setData(filter);
			obj.setSuccess(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(obj);
	}
}