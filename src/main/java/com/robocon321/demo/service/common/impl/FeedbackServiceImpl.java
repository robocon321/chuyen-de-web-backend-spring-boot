package com.robocon321.demo.service.common.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robocon321.demo.dto.common.FeedbackDTO;
import com.robocon321.demo.entity.common.Feedback;
import com.robocon321.demo.repository.FeedbackRepository;
import com.robocon321.demo.service.common.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService{

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Override
	public List<FeedbackDTO> insert(List<FeedbackDTO> dtos) throws RuntimeException {
		List<Feedback> feedbacks = new ArrayList<>();
		for(FeedbackDTO dto : dtos) {
			Feedback feedback = new Feedback();
			BeanUtils.copyProperties(dto, feedback);
			feedback.setStatus(1);
			feedback.setModifiedTime(new Date());
			feedbacks.add(feedback);
		}
		try {
			feedbacks = feedbackRepository.saveAll(feedbacks);			
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Save feedback fail");
		}
		return convertListEntityToDTO(feedbacks);
	}
	
	private List<FeedbackDTO> convertListEntityToDTO(List<Feedback> feedbacks) {
		List<FeedbackDTO> dtos = new ArrayList<>();
		for(Feedback feedback : feedbacks) {
			dtos.add(convertEntityToDTO(feedback));
		}
		return dtos;
	}
	
	private FeedbackDTO convertEntityToDTO(Feedback feedback) {
		FeedbackDTO feedbackDTO = new FeedbackDTO();
		BeanUtils.copyProperties(feedback, feedbackDTO);
		return feedbackDTO;
	}

}
