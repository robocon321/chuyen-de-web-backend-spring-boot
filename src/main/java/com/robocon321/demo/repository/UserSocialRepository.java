package com.robocon321.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.user.UserSocial;

@Repository
public interface UserSocialRepository extends JpaRepository<UserSocial, Integer>{
	boolean existsByKeyAndType(String key, Integer type);
	Optional<UserSocial> findFirstByKeyAndTypeAndUid(String key, Integer type, String uid);
}
