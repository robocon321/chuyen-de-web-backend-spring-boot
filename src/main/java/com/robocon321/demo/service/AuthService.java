package com.robocon321.demo.service;

import com.robocon321.demo.domain.AccountRegisterDomain;
import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.dto.user.UserDTO;

public interface AuthService {
	public UserDTO addNewAccountUser(AccountRegisterDomain domain);
	public UserDTO addNewSocialUser();
}
