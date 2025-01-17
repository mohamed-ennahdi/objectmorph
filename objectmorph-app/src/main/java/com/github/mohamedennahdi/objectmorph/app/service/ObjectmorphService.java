package com.github.mohamedennahdi.objectmorph.app.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.mohamedennahdi.objectmorph.app.dto.SourceCodeDto;
import com.github.mohamedennahdi.objectmorph.app.exception.ValidationException;
import com.github.mohamedennahdi.objectmorph.app.logic.ObjectmorphComponent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ObjectmorphService {
	ObjectmorphComponent objectmorphLogic;
	
	public ObjectmorphService(ObjectmorphComponent objectmorphLogic) {
		this.objectmorphLogic = objectmorphLogic;
	}

	public String generateHTML(SourceCodeDto[] sourceCodes) throws Exception {
		List<File> files = new ArrayList<>(); 
		try {
			validateFilenames(sourceCodes);
	        String path = System.getProperty("user.home");
			for (SourceCodeDto sourceCode: sourceCodes) {
				sourceCode.setSourceCode(URLDecoder.decode(sourceCode.getSourceCode(), StandardCharsets.UTF_8));
				String fileName = sourceCode.getFilename();
				log.info("Filename: " + fileName);
				File file = saveFile(path, sourceCode);
				files.add(file);
			}
			return objectmorphLogic.getHtmlGenerator(files.toArray(new File[0])).generateFullHTML();
		} finally {
			log.info("Files removal.");
			for (File file : files) {
				Files.delete(file.toPath());
			}
		}
	}
	
	private void validateFilenames(SourceCodeDto[] sourceCodes) throws ValidationException {
		List<String> filenames = Arrays.asList(sourceCodes).stream().map(n -> n.getFilename()).collect(Collectors.toList());
		Set<String> uniqueFilenames = new HashSet<>(filenames);
		if (uniqueFilenames.size() != filenames.size()) {
			throw new ValidationException("File names must be unique.");
		}
	}
	
	private File saveFile(String path, SourceCodeDto sourceCode) throws IOException  {
		File file = new File(path);
		try (Writer fileWriter = new FileWriter(file, false)) {
			fileWriter.write(sourceCode.getSourceCode());
		}
		return file;
	}
}
