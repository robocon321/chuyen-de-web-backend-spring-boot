package com.robocon321.demo.api.common;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.EmailDetails;
import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.common.FeedbackDTO;
import com.robocon321.demo.dto.common.ReplyFeedbackDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.common.Feedback;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.FeedbackRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.common.EmailService;
import com.robocon321.demo.service.common.FeedbackService;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {
	@Autowired
	private FeedbackService feedbackService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Autowired
	private UserRepository userRepository;


	@GetMapping("")
	public ResponseEntity get(@RequestParam Map<String, String> request) {
		String search = "";
		String sort = "";
		Integer page = 0;
		Integer size = 10;
		ResponseObject response = new ResponseObject<>();

		if (request.containsKey("search")) {
			search = request.get("search");
			request.remove("search");
		}
		if (request.containsKey("sort")) {
			sort = request.get("sort");
			request.remove("sort");
		}

		try {
			if (request.containsKey("size")) {
				size = Integer.parseInt(request.get("size"));
				request.remove("size");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);			
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}

		try {

			if (request.containsKey("page")) {
				page = Integer.parseInt(request.get("page")) - 1;
				if (page < 0)
					page = 0;

				request.remove("page");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}

		Page<FeedbackDTO> pageResponse = feedbackService.getPage(search, size, page, sort, request);
		response.setData(pageResponse);
		response.setMessage("Successful!");
		response.setSuccess(true);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("")
	public ResponseEntity save(@Valid @RequestBody List<FeedbackDTO> dtos, BindingResult result) {
		ResponseObject response = new ResponseObject<>();
		if (result.hasErrors()) {
			String message = "";
			for (ObjectError error : result.getAllErrors()) {
				message += error.getDefaultMessage() + ". ";
			}
			response.setMessage(message.trim());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} else {
			try {
				List<FeedbackDTO> data = feedbackService.insert(dtos);
				response.setData(data);
				response.setSuccess(true);
				return ResponseEntity.ok(response);
			} catch (Exception ex) {
				response.setMessage(ex.getMessage());
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}
	}

	@DeleteMapping("")
	public ResponseEntity delete(@RequestBody List<Integer> ids) {
		ResponseObject response = new ResponseObject<>();
		try {
			feedbackService.delete(ids);
			response.setMessage("Successful!");
			response.setSuccess(true);
			return ResponseEntity.ok(response);
		} catch(Exception ex) {
			response.setMessage(ex.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);			
		}
	}

	@PostMapping("/reply")
	public ResponseEntity reply(@Valid @RequestBody ReplyFeedbackDTO replyFeedbackDTO, BindingResult result) {
		ResponseObject response = new ResponseObject<>();
		if (result.hasErrors()) {
			String message = "";
			for (ObjectError error : result.getAllErrors()) {
				message += error.getDefaultMessage() + ". ";
			}
			response.setMessage(message.trim());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} else {
			try {
				Optional<Feedback> feedbackOpt = feedbackRepository.findById(replyFeedbackDTO.getFeedbackId());
				if (feedbackOpt.isEmpty())
					throw new RuntimeException("Your feedback is invalid");

				Feedback feedback = feedbackOpt.get();

				if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) throw new RuntimeException("Your session is invalid");
				try {
					UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					User user = userRepository.findById(userDTO.getId()).get();
					feedback.setUser(user);
				} catch(Exception ex) {
					throw new RuntimeException("Your session not found");
				}
				
				EmailDetails emailDetail = new EmailDetails();
				
				emailDetail.setSendTo(feedback.getEmail());
				emailDetail.setContent(replyFeedbackDTO.getContent());
				emailDetail.setSubject(replyFeedbackDTO.getSubject());
				
				emailService.sendSimpleMail(emailDetail);
				
				feedback.setStatus(1);
				feedbackRepository.save(feedback);

				response.setMessage("Successful!");
				response.setSuccess(true);
				return ResponseEntity.ok(response);
			} catch (Exception ex) {
				response.setMessage(ex.getMessage());
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}
	}
}
