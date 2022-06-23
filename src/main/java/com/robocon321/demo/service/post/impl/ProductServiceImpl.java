package com.robocon321.demo.service.post.impl;

import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.post.product.ProductDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.ProductRepository;
import com.robocon321.demo.service.post.ProductService;	

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Page<ProductDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter) {
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
		
		Page<Product> pageResponse = productRepository.findByPost_TitleContaining(search, PageRequest.of(page, size, sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending()));
		return convertPageSummary(pageResponse);
	}

	private Page<ProductDTO> convertPageSummary(Page<Product> page) {
		return page.map(new Function<Product, ProductDTO>() {
		    @Override
		    public ProductDTO apply(Product product) {
		    	ProductDTO productDTO = new ProductDTO();
		    	BeanUtils.copyProperties(product, productDTO);

		    	PostDTO postDTO = new PostDTO();
		    	BeanUtils.copyProperties(product.getPost(), postDTO);
		    	productDTO.setPost(postDTO);
		    	
		    	User userMod = product.getPost().getModifiedUser();
		    	if(userMod != null) {
			    	UserDTO userDTO = new UserDTO();
			    	BeanUtils.copyProperties(userMod, userDTO);
			    	productDTO.getPost().setModifiedUser(userDTO);
		    	}
		    	
		    	return productDTO;
		    }
		});	
	}

	
}
