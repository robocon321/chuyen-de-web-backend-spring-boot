package com.robocon321.demo.service.post.impl;

import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.service.post.PostService;	

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepository postRepository;

	@Override
	public Page<PostDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter) {
		String arrSort[] = sort.split("__");
		String sortName;
		String sortType;
		if(arrSort.length == 2) {
			sortName = arrSort[0];
			sortType = arrSort[1];						
		} else {
			sortName = "id";
			sortType = "ASC";
		}
		
		Page<Post> pageResponse = postRepository.findByTitleContaining(search, PageRequest.of(page, size, sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending()));
		return convertPageSummary(pageResponse);
	}

	private Page<PostDTO> convertPageSummary(Page<Post> page) {
		return page.map(new Function<Post, PostDTO>() {
		    @Override
		    public PostDTO apply(Post post) {
		    	PostDTO postDTO = new PostDTO();
		    	BeanUtils.copyProperties(post, postDTO);
		    	
		    	UserDTO userDTO = new UserDTO();
		    	BeanUtils.copyProperties(post.getModifiedUser(), userDTO);
		    	postDTO.setModifiedUser(userDTO);
		    	
		    	return postDTO;
		    }
		});	
	}

	
}
