package com.robocon321.demo.api;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robocon321.demo.domain.AccountRegisterDomain;
import com.robocon321.demo.domain.CustomUserDetailsDomain;
import com.robocon321.demo.domain.LoginRequestDomain;
import com.robocon321.demo.domain.LoginResponseDomain;
import com.robocon321.demo.domain.ResponseObjectDomain;
import com.robocon321.demo.dto.user.UserAccountDTO;
import com.robocon321.demo.dto.user.UserDTO;
import com.robocon321.demo.token.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final ModelMapper mapper = new ModelMapper();
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;	

    @PostMapping("/login")
    public ResponseEntity<ResponseObjectDomain<LoginResponseDomain>> authenticateUser(@Valid @RequestBody LoginRequestDomain loginRequest) {
        // Xác thực từ username và password.
    	ResponseObjectDomain<LoginResponseDomain> response = new ResponseObjectDomain<>();
    	
    	try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );    		
            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Trả về jwt cho người dùng.
            String jwt = tokenProvider.generateToken((CustomUserDetailsDomain) authentication.getPrincipal());

            response.setData(new LoginResponseDomain(jwt));
            response.setMessage("Successful!");
            response.setSuccess(true);
            return ResponseEntity.status(HttpStatus.OK).body(response);
            
    	} catch(Exception e) {
    		response.setMessage("Invalid username or password");
    		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    	}        
    }
    
    @GetMapping("/login")
    public ResponseEntity<ResponseObjectDomain> getInfoAccount() {
    	ResponseObjectDomain response = new ResponseObjectDomain<>();
    	try {
    		response.setData(mapper.map(((CustomUserDetailsDomain) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserAccount(), UserAccountDTO.class).getUser());
    		response.setSuccess(true);
    		response.setMessage("Successfull!");
    		return ResponseEntity.ok(response);    		
    	} catch (Exception e) {
    		response.setMessage(e.getMessage());
    		response.setSuccess(false);
    		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    	}
    }
    
    @PostMapping("/registerByAccount")
    public ResponseEntity<ResponseObjectDomain> registerByAccount(
    		@Valid @RequestBody AccountRegisterDomain accountRegisterDomain,
    		BindingResult result
    ) {
		ResponseObjectDomain response = new ResponseObjectDomain<>();

		if(result.hasErrors()) {
			String message = "";
			for(ObjectError error : result.getAllErrors()) {
				message += error.getDefaultMessage() + ". ";
			}
	    	response.setMessage(message.trim());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			
		} else {
			response.setData(accountRegisterDomain);
	    	response.setSuccess(true);
	    	return ResponseEntity.ok().body(response);			
		}
    }
}
