package com.github.mohamedennahdi.objectmorph.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaClassInterpreter {

	
	private ClassOrInterfaceDeclaration decl;
	
	private String className;
	private String packageName;
	
	private static int INSTANCE_ID = 0;
	
	private int instanceId;
	
	public JavaClassInterpreter(File myClassSourceFile) throws FileNotFoundException, ParseException {
		
		INSTANCE_ID ++;
		
		this.instanceId = INSTANCE_ID;
		
		JavaParser parser = new JavaParser();
		
		ParseResult<CompilationUnit> pr;
		try {
			pr = parser.parse( myClassSourceFile );
			Optional<CompilationUnit> ocu = pr.getResult();
			
			if( ocu.isPresent() ) {
				CompilationUnit cu = ocu.get();
				if (cu.getTypes().isEmpty()) {
					throw new ParseException("The source code of " + myClassSourceFile.getName() + " does not compile.", 48);
				}
				this.className = cu.getTypes().get(0).getNameAsString();
				this.packageName = cu.getPackageDeclaration().isPresent() ? cu.getPackageDeclaration().get().getName().asString() : "";
				this.decl = cu.getClassByName(this.className).get();
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public String getClassName() {
		return className;
	}
	
	public List<FieldDeclaration>  getFields() {
        return decl.getFields();
	}
	
	public List<ConstructorDeclaration>  getConstructors() {
		return decl.getConstructors();
	}
	
	public List<MethodDeclaration>  getMethods() {
		return decl.getMethods();
	}
	
	public String  getSuperClassName() {
		if (decl.getExtendedTypes().size() > 0)
			return decl.getExtendedTypes(0).getNameAsString();
		else return "";
	}

	public String getPackageName() {
		return packageName;
	}
	
	public int getInstanceId() {
		return this.instanceId;
	}
	
	public void resetInstanceId() {
		INSTANCE_ID = 0;
	}
}
