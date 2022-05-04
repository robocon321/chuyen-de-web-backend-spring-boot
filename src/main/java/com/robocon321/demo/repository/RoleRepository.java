package com.robocon321.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.user.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	Role findOneByName(String name);
}
