package com.robocon321.demo.entity.common;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Any;

import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "view")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class View {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Any(metaDef = "view_obj", metaColumn = @Column(name = "type", nullable = false))
	@JoinColumn(name = "object_id", nullable = false)
	private ViewObj objectId;
	
	@Column(name = "cre_time", nullable = false)
	private Date creTime;
}
