package com.robocon321.demo.service.user.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robocon321.demo.dto.user.RoleDTO;
import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.dto.user.UserSocialDTO;
import com.robocon321.demo.entity.user.Role;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.entity.user.UserAccount;
import com.robocon321.demo.entity.user.UserRole;
import com.robocon321.demo.entity.user.UserSocial;
import com.robocon321.demo.repository.RoleRepository;
import com.robocon321.demo.repository.UserAccountRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.repository.UserRoleRepository;
import com.robocon321.demo.repository.UserSocialRepository;
import com.robocon321.demo.service.user.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private UserSocialRepository userSocialRepository;


	boolean hasUname(String uname) {
		return userAccountRepository.existsByUsername(uname);
	}

	boolean hasEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	UserAccount addNewUserAccountEntity(String username, String password, User user) {
		UserAccount entity = new UserAccount();
		entity.setUsername(username);
		entity.setPassword(password);
		entity.setUser(user);
		return userAccountRepository.save(entity);
	}

	@Override
	public UserDTO addNewAccountUser(UserAccountDTO userAccountDTO) throws RuntimeException {
		if (hasEmail(userAccountDTO.getUser().getEmail()))
			throw new RuntimeException("Email already registered");
		if (hasUname(userAccountDTO.getUsername()))
			throw new RuntimeException("Username already registered");

		User user = new User();
		user.setEmail(userAccountDTO.getUser().getEmail());
		userRepository.save(user);

		if (user.getId() == null)
			throw new RuntimeException("Cannot save your user");

		Role role = roleRepository.findOneByName("CLIENT");
		if (role.getId() == null)
			throw new RuntimeException("Cannot find your role");

		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		userRoleRepository.save(userRole);
		if (userRole.getId() == null)
			throw new RuntimeException("Cannot save your user role");

		UserAccount userAccount = addNewUserAccountEntity(userAccountDTO.getUsername(), userAccountDTO.getPassword(),
				user);
		if (userAccount.getId() == null)
			throw new RuntimeException("Cannot save your user account");

		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(user, userDTO);

		return userDTO;
	}

	@Override
	public UserDTO addNewSocialUser(UserSocialDTO userSocialDTO) throws RuntimeException {
		// check exist user social

		if (userSocialRepository.existsByKeyAndType(userSocialDTO.getKey(), userSocialDTO.getType()))
			throw new RuntimeException("Your email already exist");
		Optional<User> userOptional = userRepository.findFirstByEmail(userSocialDTO.getUser().getEmail());

		// Save or Update user if exist email in database

		User user = null;
		if (userSocialDTO.getType() == 1) {
			if (userOptional.isPresent()) {
				user = userOptional.get();

				user.setAvatar(userSocialDTO.getUser().getAvatar());
				user.setEmail(userSocialDTO.getUser().getEmail());
				user.setFullname(userSocialDTO.getUser().getFullname());
				user.setPhone(userSocialDTO.getUser().getPhone());
			} else {
				BeanUtils.copyProperties(userSocialDTO.getUser(), user);
			}
		} else {
			throw new RuntimeException("Not support this social account");
		}

		userRepository.save(user);
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(user, userDTO);

		if (user.getId() == null)
			throw new RuntimeException("Cannot save your user");

		// check exists role

		Role role = roleRepository.findOneByName("CLIENT");
		if (role.getId() == null)
			throw new RuntimeException("Cannot find your role");

		// check exists user role

		Optional<UserRole> userRoleOptional = userRoleRepository.findOneByUserIdAndRoleId(user.getId(), role.getId());
		UserRole userRole = new UserRole();

		if (userRoleOptional.isPresent()) {
			userRole = userRoleOptional.get();
		} else {
			// save user role

			userRole.setUser(user);
			userRole.setRole(role);
			userRoleRepository.save(userRole);
			if (userRole.getId() == null)
				throw new RuntimeException("Cannot save your user role");
		}

		// save user social

		UserSocial userSocial = new UserSocial();
		BeanUtils.copyProperties(userSocialDTO, userSocial);
		userSocial.setUser(user);
		userSocialRepository.save(userSocial);

		if (userSocial.getId() == null)
			throw new RuntimeException("Can not save your user social");

		return userDTO;
	}

	@Override
	public UserDTO loginSocial(UserSocialDTO userSocialDTO) throws RuntimeException {
		Optional<UserSocial> userSocialOptional = userSocialRepository
				.findFirstByKeyAndTypeAndUid(userSocialDTO.getKey(), userSocialDTO.getType(), userSocialDTO.getUid());
		if (userSocialOptional.isPresent()) {
			User user = userSocialOptional.get().getUser();
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(user, userDTO);
			
			user.getRoles().forEach(role -> {
				RoleDTO roleDTO = new RoleDTO();
				BeanUtils.copyProperties(role, roleDTO);
				userDTO.getRoles().add(roleDTO);
			});			

			return userDTO;
		} else
			throw new RuntimeException("Your social account not exists");
	}

}
