package com.robocon321.demo.service.review;

import java.util.List;
import java.util.Map;

import com.robocon321.demo.dto.review.CommentDTO;

public interface CommentService {
	public List<CommentDTO> getAll(Map<String, String> request);
}
