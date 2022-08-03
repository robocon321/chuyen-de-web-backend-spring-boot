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
import com.robocon321.demo.dto.user.RoleDTO;
import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.Role;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.entity.user.UserAccount;
import com.robocon321.demo.repository.RoleRepository;
import com.robocon321.demo.repository.UserAccountRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.user.UserService;
import com.robocon321.demo.specs.UserSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Override
	public Page<UserDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter) {
		Specification<User> spec = UserSpecification.filter(new FilterCriteria("fullname", FilterOperate.LIKE, search));

		for (Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();

			if (keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for (int i = 0; i < arrValue.length; i++) {
					String value = arrValue[i];
					Specification<User> specType = UserSpecification
							.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if (spec == null) {
						spec = specType;
					} else {
						if (i == 0)
							spec = spec.and(specType);
						else
							spec = spec.or(specType);
					}
				}
			} else if (keyEntry.startsWith("BT")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				if (arrValue.length == 2) {
					Specification<User> specTypeGreater = UserSpecification
							.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<User> specTypeLess = UserSpecification
							.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[1]));
					spec = spec.and(specTypeGreater.and(specTypeLess));
				}
			}

			else if (keyEntry.startsWith("AND")) {
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for (String value : arrValue) {
					Specification<User> specType = UserSpecification
							.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if (spec == null) {
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
		if (arrSort.length == 2) {
			sortName = arrSort[0];
			sortType = arrSort[1];
		} else {
			sortName = "id";
			sortType = "ASC";
		}

		Page<User> pageResponse = userRepository.findAll(spec, PageRequest.of(page, size,
				sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending()));
		return convertPageEntityToDTO(pageResponse);
	}

	@Override
	public void delete(List<Integer> ids) {
		try {
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
				throw new RuntimeException("Your session not found");
			} else {
				UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				userRepository.deleteAllById(ids);
			}
		} catch (Exception ex) {
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
		if (userMod != null) {
			UserDTO userModDTO = new UserDTO();
			BeanUtils.copyProperties(userMod, userModDTO);
			userDTO.setModifiedUser(userModDTO);
		}
		
		
		// set userAccount
		if(user.getUserAccount() != null) {
			UserAccountDTO userAccountDTO = new UserAccountDTO();
			BeanUtils.copyProperties(user.getUserAccount(), userAccountDTO);
			userDTO.setUserAccount(userAccountDTO);			
		}
		
		// set roles
		
		List<RoleDTO> roleDTOs = user.getRoles().stream().map(role -> {
			RoleDTO roleDTO = new RoleDTO();
			BeanUtils.copyProperties(role, roleDTO);
			return roleDTO;
		}).toList();
		
		userDTO.setRoles(roleDTOs);
//		userDTO.getUserAccount().setPassword("");
		
		return userDTO;
	}

	private List<UserDTO> convertListEntityToDTO(List<User> users) {
		return users.stream().map(user -> {
			return convertEntityToDTO(user);
		}).toList();
	}

	@Override
	public List<UserDTO> save(List<UserDTO> userDTOs) throws RuntimeException {
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null)
			throw new RuntimeException("Your session invalid");
		UserDTO userModDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<User> userModOpt = userRepository.findById(userModDTO.getId());
		if (userModOpt.isEmpty())
			throw new RuntimeException("Your user not found");
		User userMod = userModOpt.get();

		List<User> users = userDTOs.stream().map(userDTO -> {
			// check exists username
			if (userAccountRepository.existsByUsername(userDTO.getUserAccount().getUsername()))
				throw new RuntimeException("Your username exists");

			User user = new User();
			BeanUtils.copyProperties(userDTO, user);

			// set role
			List<Role> roles = userDTO.getRoles().stream().map(roleDTO -> {
				Optional<Role> roleOpt = roleRepository.findById(roleDTO.getId());
				if (roleOpt.isEmpty())
					throw new RuntimeException("Not found your role");
				return roleOpt.get();
			}).toList();
			user.setRoles(roles);

			// set user account
			UserAccount userAccount = new UserAccount();
			BeanUtils.copyProperties(userDTO.getUserAccount(), userAccount);
			user.setUserAccount(userAccount);
			user.setModifiedUser(userMod);
			userAccount.setUser(user);

			return user;
		}).toList();

		users = userRepository.saveAll(users);
		return convertListEntityToDTO(users);
	}

	@Override
	public UserDTO findById(Integer id) throws RuntimeException {		
		Optional<User> userOpt = userRepository.findById(id);
		if(userOpt.isEmpty()) throw new RuntimeException("Your user not found");		
		return convertEntityToDTO(userOpt.get());
	}

	@Override
	public List<UserDTO> update(List<UserDTO> userDTOs) throws RuntimeException {
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null)
			throw new RuntimeException("Your session invalid");
		UserDTO userModDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<User> userModOpt = userRepository.findById(userModDTO.getId());
		if (userModOpt.isEmpty())
			throw new RuntimeException("Your user not found");
		User userMod = userModOpt.get();

		List<User> users = userDTOs.stream().map(userDTO -> {
			if(userDTO.getId() == null) throw new RuntimeException("Your update user invalid");
			// check exists username
			if(userDTO.getUserAccount() != null) {
				if (userAccountRepository.existsByUsernameAndUser_IdNot(userDTO.getUserAccount().getUsername(), userDTO.getId()))
					throw new RuntimeException("Your username exists");				
			}

			User user = new User();
			BeanUtils.copyProperties(userDTO, user);

			// set role
			List<Role> roles = userDTO.getRoles().stream().map(roleDTO -> {
				Optional<Role> roleOpt = roleRepository.findById(roleDTO.getId());
				if (roleOpt.isEmpty())
					throw new RuntimeException("Not found your role");
				return roleOpt.get();
			}).toList();
			user.setRoles(roles);

			if(userDTO.getUserAccount() != null) {
				// set user account
				UserAccount userAccount = new UserAccount();
				BeanUtils.copyProperties(userDTO.getUserAccount(), userAccount);
				user.setUserAccount(userAccount);
				userAccount.setUser(user);				
			}

			user.setModifiedUser(userMod);
			return user;
		}).toList();

		users = userRepository.saveAll(users);
		return convertListEntityToDTO(users);
	}

}
