package com.robocon321.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.checkout.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

}
