package com.robocon321.demo.service.checkout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.checkout.CartDTO;
//import com.robocon321.demo.dto.checkout.CartDTO;
import com.robocon321.demo.dto.checkout.CartItemDTO;
import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.post.product.ProductDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.checkout.Cart;
import com.robocon321.demo.entity.checkout.CartItem;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.entity.user.User;
//import com.robocon321.demo.entity.checkout.Cart;
import com.robocon321.demo.repository.CartItemRepository;
import com.robocon321.demo.repository.CartRepository;
import com.robocon321.demo.repository.ProductRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.checkout.CartItemService;
import com.robocon321.demo.specs.CartItemSpectification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class CartItemServiceImpl implements CartItemService{

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Override
	public List<CartItemDTO> getAll(Map<String, String> request) {
		Specification<CartItem> spec = null;	

		// OR__post.id=12%2C13
		for(Map.Entry<String, String> entry : request.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<CartItem> specType = CartItemSpectification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					
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
					
					Specification<CartItem> specTypeGreater = CartItemSpectification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<CartItem> specTypeLess = CartItemSpectification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[1]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<CartItem> specType = CartItemSpectification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}				
			}
		}

		List<CartItem> carts = cartItemRepository.findAll(spec);
		return convertListEntityToDTO(carts);
		
	}

	private List<CartItemDTO> convertListEntityToDTO(List<CartItem> cartitems){
		return cartitems.stream().map(cartItem -> convertEntityToDTO(cartItem)).collect(Collectors.toList());
	}
	
	private CartItemDTO convertEntityToDTO(CartItem cartItem) {
		CartItemDTO cartItemDTO = new CartItemDTO();
		
		BeanUtils.copyProperties(cartItem, cartItemDTO);
		
		CartDTO cartDTO = new CartDTO();
		BeanUtils.copyProperties(cartItem.getCart(), cartDTO);
		cartItemDTO.setCart(cartDTO);
		
		ProductDTO productDTO = new ProductDTO();
		BeanUtils.copyProperties(cartItem.getProduct(), productDTO);

		PostDTO postDTO = new PostDTO();
		BeanUtils.copyProperties(cartItem.getProduct().getPost(), postDTO);
		productDTO.setPost(postDTO);
		
		cartItemDTO.setProduct(productDTO);
		
		
		
		return cartItemDTO;
	}
	
	@Override
	public void delete(List<Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<CartItemDTO> add(List<Integer> ids) throws RuntimeException{
		System.out.println("List ids------- "+ids);
		try {
			if(SecurityContextHolder.getContext().getAuthentication().getPrincipal()==null) {
				 throw new RuntimeException("Your session not found");
			}else {
				Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Optional<User> optional = userRepository.findById(userDTO.getId());
//				System.out.println(optional);
				if(optional.isPresent()) {
					List<CartItem> cartItems = new ArrayList<>();
					for(Integer id:ids) {
						Optional<Product> productOpt = productRepository.findById(id);
						List<Cart> cartOpt = cartRepository.findByModifiedUserOrderByModifiedTimeDesc(optional.get());
						if(productOpt.isPresent()) {
						CartItem cartItem = new CartItem();
						cartItem.setProduct(productOpt.get());
						cartItem.setCart(cartOpt.get(0));
						cartItem.setQuantity(1);
						cartItems.add(cartItem);
						}else {
							throw new RuntimeException("Your product not found");
						}
					}
					cartItems = cartItemRepository.saveAll(cartItems);
					return convertListEntityToDTO(cartItems);
				}
					else throw new RuntimeException("Your session not found");	
				
			}
			
		}catch(Exception ex) {
			throw new RuntimeException("Your session is invalid "+ex);		
		}
	}

	@Override
	public List<CartItemDTO> getByCartId(Integer cartId) {
		try {
			if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
				throw new RuntimeException("Your session not found");
			}else {
				Optional<Cart> cart = cartRepository.findById(cartId);
				List<CartItem> cartItems = cartItemRepository.findByCart(cart.get());
				
				return convertListEntityToDTO(cartItems);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Your session is invalid");
		}
	}
}
