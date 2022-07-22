package com.robocon321.demo.service.review.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.review.CommentDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.review.Comment;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.CommentRepository;
import com.robocon321.demo.service.review.CommentService;
import com.robocon321.demo.specs.CommentSpecification;
import com.robocon321.demo.specs.PostSpecification;
import com.robocon321.demo.type.FilterOperate;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public List<CommentDTO> getAll(Map<String, String> filter) {
		Specification<Comment> spec = null;	

		// OR__post.id=12%2C13
		for(Map.Entry<String, String> entry : filter.entrySet()) {
			String keyEntry = entry.getKey();
			String valueEntry = entry.getValue();
			if(keyEntry.startsWith("OR")) {
				String field = keyEntry.substring(3);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Comment> specType = CommentSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.or(specType);
					}
				}
			} else if(keyEntry.startsWith("BT")) {
				String field = keyEntry.substring(8);
				String[] arrValue = valueEntry.split("%2C");
				System.out.println(arrValue);
				if(arrValue.length == 2) {
					
					Specification<Comment> specTypeGreater = CommentSpecification.filter(new FilterCriteria(field, FilterOperate.GREATER, arrValue[0]));
					Specification<Comment> specTypeLess = CommentSpecification.filter(new FilterCriteria(field, FilterOperate.LESS, arrValue[1]));
					spec = specTypeGreater.and(specTypeLess);
				}
			}
			
			else if(keyEntry.startsWith("AND")){
				String field = keyEntry.substring(4);
				String[] arrValue = valueEntry.split("%2C");
				for(String value : arrValue) {
					Specification<Comment> specType = CommentSpecification.filter(new FilterCriteria(field, FilterOperate.EQUALS, value));
					if(spec == null) {
						spec = specType;
					} else {
						spec = spec.and(specType);
					}
				}				
			}
		}

		List<CommentDTO > commentDTOs = new ArrayList<CommentDTO>();
		List<Comment> comments = commentRepository.findAll(spec);
		for(Comment comment : comments) {						
			CommentDTO dto = new CommentDTO();
			BeanUtils.copyProperties(comment, dto);
			
			UserDTO userDTO = new UserDTO();
			BeanUtils.copyProperties(comment.getModifiedUser(), userDTO);
			dto.setModifiedUser(userDTO);
			
			Comment parent = comment.getParent();
			if(parent != null) {
				CommentDTO parentDTO = new CommentDTO();
				BeanUtils.copyProperties(comment.getParent(), parentDTO);
				dto.setParent(parentDTO);
				
				UserDTO userParentDTO = new UserDTO();
				BeanUtils.copyProperties(comment.getModifiedUser(), userParentDTO);				
				parentDTO.setModifiedUser(userParentDTO);
			}			
			
			commentDTOs.add(dto);
		}
		
		
		return commentDTOs;
	}

	@Override
	public CommentDTO add(CommentDTO commentDTO) {
		Comment comment = new Comment();
		BeanUtils.copyProperties(commentDTO, comment);
		
		if(commentDTO.getParent() != null) {
			Comment parent = new Comment();
			parent.setId(commentDTO.getParent().getId());
			comment.setParent(parent);
		}
		
		UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = new User();
		BeanUtils.copyProperties(userDTO, user);
		comment.setModifiedUser(user);

		comment.setStatus(1);
		comment.setModifiedTime(new Date());
		
		Post post = new Post();
		post.setId(commentDTO.getPost().getId());		
		comment.setPost(post);
		
		comment = commentRepository.save(comment);
		
		//	Convert and return
		
		BeanUtils.copyProperties(comment, commentDTO);
		BeanUtils.copyProperties(comment.getModifiedUser(), userDTO);
		commentDTO.setModifiedUser(userDTO);
		
		return commentDTO;
	}

}
