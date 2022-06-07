package com.robocon321.demo.entity.review;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.checkout.PaymentMethod;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String content;

	@ManyToOne(targetEntity = Comment.class)
	@JoinColumn(name = "parent_id")
	@JsonIgnore
	private Comment parentComment;

	@ManyToOne(targetEntity = Post.class)
	@JoinColumn(name = "post_id", nullable = false)
	@JsonIgnore
	private Post post;
	
	@Column(nullable = false, columnDefinition = "DEFAULT 1")	
	private Integer status;
		
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "mod_user_id")
	@JsonIgnore
	private User modifiedUser;
	
	@Column(name = "mod_time", 
			nullable = false, 
			columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	private Date modifiedTime;
	
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "parentComment")
	@JsonIgnore
	private List<Comment> comments;
}
