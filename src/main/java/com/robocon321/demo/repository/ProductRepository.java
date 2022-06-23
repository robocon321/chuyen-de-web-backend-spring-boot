package com.robocon321.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

//import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.post.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product>{
	public Page<Product> findByPost_TitleContaining(String search, Pageable page);
}
