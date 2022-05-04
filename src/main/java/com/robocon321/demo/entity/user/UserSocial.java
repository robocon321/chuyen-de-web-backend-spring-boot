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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "social_key", nullable = false)
	private String socialKey;

	@Column(name = "social_type", nullable = false)
	private String socialType;

	@OneToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;
}
