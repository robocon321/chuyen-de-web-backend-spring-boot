package com.robocon321.demo.entity.post;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.review.Comment;
import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String content;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false, columnDefinition  = "DEFAULT 0")
	private int view;
	
	@Column(nullable = false, columnDefinition = "DEFAULT ''")
	private String thumbnail;
	
	@Column(name = "gallery_image", 
			nullable = false, 
			columnDefinition = "DEFAULT '[]'")
	private String galleryImage;
	
	@Column(nullable = false)
	private String type;
	
	@OneToOne(cascade = CascadeType.ALL, targetEntity = Post.class)
	@JoinColumn(name = "parent_id")
	private Post parentPost;
	
	@Column(nullable = false)
	private String slug;
	
	private String metaTitle;
	
	private String metaDescription;
	
	@Column(name = "comment_status", 
			nullable = false, 			
			columnDefinition = "DEFAULT 1")
	private Integer commentStatus;
	
	@Column(name = "comment_count", 
			nullable = false, 			
			columnDefinition = "DEFAULT 1")
	private Integer commentCount;
	
	@Column(nullable = false, columnDefinition = "DEFAULT 1")	
	private Integer status;
		
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "mod_user_id", nullable = false)
	private User modifiedUser;
	
	@Column(name = "mod_time", 
			nullable = false, 
			columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	private Date modifiedTime;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "link_post", 
				joinColumns = @JoinColumn(nullable = false, name = "post1_id"), 
				inverseJoinColumns = @JoinColumn(nullable = false, name = "post2_id"))	
	@JsonIgnore
	private List<Post> posts;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "parentComment")
	@JsonIgnore
	private List<Comment> comments;
}
