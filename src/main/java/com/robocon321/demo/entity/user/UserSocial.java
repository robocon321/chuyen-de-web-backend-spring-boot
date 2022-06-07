package com.robocon321.demo.entity.user;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.post.product.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_social")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSocial {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "`key`", nullable = false)
	private String key;

	@Column(name = "type", nullable = false)
	private Integer type;
	
	@Column(name = "uid", nullable = false)
	private String uid;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;
}
