package com.robocon321.demo.api;


import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.EmailDetails;
import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.dto.taxomony.TaxomonyDTO;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.repository.TaxomonyRepository;
import com.robocon321.demo.service.common.EmailService;
import com.robocon321.demo.service.post.PostService;

@RestController
@RequestMapping("/test")	
public class TestController {	
		

}