package com.becoder.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.NotesDto;

import jakarta.validation.Valid;

public interface NotesService {
		
	public List<NotesDto> getAllNotes();

	public NotesDto getNotesById(Integer id);

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;
	
}
