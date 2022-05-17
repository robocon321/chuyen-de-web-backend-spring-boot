package com.robocon321.demo.dto.post.product;

import com.robocon321.demo.dto.post.PostDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	private Integer id;
	private PostDTO post;
	private Double minPrice;
	private Double maxPrice;
	private Integer stockQuantity;
	private Integer countRating;
	private Integer totalSales;
	private Double weight;
	private Double width;
	private Double height;
}
