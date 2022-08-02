package com.robocon321.demo.service.checkout.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.checkout.CartDTO;
import com.robocon321.demo.dto.checkout.CartItemDTO;
//import com.robocon321.demo.dto.checkout.CheckoutDTO;
import com.robocon321.demo.dto.checkout.PaymentMethodDTO;
import com.robocon321.demo.dto.common.ContactDTO;
import com.robocon321.demo.dto.post.product.ProductDTO;
//import com.robocon321.demo.dto.review.CartDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.checkout.Cart;
import com.robocon321.demo.entity.checkout.CartItem;
import com.robocon321.demo.entity.checkout.Checkout;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.entity.user.User;
//import com.robocon321.demo.entity.review.Cart;
import com.robocon321.demo.repository.CartRepository;
import com.robocon321.demo.repository.ProductRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.checkout.CartService;
import com.robocon321.demo.specs.CartSpecification;
//import com.robocon321.demo.specs.CartSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
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
						
			
			cartDTOs.add(dto);
		}
		
		
		return cartDTOs;
	}
	
	private List<CartDTO> convertListEntityToDTO(List<Cart> carts){
		return carts.stream().map(cart -> convertEntityToDTO(cart)).collect(Collectors.toList());
	}
//	
	private CartDTO convertEntityToDTO(Cart cart) {
		CartDTO cartDTO = new CartDTO();
    	BeanUtils.copyProperties(cart, cartDTO);
    	
    	UserDTO userDTO = new UserDTO();
    	BeanUtils.copyProperties(cartDTO.getModifiedUser(), userDTO);
    	cartDTO.setModifiedUser(userDTO);
    	
    	cartDTO.setModifiedTime(cart.getModifiedTime());
    	
    	return cartDTO;
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

	@Override
	public List<CartDTO> saveCart(List<CartDTO> cartDTOs) {
		List<Cart> carts = new ArrayList<Cart>();
		try {
			cartDTOs.stream().forEach(cartDTO->{
				Cart cart = new Cart();
				BeanUtils.copyProperties(cartDTO, cart);
				
				User user = new User();
				BeanUtils.copyProperties(cartDTO.getModifiedUser(), user);
				cart.setModifiedUser(user);
				
				cart.setStatus(1);
				cart.setModifiedTime(new Date());
				carts.add(cart);
				
			});
			cartRepository.saveAll(carts);
			return convertListEntityToDTO(carts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		

	}

	@Override
	public void delete(List<Integer> ids) {
		// TODO Auto-generated method stub
		
	}
	
//	public List<Cart> getLastCasrt(Integer userId) {
//		return cartRepository.findByModifiedUserOrderByModifiedTimeDesc(userRepository.findById(userId).get());
//	}

	@Override
	public List<Cart> getLastCart(Integer userId) {
		// TODO Auto-generated method stub
		if(cartRepository.findByModifiedUserOrderByModifiedTimeDesc(userRepository.findById(userId).get())==null) {
			Cart cart = new Cart();
			cart.setModifiedUser(userRepository.findById(userId).get());
			cart.setStatus(1);
			cart.setModifiedTime(new Date());
			cartRepository.save(cart);
			return cartRepository.findByModifiedUserOrderByModifiedTimeDesc(userRepository.findById(userId).get());
		}
			
			
		return cartRepository.findByModifiedUserOrderByModifiedTimeDesc(userRepository.findById(userId).get());
	}
}
