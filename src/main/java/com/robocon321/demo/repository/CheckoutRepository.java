package com.robocon321.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.checkout.Checkout;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Integer>, JpaSpecificationExecutor<Checkout> {

}
