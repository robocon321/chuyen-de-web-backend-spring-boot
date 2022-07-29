package com.robocon321.demo.service.checkout.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.checkout.CartDTO;
import com.robocon321.demo.dto.checkout.CheckoutDTO;
import com.robocon321.demo.dto.checkout.PaymentMethodDTO;
import com.robocon321.demo.dto.common.ContactDTO;
import com.robocon321.demo.entity.checkout.CartItem;
import com.robocon321.demo.entity.checkout.Checkout;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.CheckoutRepository;
import com.robocon321.demo.service.checkout.CheckoutService;
import com.robocon321.demo.specs.CheckoutSpectification;
//import com.robocon321.demo.specs.PostSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class CheckoutServiceImpl implements CheckoutService{

	@Autowired
	private CheckoutRepository checkoutRepository;
	
	@Override
	public List<Checkout> getAll(Map<String, String> request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Checkout saveCheckout(Checkout checkout) {
		// TODO Auto-generated method stub
		return checkoutRepository.save(checkout);
	}

	@Override
	public void deleteCheckout(Integer checkoutId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<CheckoutDTO> getPage(String search, Integer size, Integer page, String sort,
			Map<String, String> filter) {
		Specification<Checkout> spec = null;

		for(Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Checkout> specType  = CheckoutSpectification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.or(specType);
					}
				}
			} else if(keyEntry.startsWith("BT")) {
				String field = keyEntry.substring(8);
				String[] arrValue = valueEntry.split("%2C");
				if(arrValue.length == 2) {
					Specification<Checkout> specTypeGreater = CheckoutSpectification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Checkout> specTypeLess = CheckoutSpectification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[0]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Checkout> specType = CheckoutSpectification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}				
			}
		}
		
		String arrSort[] = sort.split("__");
		String sortName;
		String sortType;
		if(arrSort.length == 2) {
			sortName = arrSort[0];
			sortType = arrSort[1];						
		} else {
			sortName = "id";
			sortType = "ASC";
		}
	
		Page<Checkout> pageResponse = checkoutRepository.findAll(spec, PageRequest.of(page, size, sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending()));
		return convertPageEntityToDTO(pageResponse);
	}
	
	private Page<CheckoutDTO> convertPageEntityToDTO(Page<Checkout> page) {
		return page.map(post -> convertEntityToDTO(post));	
	}
	
	private List<CheckoutDTO> convertListEntityToDTO(List<Checkout> posts) {
		return posts.stream().map(post -> convertEntityToDTO(post)).collect(Collectors.toList());
	}
	
	private CheckoutDTO convertEntityToDTO(Checkout checkout) {
		CheckoutDTO checkoutDTO = new CheckoutDTO();
    	BeanUtils.copyProperties(checkout, checkoutDTO);
    	
    	CartDTO cartDTO = new CartDTO();
    	BeanUtils.copyProperties(checkout.getCart(), cartDTO);
    	checkoutDTO.setCart(cartDTO);
    	
    	ContactDTO contactDTO = new ContactDTO();
    	BeanUtils.copyProperties(checkout.getContact(), contactDTO);
    	checkoutDTO.setContact(contactDTO);
    	
    	PaymentMethodDTO methodDTO = new PaymentMethodDTO();
    	BeanUtils.copyProperties(checkout.getPaymentMethod(), methodDTO);
    	checkoutDTO.setPaymentMethod(methodDTO);
    	
    	
//    	User userMod = post.getModifiedUser();
//    	if(userMod != null) {
//	    	UserDTO userDTO = new UserDTO();
//	    	BeanUtils.copyProperties(userMod, userDTO);
//	    	postDTO.setModifiedUser(userDTO);
//    	}
    	
    	return checkoutDTO;
	}
}
