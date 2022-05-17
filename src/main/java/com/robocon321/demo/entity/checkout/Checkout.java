package com.robocon321.demo.entity.checkout;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.robocon321.demo.entity.common.Contact;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "checkout")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checkout {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(targetEntity = Cart.class)
	@JoinColumn(name = "cart_id")
	@JsonIgnore
	private Cart cart;
	
	@Column(name = "shipping_price", nullable = false)
	private Double shippingPrice;
	
	@Column(name = "cart_price", nullable = false)
	private Double cartPrice;
	
	@ManyToOne(targetEntity = Contact.class)
	@JoinColumn(name = "contact_id")	
	@JsonIgnore
	private Contact contact;
	
	@ManyToOne(targetEntity = PaymentMethod.class)
	@JoinColumn(name = "paymethod_id")
	@JsonIgnore
	private PaymentMethod paymentMethod;
	
	@Column(nullable = false, columnDefinition = "DEFAULT 1")	
	private Integer status;
		
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "mod_user_id", nullable = false)
	@JsonIgnore
	private User modifiedUser;
	
	@Column(name = "mod_time", 
			nullable = false, 
			columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	private Date modifiedTime;
}
