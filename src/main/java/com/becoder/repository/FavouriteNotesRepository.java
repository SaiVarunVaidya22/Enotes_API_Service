package com.becoder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.becoder.entity.FavouriteNotes;
import com.becoder.entity.Notes;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes, Integer> {

	public List<FavouriteNotes> findByUserId(Integer userId);

	public Optional<Notes> findByNoteId(Integer noteId);
	
}
