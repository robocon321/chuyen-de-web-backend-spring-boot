package com.robocon321.demo.api.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.ResponseObject;
import com.robocon321.demo.dto.common.FavoriteDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.service.common.FavoriteService;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	@Autowired
	private FavoriteService favoriteService;
	
	@GetMapping("")
	public ResponseEntity getAll(@RequestParam Map<String, String> request) {
		String sort = "";
		ResponseObject response = new ResponseObject<>();

		if (request.containsKey("sort")) {
			sort = request.get("sort");
			request.remove("sort");
		}

		try {
			if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
				response.setMessage("Your session not found");
				response.setSuccess(false);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			} else {
				UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				request.put("AND_user.id", userDTO.getId() + "");
				
				List<FavoriteDTO> pageResponse = favoriteService.getAll(sort, request);
				response.setData(pageResponse);
				response.setMessage("Successful!");
				response.setSuccess(true);

				return ResponseEntity.status(HttpStatus.OK).body(response);				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setMessage("Your session invalid");
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		
	}

	@DeleteMapping("")
	public ResponseEntity delete(@RequestBody List<Integer> ids) {
		ResponseObject response = new ResponseObject<>();
		try {
			favoriteService.delete(ids);
			response.setMessage("Successful!");
			response.setSuccess(true);
		} catch(Exception ex) {
			ex.printStackTrace();
			response.setMessage(ex.getMessage());
			response.setSuccess(false);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			
		}
		return null;
	}
}
