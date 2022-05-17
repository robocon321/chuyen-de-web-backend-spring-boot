package com.robocon321.demo.entity.user;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.post.product.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "fullname", 
			columnDefinition = "VARCHAR", 
			length = 100)
	private String fullname;
	
	@Column(name = "email", 
			columnDefinition = "VARCHAR", 
			length = 50)
	@JsonIgnore
	private String email;
	
	@Column(name = "phone",
			columnDefinition = "VARCHAR",
			length = 15)
	private String phone;
	
	@Column(name = "avatar",
			columnDefinition = "VARCHAR",
			length = 200)
	private String avatar;
	
	@Column(name = "status",
			columnDefinition = "INT DEFAULT 1")
	private Integer status;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "mod_user_id")
	@JsonIgnore
	private User modifiedUser;
	
	@Column(name = "mod_time", 
			nullable = false, 
			columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	private Date modifiedTime;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_role", 
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	@JsonIgnore
	private List<Role> roles;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "wishlist",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "product_id"))
	@JsonIgnore
	private List<Product> wishlist;	
}
