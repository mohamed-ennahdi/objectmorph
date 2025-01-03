package com.github.mohamedennahdi.objectmorph.app.service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.mohamedennahdi.objectmorph.app.dto.SourceCodeDto;
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
	        String path = System.getProperty("user.home");
			for (SourceCodeDto sourceCode: sourceCodes) {
				sourceCode.setSourceCode(URLDecoder.decode(sourceCode.getSourceCode(), Charset.forName("UTF-8")));
				String fileName = sourceCode.getFilename();
				log.info("Filename: " + fileName);
				File file = new File(path + File.separator + fileName);
				try (Writer fileWriter = new FileWriter(file, false)) {
					fileWriter.write(sourceCode.getSourceCode());
				} catch (Exception e) {
					throw e;
				}
				files.add(file);
			}
			return objectmorphLogic.getHtmlGenerator(files.toArray(new File[0])).generateFullHTML();
		} catch (Exception e) {
			throw e;
		} finally {
			log.info("Files removal.");
			for (File file : files) {
				Files.delete(file.toPath());
			}
		}
	}
}
