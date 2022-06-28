package com.robocon321.demo.dto.common;

import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
	private Integer id;
	private PostDTO post;
	private UserDTO user;

}
