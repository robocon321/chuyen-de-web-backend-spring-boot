package com.robocon321.demo.service.post.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.robocon321.demo.domain.FilterCriteria;
//import com.robocon321.demo.dto.checkout.CartDTO;
import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.post.product.ProductDTO;
import com.robocon321.demo.dto.user.UserDTO;
//import com.robocon321.demo.entity.checkout.Cart;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.post.product.Product;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.repository.ProductRepository;
import com.robocon321.demo.service.post.ProductService;
//import com.robocon321.demo.specs.CartSpecification;
import com.robocon321.demo.specs.ProductSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PostRepository postRepository;

	@Override
	public Page<ProductDTO> getPage(String search, Integer size, Integer page, String sort,
			Map<String, String> filter) {
		Specification<Product> spec = ProductSpecification
				.filter(new FilterCriteria("post.title", FilterOperate.LIKE, search));

		for (Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();

			if (keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for (String value : arrValue) {
					Specification<Product> specType;
					if (field.equals("taxomony")) {
						try {
							specType = ProductSpecification.filterByTaxomonyId(Integer.parseInt(value));
						} catch (Exception e) {
							specType = ProductSpecification.filterByTaxomonyId(0);
							e.printStackTrace();
						}
					} else
						specType = ProductSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if (spec == null) {
						spec = specType;
					} else {
						spec = spec.or(specType);
					}
				}
			} else if (keyEntry.startsWith("BT")) {
				String field = keyEntry.substring(8);
				String[] arrValue = valueEntry.split("%2C");
				if (arrValue.length == 2) {
					Specification<Product> specTypeGreater = ProductSpecification
							.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Product> specTypeLess = ProductSpecification
							.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[1]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}

			else if (keyEntry.startsWith("AND")) {
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for (String value : arrValue) {
					Specification<Product> specType;
					if (field.equals("taxomony")) {
						try {
							specType = ProductSpecification.filterByTaxomonyId(Integer.parseInt(value));
						} catch (Exception e) {
							specType = ProductSpecification.filterByTaxomonyId(0);
							e.printStackTrace();
						}
					} else
						specType = ProductSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if (spec == null) {
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
		if (arrSort.length == 2) {
			sortName = arrSort[0];
			sortType = arrSort[1];
		} else {
			sortName = "id";
			sortType = "ASC";
		}

		Page<Product> pageResponse = productRepository.findAll(spec, PageRequest.of(page, size,
				sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending()));
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
				if (userMod != null) {
					UserDTO userDTO = new UserDTO();
					BeanUtils.copyProperties(userMod, userDTO);
					productDTO.getPost().setModifiedUser(userDTO);
				}

				return productDTO;
			}
		});

	}
//	
//	public List<Product> findById(Integer id)

	@Override
	public Product findBySlug(String slug) {
		List<Product> list = productRepository.findAll();
		for (Product product : list) {
			if (product.getPost().getSlug().equals(slug)) {

				return product;
			}
		}
		return null;
	}

	@Override
	public List<ProductDTO> saveProduct(List<ProductDTO> productDTOs) {
		// TODO Auto-generated method stub
		List<Product> products = new ArrayList<Product>();
		productDTOs.stream().forEach(productDTO->{
			Product product = new Product();
			BeanUtils.copyProperties(productDTO, product);
			
			Post post = new Post();
			BeanUtils.copyProperties(productDTO.getPost(), post);
			product.setPost(post);
			
			postRepository.save(post);
			products.add(product);
		});
		productRepository.saveAll(products);
		return convertListEntityToDTO(products);
	}

	@Override
	public List<ProductDTO> update(List<ProductDTO> productDTOs) {
		// TODO Auto-generated method stub
		List<Product> products = new ArrayList<Product>();
		productDTOs.stream().forEach(productDTO->{
			Product product = productRepository.findById(productDTO.getId()).get();
			
			Post post = new Post();
			BeanUtils.copyProperties(productDTO.getPost(), post);
			product.setPost(post);
			
			postRepository.save(post);
			products.add(product);
		});
		productRepository.saveAll(products);
		return convertListEntityToDTO(products);
	}
	@Override
	public void deleteProduct(List<Integer> ids) {
		productRepository.deleteAllById(ids);

	}
	

//	@Override
//	public ProductDTO add(ProductDTO product) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	private List<ProductDTO> convertListEntityToDTO(List<Product> products) {
		return products.stream().map(post -> convertEntityToDTO(post)).collect(Collectors.toList());
	}

	private ProductDTO convertEntityToDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		BeanUtils.copyProperties(product, productDTO);

		PostDTO postDTO = new PostDTO();
		BeanUtils.copyProperties(product.getPost(), postDTO);
		productDTO.setPost(postDTO);

		return productDTO;
	}

	@Override
	public List<ProductDTO> getAll(Map<String, String> request) {
		Specification<Product> spec = null;	

		// OR__post.id=12%2C13
		for(Map.Entry<String, String> entry : request.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Product> specType = ProductSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					
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
					
					Specification<Product> specTypeGreater = ProductSpecification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Product> specTypeLess = ProductSpecification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[1]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Product> specType = ProductSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}				
			}
		}

		List<ProductDTO > postDTOs = new ArrayList<ProductDTO>();
		List<Product> products = productRepository.findAll(spec);
		for(Product product : products) {						
			ProductDTO dto = new ProductDTO();
			BeanUtils.copyProperties(product, dto);
			
			PostDTO postDTO = new PostDTO();
			BeanUtils.copyProperties(product.getPost(), postDTO);
			dto.setPost(postDTO);
						
			
			postDTOs.add(dto);
		}
		
		
		return postDTOs;
	}


}
