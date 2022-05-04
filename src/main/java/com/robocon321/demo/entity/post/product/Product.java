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

import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.entity.taxomony.TaxomonyMeta;

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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@OneToOne(cascade = CascadeType.ALL, targetEntity = Post.class)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;
	
	@Column(name = "min_price", nullable = false)
	private double minPrice;

	@Column(name = "max_price", nullable = false)
	private double maxPrice;
	
	@Column(name = "stock_quantity", 
			columnDefinition = "DEFAULT 0", 
			nullable = false)
	private int stockQuantity;

	@Column(name = "count_rating", 
			columnDefinition = "DEFAULT 0", 
			nullable = false)
	private int countRating;

	@Column(name = "total_sales", 
			columnDefinition = "DEFAULT 0", 
			nullable = false)
	private int totalSales;
	
	@Column(columnDefinition = "DEFAULT 0", nullable = false)
	private double weight;

	@Column(columnDefinition = "DEFAULT 0", nullable = false)
	private double width;
	
	@Column(columnDefinition = "DEFAULT 0", nullable = false)
	private double height;
}
