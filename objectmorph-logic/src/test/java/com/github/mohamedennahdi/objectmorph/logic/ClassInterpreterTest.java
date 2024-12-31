package com.github.mohamedennahdi.objectmorph.logic;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

import javax.annotation.processing.Generated;

import org.junit.jupiter.api.Test;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

@Generated(value = "org.junit-tools-1.1.0")
public class ClassInterpreterTest {

	private JavaClassInterpreter createTestSubject() throws URISyntaxException, FileNotFoundException, ParseException {
		String className = "TestClass.java";
		return new JavaClassInterpreter(Paths.get(getClass().getResource(className).toURI()).toFile());
	}
	
	@Test
	public void instantiationWithCompilationErrorTest() throws FileNotFoundException, URISyntaxException {
		String className = "TestClassWithCompilationError.java";
		assertThrows(ParseException.class, () -> {
			new JavaClassInterpreter(Paths.get(getClass().getResource(className).toURI()).toFile());
		}, "The source code of " + className + " does not compile.");
	}

	@Test
	public void getAttributesTest() throws Exception {
		JavaClassInterpreter testSubject;
		List<FieldDeclaration> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getFields();
		
		assertNotNull(result);
		assertEquals(5, result.size());
	}
	
	@Test
	public void getMethodsTest() throws Exception {
		JavaClassInterpreter testSubject;
		List<MethodDeclaration> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getMethods();
		
		assertNotNull(result);
		assertEquals(10, result.size());
	}
}