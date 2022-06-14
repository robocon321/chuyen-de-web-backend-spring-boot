package com.robocon321.demo.api;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.CustomSpecification;
import com.robocon321.demo.domain.FilterCriteria;
import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.entity.post.Post;
import com.robocon321.demo.entity.taxomony.Taxomony;
import com.robocon321.demo.repository.PostRepository;
import com.robocon321.demo.repository.TaxomonyRepository;
import com.robocon321.demo.type.FilterOperate;

@RestController
@RequestMapping("/test")
public class TestController {	
	@Autowired
	private TaxomonyRepository taxomonyRepository;
	
	@GetMapping("")
	public ResponseEntity b() {
		
		ResponseObject obj = new ResponseObject<>();
		try {
			Specification spec1 = new CustomSpecification(new FilterCriteria("type", FilterOperate.EQUALS, "post"));
			Specification spec2 = new CustomSpecification(new FilterCriteria("type", FilterOperate.EQUALS, "product"));
			Specification s = spec1.or(spec2);
			
			obj.setData(taxomonyRepository.findAll(s));
			obj.setSuccess(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(obj);
	}
}