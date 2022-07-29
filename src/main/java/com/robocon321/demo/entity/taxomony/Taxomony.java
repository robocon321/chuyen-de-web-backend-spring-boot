package com.robocon321.demo.entity.taxomony;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.common.ViewObj;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taxomony")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Taxomony implements ViewObj{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String slug;
	
	private String description;
	
	@Column(nullable = false)
	private String type;
	
	@ManyToOne(cascade = CascadeType.PERSIST, targetEntity = Taxomony.class)
	@JoinColumn(name = "parent_id")
	@JsonIgnore
	private Taxomony parent;
	
	@Column(columnDefinition = "DEFAULT 1")
	private Integer status;
		
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "mod_user_id")
	@JsonIgnore
	private User modifiedUser;
	
	@Column(name = "mod_time", 
			nullable = false, 
			columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	private Date modifiedTime;
	
	// Thêm formula product 
	@Formula("(select count(*) from taxomony_relationship tr where tr.taxomony_id = id)")
	private Integer totalPost;
	
	@Formula("(select count(*) from taxomony_relationship tr where tr.taxomony_id = id)")
	private Integer totalProduct;
	
	@OneToMany(mappedBy = "taxomony", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<TaxomonyMeta> metas;
	
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "parent")
	@JsonIgnore
	private List<Taxomony> childs;
	
	@ManyToAny(metaDef = "taxomony_obj", metaColumn = @Column(name = "type"))
	@Cascade({ org.hibernate.annotations.CascadeType.PERSIST })
	@JoinTable(name = "taxomony_relationship", joinColumns = @JoinColumn(name = "taxomony_id"), inverseJoinColumns = @JoinColumn(name = "object_id"))
	@JsonIgnore
	private List<TaxomonyObj> objects;
}
