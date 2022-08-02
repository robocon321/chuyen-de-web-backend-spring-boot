package com.robocon321.demo.api.checkout;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.checkout.CartItemDTO;
import com.robocon321.demo.entity.checkout.CartItem;
import com.robocon321.demo.service.checkout.CartItemService;


@RestController
@RequestMapping("/cartitems")
public class CartItemController {

	@Autowired
	private CartItemService cartItemService;
	
	@GetMapping("")
	public ResponseEntity getAll(@RequestParam Map<String,String> request) {
		ResponseObject response = new ResponseObject<>();
		response.setData(cartItemService.getAll(request));
		response.setMessage("Successful");
		response.setSuccess(true);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/cartid/{id}")
	public ResponseEntity findByCartId(@PathVariable Integer id){
		ResponseObject response = new ResponseObject<>();
		
		List<CartItemDTO> cartItems = cartItemService.getByCartId(id);
		
		response.setData(cartItems);
		response.setMessage("Successful!");
		response.setSuccess(true);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("")
	public ResponseEntity add(@RequestBody List<Integer> ids) {
		System.out.println("list id cart=============="+ids);
		ResponseObject response = new ResponseObject<>();
		try {
			List<CartItemDTO> cartItemsDTO = cartItemService.save(ids);
			response.setData(cartItemsDTO);
			response.setMessage("Successful");
			response.setSuccess(true);
			return ResponseEntity.ok(response);
		}catch(Exception ex) {
			ex.printStackTrace();
			response.setMessage(ex.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	@PutMapping("/{id}")
	public ResponseEntity put(@Valid @RequestBody CartItemDTO cartItemDTO,@PathVariable Integer id) {
		System.out.println(id);
		ResponseObject response = new ResponseObject<>();
		try {
//			CartItemDTO cartItemsDTO = cartItemService.update(cartItemDTOs);
			response.setData(cartItemService.update(cartItemDTO));
			response.setMessage("Successful");
			response.setSuccess(true);
			return ResponseEntity.ok(response);
		}catch(Exception ex) {
			ex.printStackTrace();
			response.setMessage(ex.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	@DeleteMapping("")
	public ResponseEntity delete(@RequestBody List<Integer> ids) {
		ResponseObject response = new ResponseObject<>();
		try {
			cartItemService.delete(ids);
			response.setMessage("Successful!");
			response.setSuccess(true);
			return ResponseEntity.ok(response);
		} catch(Exception ex) {
			response.setMessage(ex.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);			
		}
	}
}
