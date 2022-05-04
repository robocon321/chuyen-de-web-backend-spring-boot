package com.robocon321.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.robocon321.demo.entity.post.PostMeta;

@Repository
public interface PostMetaRepository extends JpaRepository<PostMeta, Integer>{

}
