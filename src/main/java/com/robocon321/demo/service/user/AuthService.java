package com.robocon321.demo.service.user;

import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.dto.user.UserSocialDTO;

public interface AuthService {
	public UserDTO addNewAccountUser(UserAccountDTO userAccountDTO);
	public UserDTO addNewSocialUser(UserSocialDTO userSocialDTO);
	public UserDTO loginSocial(UserSocialDTO userSocialDTO);
}
