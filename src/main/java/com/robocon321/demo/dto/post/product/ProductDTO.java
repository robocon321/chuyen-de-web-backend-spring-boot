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
	private double minPrice;
	private double maxPrice;
	private int stockQuantity;
	private int countRating;
	private int totalSales;
	private double weight;
	private double width;
	private double height;
}
