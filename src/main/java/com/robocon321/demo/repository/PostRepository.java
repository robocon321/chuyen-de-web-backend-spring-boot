package com.robocon321.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.post.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
	Page<Post> findByTitleContaining(String search, Pageable page);
}
