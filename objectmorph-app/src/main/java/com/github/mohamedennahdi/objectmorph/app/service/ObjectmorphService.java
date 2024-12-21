package com.github.mohamedennahdi.objectmorph.app.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.github.mohamedennahdi.objectmorph.app.logic.ObjectmorphComponent;
import com.pdfcrowd.Pdfcrowd;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ObjectmorphService {
	ObjectmorphComponent objectmorphLogic;

	public ObjectmorphService(ObjectmorphComponent objectmorphLogic) {
		this.objectmorphLogic = objectmorphLogic;
	}

	public String generateHTML(String[] sourceCodes) throws IOException, URISyntaxException {
		List<File> files = new ArrayList<>();
		try {
			String patternString = "(?<=class ).*?(?=\\s)";
			Pattern pattern = Pattern.compile(patternString);

			String path = System.getProperty("user.home");
			for (String sourceCode : sourceCodes) {
				Matcher matcher = pattern.matcher(sourceCode);
				if (matcher.find()) {
					String fileName = matcher.group() + ".java";
					log.info("Filename: " + fileName);
					File file = new File(path + File.separator + fileName);
					try (Writer fileWriter = new FileWriter(file, false)) {
						fileWriter.write(sourceCode);
					} catch (Exception e) {
						throw e;
					}
					files.add(file);
				}
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

	public void generateImange(String htmlCode) throws IOException {
		try {
			// Create an API client instance.
			Pdfcrowd.HtmlToImageClient client = new Pdfcrowd.HtmlToImageClient("demo", "ce544b6ea52a5621fb9d55f8b542d14d");

			// Configure the conversion.
			client.setOutputFormat("png");

			Path tempFile = Files.createTempFile(null, ".html");
			Files.write(tempFile, htmlCode.getBytes(StandardCharsets.UTF_8));
			
			
			// Run the conversion and save the result to a file.
			client.convertFileToFile(tempFile.toFile().getAbsolutePath(), "c:/work/example.png");
		} catch (Pdfcrowd.Error why) {
			System.err.println("Pdfcrowd Error: " + why);
			throw why;
		} catch (IOException why) {
			System.err.println("IO Error: " + why);
			throw why;
		}
	}
}
