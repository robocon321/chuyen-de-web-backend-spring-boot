package com.robocon321.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.robocon321.demo.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_account")
public class UserAccountEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(columnDefinition = "VARCHAR", 
			length = 50, 
			nullable = false,
			unique = true)
	private String uname;

	@Column(columnDefinition = "VARCHAR", 
			length = 50,
			nullable = false)
	private String pwd;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
}
