package com.github.mohamedennahdi.objectmorph.app.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.github.mohamedennahdi.objectmorph.app.logic.ObjectmorphSingleton;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ObjectmorphService {
	ObjectmorphSingleton objectmorphLogic;
	
	public ObjectmorphService(ObjectmorphSingleton objectmorphLogic) {
		this.objectmorphLogic = objectmorphLogic;
	}

	public String generateHTML(String[] sourceCodes) throws IOException, URISyntaxException {
		List<File> files = new ArrayList<>(); 
		try {
			String patternString = "(?<=class ).*?(?=\\s)";
	        Pattern pattern = Pattern.compile(patternString);
			
	        String path = System.getProperty("user.home");
			for (String  sourceCode: sourceCodes) {
				Matcher matcher = pattern.matcher(sourceCode);
				if (matcher.find()) {
					String fileName = matcher.group() + ".java";
					log.info("Filename: " + fileName);
					File file = Files.createFile(Paths.get(path + File.separator + fileName)).toFile();
					FileUtils.writeStringToFile(file, sourceCode, StandardCharsets.UTF_8);
					files.add(file);
				}
			}
			
			return objectmorphLogic.getHtmlGenerator(files.toArray(new File[0])).generateFullHTML();
		} catch (Exception e) {
			throw e;
		} finally {
			for (File file : files) {
				Files.delete(file.toPath());
			}
		}
	}
}
