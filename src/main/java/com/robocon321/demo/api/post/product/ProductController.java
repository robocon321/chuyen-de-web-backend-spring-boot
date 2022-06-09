package com.robocon321.demo.api.post.product;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObjectDomain;
import com.robocon321.demo.dto.post.product.ProductDTO;
import com.robocon321.demo.service.post.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@GetMapping("")
	public ResponseEntity get(@RequestParam Map<String, String> request) {
		String search = "";
		String sort = "";
		Integer page = 0;
		Integer size = 10;
		ResponseObjectDomain response = new ResponseObjectDomain<>();
		
		if(request.containsKey("search")) {
			search = request.get("search");
			request.remove("search");
		}
		if(request.containsKey("sort")) {
			sort = request.get("sort");
			request.remove("sort");
		}
		
		try {
			if(request.containsKey("size")) {
				size  = Integer.parseInt(request.get("size"));
				request.remove("size");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if(request.containsKey("size")) {
				page  = Integer.parseInt(request.get("page"));
				request.remove("page");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		Page<ProductDTO> pageResponse = productService.getPage(search, size, page, sort, request);
		response.setData(pageResponse);
		response.setMessage("Successful!");
		response.setSuccess(true);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
