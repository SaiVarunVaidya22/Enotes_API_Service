package com.becoder.util;

import org.springframework.context.annotation.Configuration;

import com.becoder.ResourceNotFoundException;
import com.becoder.dto.ToDoDto;
import com.becoder.dto.ToDoDto.StatusDto;
import com.becoder.enums.TodoStatus;

@Configuration
public class Validation {
	
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
}
