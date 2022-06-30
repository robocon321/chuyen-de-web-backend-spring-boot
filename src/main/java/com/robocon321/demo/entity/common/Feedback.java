package com.robocon321.demo.entity.common;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feedback")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String fullname;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable =  false)
	private String subject;
	
	@Column(nullable = false)
	private String message;
	
	@Column(nullable = false, name = "mod_time")
	private Date modifiedTime;
	
	@Column(nullable = false)
	private Integer status;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
