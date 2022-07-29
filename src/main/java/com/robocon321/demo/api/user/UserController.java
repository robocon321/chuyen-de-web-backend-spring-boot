package com.robocon321.demo.api.user;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.service.user.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

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

		Page<UserDTO> pageResponse = userService.getPage(search, size, page, sort, request);
		response.setData(pageResponse);
		response.setMessage("Successful!");
		response.setSuccess(true);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("")
	public ResponseEntity delete(@RequestBody List<Integer> ids) {
		ResponseObject response = new ResponseObject<>();
		try {
			userService.delete(ids);
			response.setMessage("Successful!");
			response.setSuccess(true);
			return ResponseEntity.ok(response);
		} catch(Exception ex) {
			response.setMessage(ex.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);			
		}
	}

	@PostMapping("")
	public ResponseEntity save(@Valid @RequestBody List<UserDTO> userDTOs, BindingResult result) {
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
				response.setData(userService.save(userDTOs));
				response.setMessage("Successful!");
				response.setSuccess(true);
				return ResponseEntity.ok(response);
			} catch(Exception ex) {
				ex.printStackTrace();
				response.setMessage("Server fail");
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}		
	}

	@GetMapping("/{id}")
	public ResponseEntity get(@PathVariable Integer id) {
		ResponseObject response = new ResponseObject<>();
		try {
			response.setData(userService.findById(id));
			response.setMessage("Successful!");
			response.setSuccess(true);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setMessage(ex.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}

	@PutMapping("")
	public ResponseEntity update(@Valid @RequestBody List<UserDTO> userDTOs, BindingResult result) {
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
				response.setData(userService.update(userDTOs));
				response.setMessage("Successful!");
				response.setSuccess(true);
				return ResponseEntity.ok(response);
			} catch(Exception ex) {
				ex.printStackTrace();
				response.setMessage("Server fail");
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
		}		
	}
}
