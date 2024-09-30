package com.github.ennahdi.objectmorph.objectmorph.service;

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

import com.github.ennahdi.objectmorph.objectmorph.logic.ObjectmorphSingleton;

@Service
public class ObjectmorphService {
	ObjectmorphSingleton objectmorphLogic;
	
	public ObjectmorphService(ObjectmorphSingleton objectmorphLogic) {
		this.objectmorphLogic = objectmorphLogic;
	}

	public String generateHTML(String[] sourceCodes) throws IOException, URISyntaxException {
		try {
			String patternString = "(?<=class ).*?(?=\\s)";
	        Pattern pattern = Pattern.compile(patternString);
			
			List<File> files = new ArrayList<>(); 
			for (String  sourceCode: sourceCodes) {
				Matcher matcher = pattern.matcher(sourceCode);
				if (matcher.find()) {
					System.out.println(matcher.group());
				}
				
				File file = Files.createFile(Paths.get(matcher.group() + ".java")).toFile();
				FileUtils.writeStringToFile(file, sourceCode, StandardCharsets.UTF_8);
				files.add(file);
			}
			
			
			return objectmorphLogic.getHtmlGenerator(files.toArray(new File[0])).generateFullHTML();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (URISyntaxException e) {
			throw e;
		}
	}
}
