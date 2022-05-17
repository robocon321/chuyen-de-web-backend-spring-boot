package com.robocon321.demo.dto.review;

import java.util.Date;

import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {
	private Integer id;
	private PostDTO post;
	private Integer star;
	private String content;
	private Integer status;
	private UserDTO modifiedUser;
	private Date modifiedTime;	
}
