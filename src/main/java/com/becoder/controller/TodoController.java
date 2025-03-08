package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.service.ToDoService;
import com.becoder.util.CommonUtil;

import jakarta.websocket.server.PathParam;

import com.becoder.dto.ToDoDto;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

	@Autowired
	public ToDoService todoService;
	
	@PostMapping("/")
	public ResponseEntity<?> saveTodo(@RequestBody ToDoDto todo) throws Exception {
		Boolean todoSaved = todoService.saveTodo(todo);
		if(todoSaved) {
			return CommonUtil.createBuildResponseMessage("Todo Saved Successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Unable to save Todo", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getTodoById(@PathVariable Integer id) throws Exception {
		ToDoDto todoById = todoService.getTodoById(id);
		return CommonUtil.createBuildResponse(todoById, HttpStatus.OK);
	}
	
	@GetMapping("/list") 
	public ResponseEntity<?> getAllTodoByUser() {
		List<ToDoDto> todoById = todoService.getTodoByUser();
		if(CollectionUtils.isEmpty(todoById)) {			
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(todoById, HttpStatus.OK);
	}
}
