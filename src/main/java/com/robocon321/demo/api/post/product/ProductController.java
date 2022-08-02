package com.robocon321.demo.api.post.product;


import java.util.List;
import java.util.Map;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.robocon321.demo.dto.post.product.ProductDTO;
import com.robocon321.demo.entity.post.product.Product;
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
// 		Integer size = 9;
// 		ResponseObjectDomain response = new ResponseObjectDomain<>();
    
		Integer size = 10;
		ResponseObject response = new ResponseObject<>();

		
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
			if(request.containsKey("page")) {
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
	
	@GetMapping("/getAll")
	public ResponseEntity getAll(@RequestParam Map<String, String> request) {
		ResponseObject response = new ResponseObject<>();		
		response.setData(productService.getAll(request));
		response.setMessage("Successful!");
		response.setSuccess(true);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/{slug}")
	public ResponseEntity<Product> findBySlug(@PathVariable String slug){
		Product product = productService.findBySlug(slug);
		System.out.println(product);
		return ResponseEntity.ok().body(product);
	}
	
	
	@PostMapping("")
	public ResponseEntity createProduct(@Valid @RequestBody List<ProductDTO> products){
		return ResponseEntity.ok(productService.saveProduct(products));
	}
	@PutMapping("")
	public ResponseEntity put(@Valid @RequestBody List<ProductDTO> products) {
		return ResponseEntity.ok(productService.saveProduct(products));
	}
	@DeleteMapping("")
	public ResponseEntity deleteProduct(@Valid @RequestBody List<Integer> ids){
		ResponseObject response = new ResponseObject<>();
		try {
			productService.deleteProduct(ids);
			response.setMessage("Successful!");
			response.setSuccess(true);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);	
		}
		
	}
}
