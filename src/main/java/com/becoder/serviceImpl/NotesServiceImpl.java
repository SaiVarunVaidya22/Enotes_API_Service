package com.becoder.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.becoder.ResourceNotFoundException;
import com.becoder.dto.FavouriteNoteDto;
import com.becoder.dto.NotesDto;
import com.becoder.dto.NotesDto.FilesDto;
import com.becoder.dto.NotesResponse;
import com.becoder.entity.FavouriteNotes;
import com.becoder.entity.FileDetails;
import com.becoder.entity.Notes;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.FavouriteNotesRepository;
import com.becoder.repository.FileRepository;
import com.becoder.repository.NotesRepository;
import com.becoder.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private FavouriteNotesRepository favouriteNotesRepo;
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@Autowired
	private FileRepository fileRepository;
	
	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception{
		ObjectMapper ob = new ObjectMapper();
		NotesDto notesDto = ob.readValue(notes, NotesDto.class);
		notesDto.setIsDeleted(false);
		notesDto.setDeletedOn(null);
		
		Integer catId = notesDto.getCategory().getId();
		
		if(!ObjectUtils.isEmpty(notesDto.getId())) {
			updateNotes(notesDto, file);
		}
		
		// category Validation
		checkCategoryExistWithId(catId);
		
		Notes notesMap = mapper.map(notesDto, Notes.class);
		
		FileDetails fileDtls = saveFileDetails(file);
		
		if(!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		} else {
			if(ObjectUtils.isEmpty(notesDto.getId())) {
				notesMap.setFileDetails(null);
			}
		}
		
		Notes savedNotes = notesRepo.save(notesMap);
		if(!ObjectUtils.isEmpty(savedNotes)) {
			return true;
		}
		return false;
	}

	private void updateNotes(NotesDto notesDto, MultipartFile file) throws ResourceNotFoundException {
		Notes existingNotes = notesRepo.findById(notesDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Invalid Notes id"));
		if(ObjectUtils.isEmpty(file)) {
			notesDto.setFileDetails(mapper.map(existingNotes.getFileDetails(), FilesDto.class));
		}
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if(!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
			
			String originalFilename = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFilename);
			List<String> acceptedFileFormats = Arrays.asList("pdf","xlxs","jpeg", "png");
			if(!acceptedFileFormats.contains(extension)) {
				throw new IllegalArgumentException("Invalid file format, accepted formats pdf, xlsx, png, jpeg");
			}
			
			String rndString = UUID.randomUUID().toString();
			String uploadedFileName = rndString + "." + extension;
			
			File saveFile = new File(uploadPath);
			
			if(!saveFile.exists()) {
				saveFile.mkdir();
			}
			String storePath = uploadPath.concat(uploadedFileName);
			
			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
			
			if(upload != 0) {
				FileDetails fileDtls = new FileDetails();
				fileDtls.setOriginalFileName(originalFilename);
				fileDtls.setDisplayFileName(getDisplayName(originalFilename));
				fileDtls.setUploadedFileName(uploadedFileName);
				fileDtls.setFileSize(file.getSize());
				fileDtls.setPath(storePath);
				FileDetails savedFileDtls = fileRepository.save(fileDtls);
				return savedFileDtls;
			}
		}
		
		return null;
	}

	private String getDisplayName(String originalFilename) {
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.removeExtension(originalFilename);
		
		if(fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName+ "." + extension;
		return fileName;
	}

	private void checkCategoryExistWithId(Integer catId) throws Exception {
		categoryRepo.findById(catId).orElseThrow(() -> new ResourceNotFoundException("category id is invalid"));
	}

	@Override
	public List<NotesDto> getAllNotes() {
		List<Notes> notes = notesRepo.findAll();
		List<NotesDto> notesDtoList = notes.stream()
				.map(note -> mapper.map(note, NotesDto.class)).collect(Collectors.toList());
		
		return notesDtoList;
	}

	@Override
	public NotesDto getNotesById(Integer id) {
		Optional<Notes> noteById = notesRepo.findById(id);
		
		NotesDto noteDto = mapper.map(noteById, NotesDto.class);
		return noteDto;
	}

	@Override
	public byte[] downloadFile(FileDetails fileDtls) throws Exception {
		InputStream io = new FileInputStream(fileDtls.getPath());
		return StreamUtils.copyToByteArray(io);
	}

	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		FileDetails fileDtls = fileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File unavailable to download"));
		return fileDtls;
	}

	@Override
	public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes = notesRepo.findAllByCreatedByAndIsDeletedFalse(userId, pageable);
		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();
		
		NotesResponse notes = NotesResponse.builder()
				.notes(notesDto)
				.pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize())
				.totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages())
				.isFirst(pageNotes.isFirst())
				.isLast(pageNotes.isLast())
				.build();
		return notes;
	}

	@Override
	public void softDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes with given id Not Found"));
		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		Notes savedNotes = notesRepo.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws Exception {
		Notes notes = notesRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes with given id Not Found"));
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		Notes savedNotes = notesRepo.save(notes);
	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
		List<Notes> recycleBinNotes = notesRepo.findAllByCreatedByAndIsDeletedTrue(userId);
		List<NotesDto> notesDto = recycleBinNotes.stream().map(n -> mapper.map(n, NotesDto.class)).toList();
		return notesDto;
	}

	@Override
	public void hardDeleteNotes(Integer id) throws Exception {
		 Notes notes = notesRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Notes not found"));
		 
		 if(notes.getIsDeleted()) {
			 notesRepo.delete(notes);
		 } else {
			 throw new IllegalArgumentException("Sorry you can't hard delete directly");
		 }
	}

	@Override
	public void emptyRecycleBin(int userId) {
		List<Notes> recycleBinNotes = notesRepo.findAllByCreatedByAndIsDeletedTrue(userId);
		if(!CollectionUtils.isEmpty(recycleBinNotes)) {
			notesRepo.deleteAll(recycleBinNotes);
		}
	}

	@Override
	public void favouriteNotes(Integer noteId) throws Exception {
		Integer userId = 1;
		Notes notes = notesRepo.findById(noteId).orElseThrow(()-> new ResourceNotFoundException("Notes not found as id is invalid"));		

		FavouriteNotes favouriteNotes = FavouriteNotes.builder()
				.note(notes)
				.userId(userId)
				.build();
		favouriteNotesRepo.save(favouriteNotes);
	}

	@Override
	public void unfavouriteNotes(Integer favouriteNoteId) throws Exception {
        FavouriteNotes favNote = favouriteNotesRepo.findById(favouriteNoteId).orElseThrow(()-> new ResourceNotFoundException("Favourite Note not found as id is invalid"));
        // 	Remember we should use favouriteNote to delete not the noteId
        favouriteNotesRepo.delete(favNote);
	}

	@Override
	public List<FavouriteNoteDto> getUserFavouritenotes() throws Exception {
		Integer userId = 1;
		
		List<FavouriteNotes> favNotes = favouriteNotesRepo.findByUserId(userId);
		return favNotes.stream().map(fn -> mapper.map(fn, FavouriteNoteDto.class)).toList();
		
	}

}
