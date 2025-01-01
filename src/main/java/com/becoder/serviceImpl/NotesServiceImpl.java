package com.becoder.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.ResourceNotFoundException;
import com.becoder.dto.CategoryDto;
import com.becoder.dto.NotesDto;
import com.becoder.entity.Category;
import com.becoder.entity.Notes;
import com.becoder.repository.CategoryRepository;
import com.becoder.repository.NotesRepository;
import com.becoder.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Override
	public Boolean saveNotes(NotesDto notesDto) throws Exception{
		Integer catId = notesDto.getCategory().getId();
		checkCategoryExistWithId(catId);
		
		Notes notes = mapper.map(notesDto, Notes.class);
		Notes savedNotes = notesRepo.save(notes);
		if(!ObjectUtils.isEmpty(savedNotes)) {
			return true;
		}
		return false;
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

}
