package com.robocon321.demo.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.robocon321.demo.domain.AccountRegisterDomain;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.user.Role;
import com.robocon321.demo.entity.user.UserAccount;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.RoleRepository;
import com.robocon321.demo.repository.UserAccountRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.AuthService;

public class AuthServiceImpl implements AuthService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private RoleRepository roleRepository;
		
	private ModelMapper mapper = new ModelMapper();
	
	boolean hasUname(String uname) {
		return userAccountRepository.existsByUname(uname);
	}
	
	boolean hasEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	User addNewUserEntity(String email) {
		User entity = new User();
		entity.setEmail(email);
		return userRepository.save(entity);
	}
	
	UserAccount addNewUserAccountEntity(String username, String password, User user) {
		UserAccount entity = new UserAccount();
		entity.setUname(username);
		entity.setPwd(password);
		entity.setUser(user);
		return userAccountRepository.save(entity);
	}

	@Override
	public UserDTO addNewAccountUser(AccountRegisterDomain domain) throws RuntimeException {
		if(hasEmail(domain.getEmail())) throw new RuntimeException("Email already registered");
		if(hasUname(domain.getUsername())) throw new RuntimeException("Username already registered");
		
		User user = addNewUserEntity(domain.getEmail());
		if(user.getId() == 0) throw new RuntimeException("Cannot save your user");
		
		UserAccount userAccountEntity = addNewUserAccountEntity(domain.getUsername(), domain.getPassword(), user);
		if(userAccountEntity.getId() == 0) throw new RuntimeException("Cannot save your user account");
		
		Role roleEntity = roleRepository.findOneByName("CLIENT");
		if(roleEntity == null) throw new RuntimeException("Cannot save your role");
		
		user.setRoles(List.of(roleEntity));
		user = userRepository.save(user);
		if(user.getId() == 0) throw new RuntimeException("Cannot save User role");
		
		return mapper.map(user, UserDTO.class);
	}

	@Override
	public UserDTO addNewSocialUser() {
		// TODO Auto-generated method stub
		return null;
	}

}
