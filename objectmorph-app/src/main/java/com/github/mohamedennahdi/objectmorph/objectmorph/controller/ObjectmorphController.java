package com.github.mohamedennahdi.objectmorph.objectmorph.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.mohamedennahdi.objectmorph.objectmorph.service.ObjectmorphService;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
public class ObjectmorphController {
	
	ObjectmorphService objectmorphService;
	
	public ObjectmorphController(ObjectmorphService objectmorphService) {
		this.objectmorphService = objectmorphService;
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping( value = "/hello", method =  RequestMethod.GET )
	public ResponseEntity<String> hello() {
		return new ResponseEntity<>( "Hello World", HttpStatus.OK);
	}
	
	@RequestMapping( value = "/test", method =  RequestMethod.GET )
	public ResponseEntity<String> test(List<String> files) {
		return new ResponseEntity<>( "Hello World", HttpStatus.OK);
	}
	
	@GetMapping( value = "/html" )
	public ResponseEntity<String> html(@Parameter(name = "sourceCode", description = "Source Code", example = "class SourceCode { int attribute1; int attribute2; SourceCode(){} public int getAttribute1() { return attribute1;}}") String[] sourceCode) {
		String html = "";
		try {
			html = objectmorphService.generateHTML(sourceCode);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(html, HttpStatus.OK);
	}
}
