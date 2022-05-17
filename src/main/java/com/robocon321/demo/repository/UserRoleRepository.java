package com.robocon321.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.user.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>{
	Optional<UserRole> findOneByUserIdAndRoleId(Integer userId, Integer roleId);
}
