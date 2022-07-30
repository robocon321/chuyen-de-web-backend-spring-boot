package com.robocon321.demo.service.post;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.robocon321.demo.dto.post.product.ProductDTO;
import com.robocon321.demo.entity.post.product.Product;

public interface ProductService {
	public Page<ProductDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter);
	public Product saveProduct(Product product);
	public void deleteProduct(Integer productId);
	public Product findBySlug(String slug);
	//product DTO
	public ProductDTO add(ProductDTO product);	
}
