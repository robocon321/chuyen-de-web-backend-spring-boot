package com.robocon321.demo.service.common.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.common.FeedbackDTO;
import com.robocon321.demo.dto.user.RoleDTO;
import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.common.Feedback;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.FeedbackRepository;
import com.robocon321.demo.service.common.FeedbackService;
import com.robocon321.demo.specs.FeedbackSpecification;
import com.robocon321.demo.specs.UserSpecification;
import com.robocon321.demo.type.FilterOperate;

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
	

	@Override
	public Page<FeedbackDTO> getPage(String search, Integer size, Integer page, String sort,
			Map<String, String> filter) {
		Specification<Feedback> spec = FeedbackSpecification.filter(new FilterCriteria("subject", FilterOperate.LIKE, search));

		for (Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();

			if (keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for (int i = 0; i < arrValue.length; i++) {
					String value = arrValue[i];
					Specification<Feedback> specType = FeedbackSpecification
							.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if (spec == null) {
						spec = specType;
					} else {
						if (i == 0)
							spec = spec.and(specType);
						else
							spec = spec.or(specType);
					}
				}
			} else if (keyEntry.startsWith("BT")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				if (arrValue.length == 2) {
					Specification<Feedback> specTypeGreater = FeedbackSpecification
							.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Feedback> specTypeLess = FeedbackSpecification
							.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[1]));
					spec = spec.and(specTypeGreater.and(specTypeLess));
				}
			}

			else if (keyEntry.startsWith("AND")) {
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for (String value : arrValue) {
					Specification<Feedback> specType = FeedbackSpecification
							.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if (spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}
			}
		}

		String arrSort[] = sort.split("__");
		String sortName;
		String sortType;
		if (arrSort.length == 2) {
			sortName = arrSort[0];
			sortType = arrSort[1];
		} else {
			sortName = "id";
			sortType = "ASC";
		}

		Page<Feedback> pageResponse = feedbackRepository.findAll(spec, PageRequest.of(page, size,
				sortType.equals("DESC") ? Sort.by(sortName).descending() : Sort.by(sortName).ascending()));
		return convertPageEntityToDTO(pageResponse);
	}
	
	private Page<FeedbackDTO> convertPageEntityToDTO(Page<Feedback> page) {
		return page.map(feedback -> convertEntityToDTO(feedback));
	}

	private FeedbackDTO convertEntityToDTO(Feedback feedback) {
		FeedbackDTO feedbackDTO = new FeedbackDTO();
		BeanUtils.copyProperties(feedback, feedbackDTO);
		User reply = feedback.getUser();
		if(reply != null) {
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(reply, userDTO);
			feedbackDTO.setUser(userDTO);
		}
				
		return feedbackDTO;
	}

	@Override
	public void delete(List<Integer> ids) {
		try {
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
				throw new RuntimeException("Your session not found");
			} else {
				UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				feedbackRepository.deleteAllById(ids);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Your session is invalid");
		}
	}

}
