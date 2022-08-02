package com.robocon321.demo.service.checkout;

import java.util.List;
import java.util.Map;

import com.robocon321.demo.dto.checkout.CartItemDTO;
import com.robocon321.demo.entity.checkout.Cart;

public interface CartItemService {
	public List<CartItemDTO> getAll(Map<String, String> request);
	public void delete(List<Integer> ids);
	public List<CartItemDTO> save(List<Integer> ids);
	public List<CartItemDTO> getByCartId(Integer cartId);
	public CartItemDTO update(CartItemDTO cartItemDTO);
	
}
