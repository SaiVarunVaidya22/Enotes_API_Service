package com.becoder.controller;
import com.becoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.UserDto;
import com.becoder.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/user")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
		Boolean register = userService.register(userDto);
		if(register) {
			return CommonUtil.createBuildResponseMessage("Registration Successful", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Registration Failed", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@GetMapping("/user/{id}")
//	public ResponseEntity<?> getUserById(@PathVariable Integer id) {
//		userService.getUserById()
//	}
}
