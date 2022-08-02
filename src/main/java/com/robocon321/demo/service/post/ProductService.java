package com.robocon321.demo.service.post;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.robocon321.demo.dto.post.product.ProductDTO;
import com.robocon321.demo.entity.post.product.Product;

public interface ProductService {
	public Page<ProductDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter);
	public List<ProductDTO> saveProduct(List<ProductDTO> productDTO);
	public void deleteProduct(List<Integer> ids);
	public Product findBySlug(String slug);
	public List<ProductDTO> getAll(Map<String, String> request);
	public List<ProductDTO> update(List<ProductDTO> productDTOs);
	//product DTO

}
