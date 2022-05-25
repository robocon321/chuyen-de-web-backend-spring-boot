package com.robocon321.demo.entity.post;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_meta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMeta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String key;
	private String value;
	
	@ManyToOne(targetEntity = Post.class)
	@JoinColumn(name = "post_id", nullable = false)
	@JsonIgnore
	private Post post;

}
