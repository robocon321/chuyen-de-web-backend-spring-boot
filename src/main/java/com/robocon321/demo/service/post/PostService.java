package com.robocon321.demo.service.post;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.post.product.ProductDTO;
import com.robocon321.demo.entity.post.Post;

public interface PostService {
	public Page<PostDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter);
	public List<PostDTO> getAll(String search, String sort, Map<String, String> filter);

	public List<PostDTO> save(List<PostDTO> postDTOs);
	public List<PostDTO> update(List<PostDTO> postDTOs);
	public void delete(List<Integer> ids);

	public PostDTO getDetailPostBySlug(String slug);
}
