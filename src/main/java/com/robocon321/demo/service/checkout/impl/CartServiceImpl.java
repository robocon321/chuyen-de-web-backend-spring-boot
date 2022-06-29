package com.robocon321.demo.service.checkout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.checkout.CartDTO;
//import com.robocon321.demo.dto.review.CartDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.checkout.Cart;
import com.robocon321.demo.entity.user.User;
//import com.robocon321.demo.entity.review.Cart;
import com.robocon321.demo.repository.CartRepository;
import com.robocon321.demo.service.checkout.CartService;
import com.robocon321.demo.specs.CartSpecification;
//import com.robocon321.demo.specs.CartSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartRepository cartRepository;

	@Override
	public List<CartDTO> getAll(Map<String, String> filter) {
		Specification<Cart> spec = null;	

		// OR__post.id=12%2C13
		for(Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Cart> specType = CartSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.or(specType);
					}
				}
			} else if(keyEntry.startsWith("BT")) {
				String field = keyEntry.substring(8);
				String[] arrValue = valueEntry.split("%2C");
				System.out.println(arrValue);
				if(arrValue.length == 2) {
					
					Specification<Cart> specTypeGreater = CartSpecification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Cart> specTypeLess = CartSpecification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[1]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Cart> specType = CartSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}				
			}
		}

		List<CartDTO > cartDTOs = new ArrayList<CartDTO>();
		List<Cart> carts = cartRepository.findAll(spec);
		for(Cart cart : carts) {						
			CartDTO dto = new CartDTO();
			BeanUtils.copyProperties(cart, dto);
			
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(cart.getModifiedUser(), userDTO);
			dto.setModifiedUser(userDTO);
			
//			Cart parent = cart.getParent();
//			if(parent != null) {
//				CartDTO parentDTO = new CartDTO();
//				BeanUtils.copyProperties(comment.getParent(), parentDTO);
//				dto.setParent(parentDTO);
//				
//				UserDTO userParentDTO = new UserDTO();
//				BeanUtils.copyProperties(comment.getModifiedUser(), userParentDTO);				
//				parentDTO.setModifiedUser(userParentDTO);
//			}			
			
			cartDTOs.add(dto);
		}
		
		
		return cartDTOs;
	}

	@Override
	public List<Cart> findByModUserid(Integer userId) {
		// TODO Auto-generated method stub
		List<Cart> lists = cartRepository.findAll();
		List<Cart> result = new ArrayList<Cart>();
		for(Cart cart:lists) {
			if(cart.getModifiedUser().getId()==userId) {
				result.add(cart);
			}
		}
		return result;
	}
	
	

}
