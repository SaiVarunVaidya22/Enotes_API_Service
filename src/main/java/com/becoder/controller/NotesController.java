package com.becoder.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.dto.FavouriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FileDetails;
import com.becoder.service.NotesService;
import com.becoder.util.CommonUtil;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {
		FileDetails fileDtls = notesService.getFileDetails(id);
		byte[] downlodFile = notesService.downloadFile(fileDtls);
		
		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDtls.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDtls.getOriginalFileName());
		return ResponseEntity.ok().headers(headers).body(downlodFile);
	}
	
	@GetMapping("/user-notes")
	public ResponseEntity<?> getAllNotesByUser(
			@RequestParam(name="pageNo", defaultValue="0") Integer pageNo,
			@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Integer userId = 1;
		NotesResponse notes = notesService.getAllNotesByUser(userId, pageNo, pageSize);
		
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
		notesService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Notes deleted succesfully",HttpStatus.OK);
	}
	
	@GetMapping("/restore/{id}")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
		notesService.restoreNotes(id);
		return CommonUtil.createBuildResponseMessage("Notes restore success",HttpStatus.OK);
	}
	
	@GetMapping("/recycle-bin")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
		Integer userId = 1;
		List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
		if(CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage("Recycle bin is Empty", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
		notesService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Notes deleted succesfully",HttpStatus.OK);
	}
	
	@DeleteMapping("/delete-recycle-bin/{id}")
	public ResponseEntity<?> emptyRecycleBin(@PathVariable Integer id) throws Exception {
		int userId=1;
		notesService.emptyRecycleBin(userId);
		return CommonUtil.createBuildResponseMessage("Recycle bin cleaned up",HttpStatus.OK);
	}
	
	@GetMapping("/fav/{noteId}")
	public ResponseEntity<?> favouriteNote(@PathVariable Integer noteId) throws Exception {
		notesService.favouriteNotes(noteId);
		return CommonUtil.createBuildResponseMessage("Notes added to favourites",HttpStatus.OK);
	}
	
	@DeleteMapping("/un-fav/{favNoteId}")
	public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws Exception {
		notesService.unfavouriteNotes(favNoteId);
		return CommonUtil.createBuildResponseMessage("Note removed from favourites",HttpStatus.OK);
	}
	
	@GetMapping("/fav-notes")
	public ResponseEntity<?> getAllUserFavouriteNotes() throws Exception {
		List<FavouriteNoteDto> userFavouritenotes = notesService.getUserFavouritenotes();
		if(CollectionUtils.isEmpty(userFavouritenotes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(userFavouritenotes,HttpStatus.OK);
	}
	
	@GetMapping("/copy/{noteId}")
	public ResponseEntity<?> copyNotes(@PathVariable Integer noteId) throws Exception {
		
		boolean copyNotes = notesService.copyNotes(noteId);
		
		if(copyNotes) {
			return CommonUtil.createBuildResponseMessage("Copied notes success", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponseMessage("Notes not copied, Try again !",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
