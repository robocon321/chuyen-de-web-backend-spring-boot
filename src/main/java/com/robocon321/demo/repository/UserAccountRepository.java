package com.robocon321.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.UserAccountEntity;
import com.robocon321.demo.entity.UserEntity;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Integer>{
	UserAccountEntity findByUser_id(int id);
	UserAccountEntity findByUname(String uname);
}
