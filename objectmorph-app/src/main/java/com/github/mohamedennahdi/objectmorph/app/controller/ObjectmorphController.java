package com.github.mohamedennahdi.objectmorph.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.mohamedennahdi.objectmorph.app.service.ObjectmorphService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ObjectmorphController {
	
	ObjectmorphService objectmorphService;
	
	public ObjectmorphController(ObjectmorphService objectmorphService) {
		this.objectmorphService = objectmorphService;
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping( value = "/html" )
	public ResponseEntity<String> html(@Parameter(name = "sourceCode", description = "Source Code", example = "class SourceCode { int attribute1; int attribute2; SourceCode(){} public int getAttribute1() { return attribute1;}}") 
									   @RequestParam("sourceCode")	String[] sourceCode) {
		try {
			String html = "";
			html = objectmorphService.generateHTML(sourceCode);
			return new ResponseEntity<>(html, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error in ObjectmorphController", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping( value = "/image" )
	public ResponseEntity<String> image(@Parameter(name = "htmlCode", description = "Source Code", example = "class SourceCode { int attribute1; int attribute2; SourceCode(){} public int getAttribute1() { return attribute1;}}") 
	@RequestParam("htmlCode")	String htmlCode) {
		try {
			String html = "";
			objectmorphService.generateImange(htmlCode);
			return new ResponseEntity<>("", HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error in ObjectmorphController", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
