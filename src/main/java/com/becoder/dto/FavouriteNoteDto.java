package com.becoder.dto;

import java.io.Serializable;

import com.becoder.entity.Notes;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavouriteNoteDto implements Serializable{
	
	private Integer id;
	
	private NotesDto note;
	
	private Integer userId;
}
