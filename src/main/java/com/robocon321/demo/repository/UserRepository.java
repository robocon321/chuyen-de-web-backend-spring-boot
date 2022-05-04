package com.robocon321.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	boolean existsByEmail(String email);
}
