package com.robocon321.demo.api.checkout;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.checkout.CheckoutDTO;
//import com.robocon321.demo.dto.post.PostDTO;
import com.robocon321.demo.entity.checkout.Cart;
import com.robocon321.demo.entity.checkout.Checkout;
import com.robocon321.demo.service.checkout.CheckoutService;


@RestController
@RequestMapping("/checkouts")
public class CheckoutController {
	@Autowired
	private CheckoutService checkoutService;
	
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

		Page<CheckoutDTO> pageResponse = checkoutService.getPage(search, size, page, sort, request);
		response.setData(pageResponse);
		response.setMessage("Successful!");
		response.setSuccess(true);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping(value = "")
	public ResponseEntity<Checkout> createCheckout(@Valid @RequestBody Checkout checkout) throws URISyntaxException{
		Checkout newCheckout = checkoutService.saveCheckout(checkout);
		return ResponseEntity.created(new URI("/checkouts/"+newCheckout.getId())).body(newCheckout);
	}
}
