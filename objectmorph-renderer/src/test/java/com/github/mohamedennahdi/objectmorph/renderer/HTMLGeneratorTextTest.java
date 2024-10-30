package com.github.mohamedennahdi.objectmorph.renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.processing.Generated;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.commons.util.StringUtils;

@Generated(value = "org.junit-tools-1.1.0")
public class HTMLGeneratorTextTest {
	
	@TempDir
	Path tempDir;
	
	String text1 = "package test; class SourceCode1 { int attribute1; String attribute2; }";
	String text2 = "package com.github.mohamedennahdi; class SourceCode2 { int attribute3; SourceCode1 attribute4; }";

	private HTMLGenerator createTestSubject() throws IOException {
		
		
		final Path tempFile1 = Files.createFile(tempDir.resolve("SourceCode1.java"));
		final Path tempFile2 = Files.createFile(tempDir.resolve("SourceCode2.java"));
		 
		FileUtils.writeStringToFile(tempFile1.toFile(), text1, StandardCharsets.UTF_8);
		FileUtils.writeStringToFile(tempFile2.toFile(), text2, StandardCharsets.UTF_8);
		
		return new HTMLGenerator(new File[] {tempFile1.toFile(), tempFile2.toFile()});
	}

	@Test
	public void generateFullHTMLTest() throws Exception {
		HTMLGenerator testSubject;

		// default test
		testSubject = createTestSubject();
		String actual = testSubject.generateFullHTML();
		Path path = Paths.get(getClass().getClassLoader().getResource("TestClass.java.html").toURI());
		String expected = Files.readString(path, StandardCharsets.UTF_8);
		
		assertEquals(StringUtils.replaceWhitespaceCharacters(expected, ""), StringUtils.replaceWhitespaceCharacters(actual, ""));
	}
}