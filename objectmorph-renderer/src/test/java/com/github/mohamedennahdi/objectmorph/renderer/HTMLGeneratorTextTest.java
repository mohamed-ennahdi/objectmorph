package com.github.mohamedennahdi.objectmorph.renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.processing.Generated;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.commons.util.StringUtils;

@TestMethodOrder(OrderAnnotation.class)
@Generated(value = "org.junit-tools-1.1.0")
public class HTMLGeneratorTextTest {
	
	@TempDir
	Path tempDir;
	
	String text1 = "package test; class SourceCode1 { int attribute1; String attribute2; }";
	String text2 = "package com.github.mohamedennahdi; class SourceCode2 { int attribute3; SourceCode1 attribute4; }";
	String text3 = "package com.github.mohamedennahdi; class SourceCodeUnary { protected Object attribute5; public SourceCodeUnary attribute6; }";

	private HTMLGenerator createTestSubject() throws Exception {
		
		
		final Path tempFile1 = Files.createFile(tempDir.resolve("SourceCode1.java"));
		final Path tempFile2 = Files.createFile(tempDir.resolve("SourceCode2.java"));
		 
		FileUtils.writeStringToFile(tempFile1.toFile(), text1, StandardCharsets.UTF_8);
		FileUtils.writeStringToFile(tempFile2.toFile(), text2, StandardCharsets.UTF_8);
		
		return new HTMLGenerator(new File[] {tempFile1.toFile(), tempFile2.toFile()});
	}

	
	private HTMLGenerator createTestUnarySubject() throws Exception {
		
		final Path tempFile = Files.createFile(tempDir.resolve("SourceCodeUnary.java"));
		
		FileUtils.writeStringToFile(tempFile.toFile(), text3, StandardCharsets.UTF_8);
		
		return new HTMLGenerator(new File[] {tempFile.toFile()});
	}
	
	
	@Order(1)
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
	
	@Order(2)
	@Test
	public void generateFullHTMLUnaryTest() throws Exception {
		HTMLGenerator testSubject;
		
		// default test
		testSubject = createTestUnarySubject();
		String actual = testSubject.generateFullHTML();
		Path path = Paths.get(getClass().getClassLoader().getResource("UnaryTestClass.java.html").toURI());
		String expected = Files.readString(path, StandardCharsets.UTF_8);
		
		assertEquals(StringUtils.replaceWhitespaceCharacters(expected, ""), StringUtils.replaceWhitespaceCharacters(actual, ""));
	}
}