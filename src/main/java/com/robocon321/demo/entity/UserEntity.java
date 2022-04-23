package com.robocon321.demo.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;


@Entity
@Data
@Table(name = "user")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column(name = "fullname", 
			columnDefinition = "VARCHAR", 
			length = 100)
	private String fullname;
	
	@Column(name = "email", 
			columnDefinition = "VARCHAR", 
			length = 50)
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
	private int status;
	
	@OneToOne
	@JoinColumn(name = "mod_user_id")
	private UserEntity modifiedUser;
	
	@Column(name = "mod_time")
	@LastModifiedDate
	private Date modifiedTime;
}
