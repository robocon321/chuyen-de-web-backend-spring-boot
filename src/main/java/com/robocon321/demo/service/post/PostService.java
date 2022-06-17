package com.robocon321.demo.service.post;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.post.product.ProductDTO;

public interface PostService {
	public Page<PostDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter);
	public PostDTO getDetailPostBySlug(String slug);
}
