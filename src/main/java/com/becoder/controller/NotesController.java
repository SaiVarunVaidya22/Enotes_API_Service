package com.becoder.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.NotesDto;
import com.becoder.service.NotesService;
import com.becoder.util.CommonUtil;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {
	
	@Autowired
	private NotesService notesService;
	
	@GetMapping("/")
	public ResponseEntity<?> getAllNotes() {
		List<NotesDto> allNotes = notesService.getAllNotes();
		
		if(!ObjectUtils.isEmpty(allNotes)) {			
			return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
		}
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getNotesById(@PathVariable Integer id) {
		NotesDto noteById = notesService.getNotesById(id);
		if(ObjectUtils.isEmpty(noteById)) {
			return CommonUtil.createErrorResponseMessage("Notes with given id Not Found", HttpStatus.NOT_FOUND);
		}
		return CommonUtil.createBuildResponse(noteById, HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> saveNotes(@Valid @RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception {
		Boolean saveNotes = notesService.saveNotes(notes, file);
		if(saveNotes) {			
			return CommonUtil.createBuildResponseMessage("Notes saved success", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
