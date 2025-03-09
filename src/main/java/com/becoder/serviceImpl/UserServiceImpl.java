package com.becoder.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.EmailRequest;
import com.becoder.dto.UserDto;
import com.becoder.entity.Role;
import com.becoder.entity.User;
import com.becoder.repository.RoleRepository;
import com.becoder.repository.UserRepository;
import com.becoder.service.EmailSenderService;
import com.becoder.service.UserService;
import com.becoder.util.Validation;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	public Validation validate;
	
	@Autowired
	public ModelMapper mapper;
	
	@Autowired
	public EmailSenderService emailSenderService;
		
	@Override
	public Boolean register(UserDto userDto) throws Exception {
		validate.userValidation(userDto);
		User user = mapper.map(userDto, User.class);
		setRole(userDto, user);
		User saveUser = userRepository.save(user);
		if(!ObjectUtils.isEmpty(saveUser)) {
			// Send eMail
			emailSend(saveUser);
			return true;
		}
		return false;
	}

	private void emailSend(User saveUser) throws Exception {
		String message="Hi, <b>"+saveUser.getFirstName()+"</b><br> Your account registration for Enotes was successful.<br>"
				+"<br> Click the below link to verify your account <br>"
				+"<a href='#'>Click Here</a> <br><br>"+"Thanks,<br>Enotes !";
		
		EmailRequest emailRequest = EmailRequest.builder()
				.to(saveUser.getEmail())
				.title("Account Creation Confirmation")
				.subject("Account Creation Success")
				.message(message)
				.build();
		
		emailSenderService.SendEmail(emailRequest);
	}

	private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleIds = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepository.findAllById(reqRoleIds);
		user.setRoles(roles);
	}
	
}
