package com.robocon321.demo.service.checkout;
import java.util.List;
import java.util.Map;

import com.robocon321.demo.dto.checkout.CartDTO;
import com.robocon321.demo.entity.checkout.Cart;
import com.robocon321.demo.entity.user.User;

public interface CartService {
	public List<CartDTO> getAll(Map<String, String> request);
	public List<Cart> findByModUserid(Integer userId);
	public Cart saveCart(Cart cart);
	public void delete(List<Integer> ids);
	public List<Cart> getLastCart(Integer userId);

}
