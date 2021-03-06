package com.robocon321.demo.dto.review;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
	private Integer id;
	private String content;
	private CommentDTO parent;
	private PostDTO post;
	private Integer status;
	private UserDTO modifiedUser;
	private Date modifiedTime;
	private List<CommentDTO> comments = new ArrayList<CommentDTO>();
}
