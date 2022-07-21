package com.robocon321.demo.service.user.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.user.UserService;
import com.robocon321.demo.specs.UserSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDTO update(UserDTO userDTO) throws RuntimeException {
		Optional<User> opt = userRepository.findById(userDTO.getId());
		if(opt.isPresent()) {
			User user = opt.get();
			BeanUtils.copyProperties(userDTO, user);
			
			try {
				if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
					UserDTO userModDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Optional<User> optModDTO = userRepository.findById(userModDTO.getId());
					if(optModDTO.isPresent()) {
						user.setModifiedUser(optModDTO.get());
					}			
				}				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Your session not valid");
			}
			
			user = userRepository.save(user);
			BeanUtils.copyProperties(user, userDTO);
			return userDTO;	
		} else {
			throw new RuntimeException("Your account not found");
		}
	}

	@Override
	public Optional<User> findById(Integer id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}
	@Override
public Page<UserDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter) {
		Specification<User> spec = UserSpecification.filter(new FilterCriteria("fullname", FilterOperate.LIKE, search));

		for(Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(int i = 0 ; i < arrValue.length ; i ++) {
					String value = arrValue[i];
					Specification<User> specType  = UserSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
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
					Specification<User> specTypeGreater = UserSpecification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<User> specTypeLess = UserSpecification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[1]));
					spec = spec.and(specTypeGreater.and(specTypeLess));
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<User> specType = UserSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
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
	
		Page<User> pageResponse = userRepository.findAll(spec, PageRequest.of(page, size, sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending()));
		return convertPageEntityToDTO(pageResponse);
	}

	@Override
	public void delete(List<Integer> ids) {
		try {
			if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
				throw new RuntimeException("Your session not found");
			} else {
				UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				userRepository.deleteAllById(ids);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Your session is invalid");
		}		
	}

	private Page<UserDTO> convertPageEntityToDTO(Page<User> page) {
		return page.map(user -> convertEntityToDTO(user));	
	}
	
	private UserDTO convertEntityToDTO(User user) {
    	UserDTO userDTO = new UserDTO();
    	BeanUtils.copyProperties(user, userDTO);
    	
    	User userMod = user.getModifiedUser();
    	if(userMod != null) {
	    	UserDTO userModDTO = new UserDTO();
	    	BeanUtils.copyProperties(userMod, userModDTO);
	    	userDTO.setModifiedUser(userModDTO);
    	}
    	
    	return userDTO;
	}
	
}
