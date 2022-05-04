package com.robocon321.demo.entity.post;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.entity.taxomony.TaxomonyMeta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_meta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMeta {
	private Integer id;
	private String key;
	private String value;
	
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Post.class)
	@JoinColumn(name = "post_id")
	private Post post;

}
