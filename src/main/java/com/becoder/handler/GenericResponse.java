package com.becoder.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GenericResponse {
	
	private HttpStatus responseStatus;
	
	private String status;
	
	private Object message;
	
	private Object data;
	
	public ResponseEntity<?> create() {
		
		Map<String,Object> map = new LinkedHashMap<>();
		map.put("status", status);
		
		if(!ObjectUtils.isEmpty(message)) {
			map.put("message", message);
		}
		
		if(!ObjectUtils.isEmpty(data)) {			
			map.put("data", data);
		}
		
		return new ResponseEntity<>(map, responseStatus);
	}
	
	
}
