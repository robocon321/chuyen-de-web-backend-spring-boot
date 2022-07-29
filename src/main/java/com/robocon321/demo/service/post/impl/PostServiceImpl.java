package com.robocon321.demo.service.post.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.robocon321.demo.dto.taxomony.TaxomonyDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.post.Post;

import com.robocon321.demo.entity.post.product.Product;

import com.robocon321.demo.entity.review.Comment;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.repository.TaxomonyRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.post.PostService;
import com.robocon321.demo.specs.PostSpecification;
import com.robocon321.demo.type.FilterOperate;	

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaxomonyRepository taxomonyRepository;

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
		return posts.stream().map(post -> convertEntityToDTO(post)).collect(Collectors.toList());
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
	public List<PostDTO> save(List<PostDTO> postDTOs) throws RuntimeException {
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) throw new RuntimeException("Your session is not exists");
		UserDTO userDTO = null;
		try {
			userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		} catch(Exception ex) {
			throw new RuntimeException("Your session is invalid");
		}
		
		Optional<User> userOption = userRepository.findById(userDTO.getId());
		if(userOption.isEmpty()) throw new RuntimeException("Your user not found");
		User user = userOption.get();
		List<Post> posts = new ArrayList<>();
		postDTOs.stream().forEach(postDTO -> {
			Post post = new Post();
			BeanUtils.copyProperties(postDTO, post);
			post.setStatus(1);
			post.setModifiedUser(user);
			post.setModifiedTime(new Date());

			Post postSaved = postRepository.save(post);
			posts.add(postSaved);
			
			List<Taxomony> taxomonies = new ArrayList<>();
			
			postDTO.getTaxomonies().stream().forEach(taxomonyDTO -> {
				Optional<Taxomony> taxomonyOpt = taxomonyRepository.findById(taxomonyDTO.getId());
				if(taxomonyOpt.isEmpty()) throw new RuntimeException("Your taxomony not found");
				else {
					Taxomony taxomony = taxomonyRepository.findById(taxomonyDTO.getId()).get();
					taxomony.getObjects().add(postSaved);
				}
			});

			taxomonyRepository.saveAll(taxomonies);
		});
				
		return convertListEntityToDTO(posts);
	}

	@Override
	public PostDTO getDetailPostBySlug(String slug) {
		PostDTO postDTO = new PostDTO();
		Post post = postRepository.findOneBySlug(slug);
		if(post == null) return null;
		BeanUtils.copyProperties(post, postDTO);		

    	UserDTO userDTO = new UserDTO();
    	if(post.getModifiedUser() != null) {
    		BeanUtils.copyProperties(post.getModifiedUser(), userDTO);
        	postDTO.setModifiedUser(userDTO);
    	}
		
    	List<TaxomonyDTO> taxomonyDTOs = post.getTaxomonies().stream().map(taxomony -> {
    		TaxomonyDTO taxomonyDTO = new TaxomonyDTO();
    		BeanUtils.copyProperties(taxomony, taxomonyDTO);
    		return taxomonyDTO;
    	}).toList();
    	
    	postDTO.setTaxomonies(taxomonyDTOs);
    		
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

	@Override
	public List<PostDTO> update(List<PostDTO> postDTOs) throws RuntimeException {
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) throw new RuntimeException("Your session is not exists");
		UserDTO userDTO = null;
		try {
			userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		} catch(Exception ex) {
			throw new RuntimeException("Your session is invalid");
		}
		
		Optional<User> userOption = userRepository.findById(userDTO.getId());
		if(userOption.isEmpty()) throw new RuntimeException("Your user not found");
		User user = userOption.get();
		List<Post> posts = new ArrayList<>();
		postDTOs.stream().forEach(postDTO -> {
			// delete old relationship
			Post postOld = postRepository.findById(postDTO.getId()).get();
			List<Taxomony> taxomonyOlds = postOld.getTaxomonies();
			taxomonyOlds.forEach(taxomony -> {
				taxomony.getObjects().remove(postOld);
			});
			
			// update new relationship
			Post post = new Post();
			BeanUtils.copyProperties(postDTO, post);
			post.setStatus(1);
			post.setModifiedUser(user);
			post.setModifiedTime(new Date());
			
			List<Taxomony> taxomonies = new ArrayList<>();
			Post postSaved = postRepository.save(post);
			posts.add(postSaved);
			
			postDTO.getTaxomonies().stream().forEach(taxomonyDTO -> {
				Optional<Taxomony> taxomonyOpt = taxomonyRepository.findById(taxomonyDTO.getId());
				if(taxomonyOpt.isEmpty()) throw new RuntimeException("Your taxomony not found");
				else {
					Taxomony taxomony = taxomonyRepository.findById(taxomonyDTO.getId()).get();

					taxomony.getObjects().add(postSaved);
				}
			});
			
			taxomonyRepository.saveAll(taxomonies);
		});
				
		return convertListEntityToDTO(posts);
	}
	
}
