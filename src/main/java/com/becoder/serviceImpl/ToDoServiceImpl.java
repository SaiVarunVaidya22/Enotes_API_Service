package com.becoder.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.dto.ToDoDto;
import com.becoder.dto.ToDoDto.StatusDto;
import com.becoder.entity.Todo;
import com.becoder.enums.TodoStatus;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.ToDoRepository;
import com.becoder.service.ToDoService;
import com.becoder.util.Validation;

@Service
public class ToDoServiceImpl implements ToDoService {

	@Autowired
	private ToDoRepository todoRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private Validation validate;
	
	@Override
	public Boolean saveTodo(ToDoDto todoDto) throws Exception {
		// Validate todo status
		validate.todoValidation(todoDto);
		Todo todo = mapper.map(todoDto, Todo.class);
		todo.setStatusId(todoDto.getStatus().getId());
		
		Todo saveTodo = todoRepo.save(todo);
		
		if(ObjectUtils.isEmpty(saveTodo)) {
			return false;
		}
		return true;
	}

	@Override
	public ToDoDto getTodoById(Integer id) throws Exception {
		Todo todoById = todoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo with id not found, Id invalid"));
		ToDoDto todoDto = mapper.map(todoById,ToDoDto.class);
		setStatus(todoDto,todoById);
		return todoDto;
	}

	private void setStatus(ToDoDto todoDto, Todo todoById) {
		for(TodoStatus st: TodoStatus.values()) {
			if(st.getId().equals(todoById.getStatusId())) {
				StatusDto statusDto = com.becoder.dto.ToDoDto.StatusDto.builder()
						.id(st.getId())
						.name(st.getName())
						.build();
				todoDto.setStatus(statusDto);
			}
		}
	}

	@Override
	public List<ToDoDto> getTodoByUser() {
		Integer userId=1;
		List<Todo> todoListByUser = todoRepo.findByCreatedBy(userId);
		List<ToDoDto> todoDtoListByUser = todoListByUser.stream().map(t -> mapper.map(t, ToDoDto.class)).toList();
		return todoDtoListByUser;
	}
	
}
