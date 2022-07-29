package com.robocon321.demo.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.robocon321.demo.dto.common.FeedbackDTO;

public interface FeedbackService {
	public List<FeedbackDTO> insert(List<FeedbackDTO> list);
	public Page<FeedbackDTO> getPage(String search, Integer size, Integer page, String sort, Map<String, String> filter);
	public void delete(List<Integer> ids);
}
