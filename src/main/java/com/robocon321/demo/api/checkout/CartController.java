package com.robocon321.demo.api.checkout;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.entity.checkout.Cart;
import com.robocon321.demo.service.checkout.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {
	@Autowired
	private CartService cartService;
	
	@GetMapping("")
	public ResponseEntity getAll(@RequestParam Map<String, String> request) {
		ResponseObject response = new ResponseObject<>();		
		response.setData(cartService.getAll(request));
		response.setMessage("Successful!");
		response.setSuccess(true);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/{userId}")
	public ResponseEntity getByUserId(@PathVariable Integer userId) {
		ResponseObject response = new ResponseObject<>();
		try {
			response.setData(cartService.findByModUserid(userId));
			response.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		return ResponseEntity.ok(response);
	}
}
