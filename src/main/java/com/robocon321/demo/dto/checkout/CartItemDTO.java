package com.robocon321.demo.dto.checkout;

import com.robocon321.demo.dto.post.product.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
	private Integer id;
	private ProductDTO product;
	private Integer quantity;
	private CartDTO cart;
}
