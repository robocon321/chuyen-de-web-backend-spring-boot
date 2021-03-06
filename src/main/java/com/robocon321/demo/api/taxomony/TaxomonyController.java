package com.robocon321.demo.api.taxomony;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.service.taxomony.TaxomonyService;

@RestController
@RequestMapping("/taxomonies")
public class TaxomonyController {
	@Autowired
	private TaxomonyService taxomonyService;
	
	@GetMapping("")
	public ResponseEntity getAll(@RequestParam Map<String, String> request) {
		ResponseObject response = new ResponseObject<>();		
		response.setData(taxomonyService.getAll(request));
		response.setMessage("Successful!");
		response.setSuccess(true);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
