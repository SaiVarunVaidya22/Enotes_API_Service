package com.becoder.service;

import com.becoder.exception.ResourceNotFoundException;

public interface HomeService {
	
	public Boolean verifyAccount(Integer userId,String verificationCode) throws ResourceNotFoundException;
	
}
