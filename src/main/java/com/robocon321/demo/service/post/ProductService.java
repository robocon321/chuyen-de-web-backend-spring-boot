package com.robocon321.demo.service.post;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.robocon321.demo.dto.post.product.ProductDTO;

public interface ProductService {
	public Page<ProductDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter);
}
