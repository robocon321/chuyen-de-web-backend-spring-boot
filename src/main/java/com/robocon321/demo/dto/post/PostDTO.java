package com.robocon321.demo.dto.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.robocon321.demo.dto.review.CommentDTO;
import com.robocon321.demo.dto.taxomony.TaxomonyDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.taxomony.Taxomony;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PostDTO {
	private Integer id;

	@NotBlank(message = "Title not blank")
	private String title;
	
	@NotBlank(message = "Content not blank")
	private String content;
	
	@NotBlank(message = "Description not blank")
	private String description;

	@NotBlank(message = "Thumnail not blank")
	private String thumbnail;
	
	private String galleryImage;
	private String type;
	private PostDTO parentPost;
	
	@NotBlank(message = "Slug not blank")
	private String slug;
	
	private String metaTitle;	
	private String metaDescription;
	private Integer status;
	private UserDTO modifiedUser;
	private Date modifiedTime;
	private Double averageRating;
	private Integer totalComment;
	private Integer totalView;
	private List<PostDTO> posts = new ArrayList<>();
	private List<CommentDTO> comments = new ArrayList<>();
	private List<TaxomonyDTO> taxomonies = new ArrayList<>();
}
