package com.robocon321.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.checkout.Cart;
import com.robocon321.demo.entity.user.User;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>,JpaSpecificationExecutor<Cart>{
	
}
