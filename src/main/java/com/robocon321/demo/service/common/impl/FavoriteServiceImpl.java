package com.robocon321.demo.service.common.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.common.FavoriteDTO;
import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.common.Favorite;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.FavoriteRepository;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.common.FavoriteService;
import com.robocon321.demo.specs.FavoriteSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class FavoriteServiceImpl implements FavoriteService {
	@Autowired
	private FavoriteRepository favoriteRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;

	@Override
	public List<FavoriteDTO> getAll(String sort, Map<String, String> filter) {
		Specification<Favorite> spec = null;

		for(Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Favorite> specType  = FavoriteSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
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
					Specification<Favorite> specTypeGreater = FavoriteSpecification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Favorite> specTypeLess = FavoriteSpecification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[0]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Favorite> specType = FavoriteSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
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
	
		List<Favorite> entities = favoriteRepository.findAll(spec, sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending());
		return convertListEntityToDTO(entities);
	}

	private List<FavoriteDTO> convertListEntityToDTO(List<Favorite> entities) {
		return entities.stream().map(entity -> convertEntityToDTO(entity)).toList();
	}
	
	private FavoriteDTO convertEntityToDTO(Favorite favorite) {
		FavoriteDTO favoriteDTO = new FavoriteDTO();
		
		BeanUtils.copyProperties(favorite, favoriteDTO);
		
		PostDTO postDTO = new PostDTO();
		BeanUtils.copyProperties(favorite.getPost(), postDTO);
		favoriteDTO.setPost(postDTO);
		
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(favorite.getUser(), userDTO);
		favoriteDTO.setUser(userDTO);
		
				
		return favoriteDTO;
	}

	@Override
	public void delete(List<Integer> ids) throws RuntimeException {
		try {
			if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
				throw new RuntimeException("Your session not found");
			} else {
				UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				favoriteRepository.deleteAllById(ids);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Your session is invalid");
		}
	}

	@Override
	public List<FavoriteDTO> add(List<Integer> ids) throws RuntimeException {
		try {
			if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
				throw new RuntimeException("Your session not found");
			} else {
				Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Optional<User> optional = userRepository.findById(userDTO.getId());
				if(optional.isPresent()) {
					List<Favorite> favorites = new ArrayList<>();
					for(Integer id: ids) {
						Optional<Post>  postOpt = postRepository.findById(id);
						if(postOpt.isPresent()) {
							Favorite favorite = new Favorite();
							favorite.setPost(postOpt.get());
							favorite.setUser(optional.get());
							favorites.add(favorite);
						} else {
							throw new RuntimeException("Your post not found");
						}
					}
					favorites = favoriteRepository.saveAll(favorites);
					return convertListEntityToDTO(favorites);
					
				} else throw new RuntimeException("Your session not found");				
			}
		} catch(Exception ex) {
			throw new RuntimeException("Your session is invalid");			
		}		
	}
	
}
