package com.becoder.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.becoder.ResourceNotFoundException;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;

import jakarta.validation.Valid;

public interface NotesService {
		
	public List<NotesDto> getAllNotes();

	public NotesDto getNotesById(Integer id);

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;

	public byte[] downloadFile(FileDetails fileDtls) throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;

	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);
	
}
