package com.robocon321.demo.service.post.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.review.CommentDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.post.Post;

import com.robocon321.demo.entity.post.product.Product;

import com.robocon321.demo.entity.review.Comment;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.service.post.PostService;
import com.robocon321.demo.specs.PostSpecification;
import com.robocon321.demo.type.FilterOperate;	

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepository postRepository;

	@Override
	public Page<PostDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter) {
		Specification<Post> spec = PostSpecification.filter(new FilterCriteria("title", FilterOperate.LIKE, search));

		for(Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(int i = 0 ; i < arrValue.length ; i ++) {
					String value = arrValue[i];
					Specification<Post> specType  = PostSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						if(i == 0) spec = spec.and(specType);
						else spec = spec.or(specType);
					}
				}
			} else if(keyEntry.startsWith("BT")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				if(arrValue.length == 2) {
					Specification<Post> specTypeGreater = PostSpecification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Post> specTypeLess = PostSpecification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[1]));
					spec = spec.and(specTypeGreater.and(specTypeLess));
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Post> specType = PostSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}				
			}
		}
		
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
	
		Page<Post> pageResponse = postRepository.findAll(spec, PageRequest.of(page, size, sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending()));
		return convertPageEntityToDTO(pageResponse);
	}

	private Page<PostDTO> convertPageEntityToDTO(Page<Post> page) {
		return page.map(post -> convertEntityToDTO(post));	
	}
	
	private List<PostDTO> convertListEntityToDTO(List<Post> posts) {
		return posts.stream().map(post -> convertEntityToDTO(post)).toList();
	}
	
	private PostDTO convertEntityToDTO(Post post) {
    	PostDTO postDTO = new PostDTO();
    	BeanUtils.copyProperties(post, postDTO);
    	
    	User userMod = post.getModifiedUser();
    	if(userMod != null) {
	    	UserDTO userDTO = new UserDTO();
	    	BeanUtils.copyProperties(userMod, userDTO);
	    	postDTO.setModifiedUser(userDTO);
    	}
    	
    	return postDTO;
	}
	
	
	@Override
	public Post findBySlug(String slug) {
		List<Post> list = postRepository.findAll();
		for(Post post:list) {
			if(post.getSlug().equals(slug)) {
				
				return post;
			}
		}
		return null;
	}

	@Override
	public Post savePost(Post post) {
		// TODO Auto-generated method stub
		return postRepository.save(post);
	}

	@Override
	public PostDTO getDetailPostBySlug(String slug) {
		PostDTO postDTO = new PostDTO();
		Post post = postRepository.findOneBySlug(slug);
		if(post == null) return null;
		BeanUtils.copyProperties(post, postDTO);		

    	UserDTO userDTO = new UserDTO();
    	BeanUtils.copyProperties(post.getModifiedUser(), userDTO);
    	postDTO.setModifiedUser(userDTO);
		
		
		return postDTO;
	}

	@Override
	public List<PostDTO> getAll(String search, String sort, Map<String, String> filter) {
		Specification<Post> spec = PostSpecification.filter(new FilterCriteria("title", FilterOperate.LIKE, search));

		for(Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Post> specType  = PostSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.or(specType);
					}
				}
			} else if(keyEntry.startsWith("BT")) {
				String field = keyEntry.substring(8);
				String[] arrValue = valueEntry.split("%2C");
				if(arrValue.length == 2) {
					Specification<Post> specTypeGreater = PostSpecification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Post> specTypeLess = PostSpecification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[0]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Post> specType = PostSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}				
			}
		}
		
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
	
		List<Post> pageResponse = postRepository.findAll(spec, sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending());
		return convertListEntityToDTO(pageResponse);
	}

	@Override
	public void delete(List<Integer> ids) {
		try {
			if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
				throw new RuntimeException("Your session not found");
			} else {
				UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				postRepository.deleteAllById(ids);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Your session is invalid");
		}		
	}


	
}
