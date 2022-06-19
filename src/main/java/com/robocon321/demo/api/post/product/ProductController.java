package com.robocon321.demo.api.post.product;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObjectDomain;
import com.robocon321.demo.dto.post.product.ProductDTO;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.service.post.ProductService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

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
		Integer size = 9;
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
	
	@GetMapping("/{slug}")
	public ResponseEntity<Product> findBySlug(@PathVariable String slug){
		Product product = productService.findBySlug(slug);
		System.out.println(product);
		return ResponseEntity.ok().body(product);
	}
	
	
	@PostMapping("")
	public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws URISyntaxException {
		Product newProduct = productService.saveProduct(product);
		return ResponseEntity.created(new URI("/products/"+newProduct.getId())).body(newProduct);
	}
	@PutMapping("")
	public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product){
		Product newProduct = productService.saveProduct(product);
		return ResponseEntity.ok().body(newProduct);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable Integer productId){
		productService.deleteProduct(productId);
		return ResponseEntity.ok().build();
	}
}
