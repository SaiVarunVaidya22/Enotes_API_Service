package com.becoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.exception.ResourceNotFoundException;
import com.becoder.service.HomeService;
import com.becoder.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {
	
	@Autowired
	private HomeService homeService;
	
	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam Integer id,@RequestParam String vc) throws ResourceNotFoundException {
		Boolean accountVerify = homeService.verifyAccount(id, vc);
		if(accountVerify) {
			return CommonUtil.createBuildResponseMessage("Account verification success", HttpStatus.OK);
		}
		
		return CommonUtil.createErrorResponseMessage("Invalid Verification Link", HttpStatus.BAD_REQUEST);
	}
}
