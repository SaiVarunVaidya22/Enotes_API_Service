package com.becoder.service;

import com.becoder.entity.User;

public interface JwtService {
	
	public String generateToken(User user);
	
}
