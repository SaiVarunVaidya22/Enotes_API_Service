package com.becoder.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.becoder.entity.Notes;
import com.becoder.repository.NotesRepository;

@Component
public class NotesScheduler {
	
	@Autowired
	private NotesRepository notesRepo;
	
	@Scheduled(cron="0 0 0 * * ?") 
	public void deleteNotesScheduler() {
		LocalDateTime cuttOffDate = LocalDateTime.now().minusDays(7);
		List<Notes> deleteNotes = notesRepo.findAllByIsDeletedAndDeletedOnBefore(true, cuttOffDate);
		notesRepo.deleteAll(deleteNotes);
	}
}
