package com.robocon321.demo.entity.post.product;

import java.util.Date;

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

import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attribute")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "product_id")
	private Product product;
		
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "mod_user_id", nullable = false)
	private User modifiedUser;
	
	@Column(name = "mod_time", 
			nullable = false, 
			columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	private Date modifiedTime;

}
