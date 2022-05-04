package com.robocon321.demo.dto.checkout;

import java.util.Date;

import com.robocon321.demo.dto.common.ContactDTO;
import com.robocon321.demo.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDTO {
	private Integer id;
	private CartDTO cart;
	private double shippingPrice;
	private double cartPrice;
	private ContactDTO contact;
	private PaymentMethodDTO paymentMethod;
	private int status;
	private UserDTO modifiedUser;
	private Date modifiedTime;
}
