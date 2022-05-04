package com.robocon321.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.user.UserAccount;
import com.robocon321.demo.entity.user.User;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer>{
	UserAccount findByUser_id(int id);
	UserAccount findByUname(String uname);
	boolean existsByUname(String uname);
}
