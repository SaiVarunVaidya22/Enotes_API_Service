package com.becoder.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.becoder.ResourceNotFoundException;
import com.becoder.dto.FavouriteNoteDto;
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

	public void softDeleteNotes(Integer id) throws Exception;

	public void restoreNotes(Integer id) throws Exception;

	public List<NotesDto> getUserRecycleBinNotes(Integer userId);

	public void hardDeleteNotes(Integer id) throws Exception;

	public void emptyRecycleBin(int userId);
		
	public void unfavouriteNotes(Integer noteId) throws Exception;
	
	public List<FavouriteNoteDto> getUserFavouritenotes() throws Exception;

	public void favouriteNotes(Integer noteId) throws Exception;
}
