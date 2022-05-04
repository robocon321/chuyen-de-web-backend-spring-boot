package com.robocon321.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.review.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{

}
