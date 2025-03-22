package com.becoder.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.becoder.entity.AccountStatus;
import com.becoder.entity.User;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.exception.SuccessException;
import com.becoder.repository.UserRepository;
import com.becoder.service.HomeService;

@Service
public class HomeServiceImpl implements HomeService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public Boolean verifyAccount(Integer userId,String verificationCode) throws ResourceNotFoundException {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));
		
		if(user.getStatus().getVerificationCode() == null) {
			throw new SuccessException("Account Already Verified");
		}
		
		if(user.getStatus().getVerificationCode().equals(verificationCode)) {
			AccountStatus status = user.getStatus();
			status.setIsActive(true);
			status.setVerificationCode(null);
			
			userRepo.save(user);
			return true;
		}
		
		return false;
	}
}
