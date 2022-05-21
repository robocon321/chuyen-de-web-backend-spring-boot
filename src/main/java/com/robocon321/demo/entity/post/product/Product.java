package com.robocon321.demo.entity.post.product;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.post.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id", nullable = false)
	@JsonIgnore
	private Post post;
	
	@Column(name = "min_price", nullable = false)
	private Double minPrice;

	@Column(name = "max_price", nullable = false)
	private Double maxPrice;
	
	@Column(name = "stock_quantity", 
			columnDefinition = "DEFAULT 0", 
			nullable = false)
	private Integer stockQuantity;

	@Column(name = "count_rating", 
			columnDefinition = "DEFAULT 0", 
			nullable = false)
	private Integer countRating;

	@Column(name = "total_sales", 
			columnDefinition = "DEFAULT 0", 
			nullable = false)
	private Integer totalSales;
	
	@Column(columnDefinition = "DEFAULT 0", nullable = false)
	private Double weight;

	@Column(columnDefinition = "DEFAULT 0", nullable = false)
	private Double width;
	
	@Column(columnDefinition = "DEFAULT 0", nullable = false)
	private Double height;
}
