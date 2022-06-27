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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.common.ViewObj;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.entity.review.Comment;
import com.robocon321.demo.entity.review.Vote;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.entity.taxomony.TaxomonyObj;
import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post implements ViewObj, TaxomonyObj {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String description;

	private String thumbnail;

	@Column(name = "gallery_image")
	private String galleryImage;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_id")
	private Post parentPost;

	@Column(nullable = false)
	private String slug;

	@Column(nullable = false)
	private String type;
	
	@Column(name = "meta_title")
	private String metaTitle;

	@Column(name = "meta_description")
	private String metaDescription;

	@Column(nullable = false, columnDefinition = "DEFAULT 1")
	private Integer status;

	@Formula("(select count(*) from comment where comment.post_id=id)")
	private Integer totalComment;

	@Formula("(select ROUND(avg(vote.star), 1) from vote where vote.post_id = id)")
	private Double averageRating;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "mod_user_id")
	private User modifiedUser;

	@Column(name = "mod_time", nullable = false, columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	private Date modifiedTime;

//	@ManyToMany(cascade = CascadeType.REMOVE)
//	@JoinTable(name = "link_post", 
//				joinColumns = @JoinColumn(nullable = false, name = "post1_id"), 
//				inverseJoinColumns = @JoinColumn(nullable = false, name = "post2_id"))	
//	@JsonIgnore
//	private List<Post> linkPosts;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
	@JsonIgnore
	private List<Comment> comments;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
	@JsonIgnore
	private List<Vote> votes;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "post")
	@JsonIgnore
	private List<PostMeta> postMetas;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parentPost")
	@JsonIgnore
	private List<Post> posts;

	@OneToOne(cascade = CascadeType.REMOVE, mappedBy = "post")
	@JsonIgnore
	private Product product;

	@ManyToAny(metaDef = "taxomony_obj", metaColumn = @Column(name = "type"))
	@Cascade({ org.hibernate.annotations.CascadeType.PERSIST })
	@JoinTable(name = "taxomony_relationship", joinColumns = @JoinColumn(name = "object_id"), inverseJoinColumns = @JoinColumn(name = "taxomony_id"))
	private List<Taxomony> taxomonies;
}
