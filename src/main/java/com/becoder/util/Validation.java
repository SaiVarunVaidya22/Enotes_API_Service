package com.becoder.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.becoder.dto.ToDoDto;
import com.becoder.dto.ToDoDto.StatusDto;
import com.becoder.dto.UserDto;
import com.becoder.enums.TodoStatus;
import com.becoder.exception.ExistDataException;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.RoleRepository;
import com.becoder.repository.UserRepository;

import io.micrometer.common.util.StringUtils;

@Component
public class Validation {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
		
	public void todoValidation(ToDoDto todo) throws Exception {
		StatusDto reqStatus = todo.getStatus();
		Boolean statusFound = false;
		for(TodoStatus st:TodoStatus.values()) {
			if(st.getId().equals(reqStatus.getId())) {
				statusFound = true;
			}
		}
		if(!statusFound) {
			throw new ResourceNotFoundException("Invalid status");
		}
	}
	
	public void userValidation(UserDto userDto) {
		
		if(StringUtils.isEmpty(userDto.getFirstName())) {
			throw new IllegalArgumentException("First name of user invalid");
		}
		
		if(StringUtils.isEmpty(userDto.getLastName())) {
			throw new IllegalArgumentException("Last name of user invalid");
		}
		
		if(StringUtils.isEmpty(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
			throw new IllegalArgumentException("Email of user invalid");
		} else {
			// Validate email exist already
			Boolean existEmail = userRepository.existsByEmail(userDto.getEmail());
			if(existEmail) {
				throw new ExistDataException("User with this mail already exists");
			}
		}
		
		if(StringUtils.isEmpty(userDto.getMobNo()) || !userDto.getMobNo().matches(Constants.PHONE_REGEX)){
			throw new IllegalArgumentException("Mobile Number of user invalid");
		}
		
		if(CollectionUtils.isEmpty(userDto.getRoles())) {
			throw new IllegalArgumentException("Role of user invalid");
		} else {
			List<Integer> roleIds = roleRepository.findAll().stream().map(r -> r.getId()).toList();
			List<Integer> InvalidReqRoleIds = userDto.getRoles().stream().map(r -> r.getId()).filter(roleId -> !roleIds.contains(roleId)).toList();
			
			if(!CollectionUtils.isEmpty(InvalidReqRoleIds)) {
				throw new IllegalArgumentException("Role list of user inValid " + InvalidReqRoleIds);
			}
		}
	}
}
