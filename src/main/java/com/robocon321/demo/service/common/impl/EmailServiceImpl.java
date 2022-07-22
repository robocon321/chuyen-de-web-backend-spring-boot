package com.robocon321.demo.service.common.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.robocon321.demo.domain.EmailDetails;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.entity.common.Feedback;
import com.robocon321.demo.entity.user.User;
import com.robocon321.demo.repository.FeedbackRepository;
import com.robocon321.demo.repository.UserRepository;
import com.robocon321.demo.service.common.EmailService;

//Annotation
@Service
//Class
//Implementing EmailService interface
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	// Method 1
	// To send a simple email
	public boolean sendSimpleMail(EmailDetails details) throws RuntimeException {
		// Try block to check for exceptions
		try {
			// Creating a simple mail message
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			// Setting up necessary details
			mailMessage.setFrom(sender);
			mailMessage.setTo(details.getSendTo());
			mailMessage.setText(details.getContent());
			mailMessage.setSubject(details.getSubject());

			// Sending the mail
			javaMailSender.send(mailMessage);
			return true;
		}

		// Catch block to handle the exceptions
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Can not send your email");
		}
		
	}
}
