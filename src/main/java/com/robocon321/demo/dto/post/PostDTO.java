package com.robocon321.demo.dto.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.robocon321.demo.dto.review.CommentDTO;
import com.robocon321.demo.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
	private Integer id;
	private String title;
	private String content;
	private String description;
	private String thumbnail;
	private String galleryImage;
	private String type;
	private PostDTO parentPost;
	private String slug;	
	private String metaTitle;	
	private String metaDescription;
	private Integer status;
	private UserDTO modifiedUser;
	private Date modifiedTime;
	private Double averageRating;
	private Integer totalComment;	
	private List<PostDTO> posts = new ArrayList<>();
	private List<CommentDTO> comments = new ArrayList<>();
}
