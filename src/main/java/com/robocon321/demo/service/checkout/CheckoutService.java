package com.robocon321.demo.service.checkout;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.robocon321.demo.dto.checkout.CheckoutDTO;
import com.robocon321.demo.entity.checkout.Checkout;

public interface CheckoutService {
	public Page<CheckoutDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter);
	public List<Checkout> getAll(Map<String, String> request);
	public Checkout saveCheckout(Checkout checkout);
	public void deleteCheckout(Integer checkoutId);
}
