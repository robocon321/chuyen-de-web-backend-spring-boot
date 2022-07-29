package com.robocon321.demo.entity.checkout;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.post.product.Attribute;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(targetEntity = Product.class)
	@JoinColumn(name = "product_id", nullable = false)
	@JsonIgnore
	private Product product;
	
	@Column(nullable = false, columnDefinition = "DEFAULT 1")
	private Integer quantity;
	
	@ManyToOne(targetEntity = Cart.class)
	@JoinColumn(name = "cart_id", nullable = false)
	@JsonIgnore
	private Cart cart;
}
