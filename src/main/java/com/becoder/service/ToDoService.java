package com.becoder.service;

import java.util.List;

import com.becoder.ResourceNotFoundException;
import com.becoder.dto.ToDoDto;

public interface ToDoService {
	
	public Boolean saveTodo(ToDoDto todo) throws Exception;
	
	public ToDoDto getTodoById(Integer id) throws Exception;
	
	public List<ToDoDto> getTodoByUser();
	
}
