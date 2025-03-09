package com.becoder.serviceImpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.UserDto;
import com.becoder.entity.Role;
import com.becoder.entity.User;
import com.becoder.repository.RoleRepository;
import com.becoder.repository.UserRepository;
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
		
	@Override
	public Boolean register(UserDto userDto) {
		validate.userValidation(userDto);
		User user = mapper.map(userDto, User.class);
		setRole(userDto, user);
		User saveUser = userRepository.save(user);
		if(!ObjectUtils.isEmpty(saveUser)) {
			return true;
		}
		return false;
	}

	private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleIds = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepository.findAllById(reqRoleIds);
		user.setRoles(roles);
	}
	
}
