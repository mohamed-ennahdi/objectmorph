package com.github.mohamedennahdi.objectmorph.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.github.mohamedennahdi.objectmorph.app.dto.SourceCodeDto;
import com.github.mohamedennahdi.objectmorph.app.service.ObjectmorphService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ObjectmorphController {
	
	ObjectmorphService objectmorphService;
	
	@Value("${spring.application.version}")
    private String applicationVersion;
	
	public ObjectmorphController(ObjectmorphService objectmorphService) {
		this.objectmorphService = objectmorphService;
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping( value = "/html" )
	public ResponseEntity<String> html(@Parameter(name = "sourceCode", description = "Source Code", example = "class SourceCode { int attribute1; int attribute2; SourceCode(){} public int getAttribute1() { return attribute1;}}") 
									   @RequestBody	SourceCodeDto[] sourceCode) {
		try {
			String html = "";
			String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
			log.info("Session: " + sessionId);
			html = objectmorphService.generateHTML(sourceCode, sessionId);
			return new ResponseEntity<>(html, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error in ObjectmorphController", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping( value = "/version" )
	public ResponseEntity<String> version() {
		try {
			return new ResponseEntity<>(applicationVersion, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error in ObjectmorphController", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
