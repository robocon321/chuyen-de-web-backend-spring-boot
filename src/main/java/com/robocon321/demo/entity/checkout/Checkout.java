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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(targetEntity = Cart.class)
	@JoinColumn(name = "cart_id")
	private Cart cart;
	
	@Column(name = "shipping_price", nullable = false)
	private double shippingPrice;
	
	@Column(name = "cart_price", nullable = false)
	private double cartPrice;
	
	@OneToMany(targetEntity = Contact.class)
	@JoinColumn(name = "contact_id")	
	private Contact contact;
	
	@OneToMany(targetEntity = PaymentMethod.class)
	@JoinColumn(name = "cart_id")
	private PaymentMethod paymentMethod;
	
	@Column(nullable = false, columnDefinition = "DEFAULT 1")	
	private int status;
		
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "mod_user_id", nullable = false)
	private User modifiedUser;
	
	@Column(name = "mod_time", 
			nullable = false, 
			columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	private Date modifiedTime;
}
