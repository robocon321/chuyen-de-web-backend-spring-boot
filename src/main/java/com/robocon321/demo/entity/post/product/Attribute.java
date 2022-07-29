package com.robocon321.demo.entity.post.product;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.entity.taxomony.TaxomonyObj;
import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attribute")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attribute implements TaxomonyObj {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;
		
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "mod_user_id")
	@JsonIgnore
	private User modifiedUser;
	
	@Column(name = "mod_time", 
			nullable = false, 
			columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	private Date modifiedTime;

	@ManyToAny(metaDef = "taxomony_obj", metaColumn = @Column(name = "type"))
	@Cascade({ org.hibernate.annotations.CascadeType.PERSIST })
	@JoinTable(name = "taxomony_relationship", joinColumns = @JoinColumn(name = "object_id"), inverseJoinColumns = @JoinColumn(name = "taxomony_id"))
	private List<Taxomony> taxomonies;

}
