package com.github.mohamedennahdi.objectmorph.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaClassInterpreter {

	
	private ClassOrInterfaceDeclaration decl;
	private EnumDeclaration enumDecl;
	
	private String className;
	private String packageName;
	private NodeList<ImportDeclaration> imports;
	
	private static int instanceId = 0;
	
	public JavaClassInterpreter(File myClassSourceFile) throws FileNotFoundException, ParseException {
		
		JavaParser parser = new JavaParser();
		
		ParseResult<CompilationUnit> pr;
		try {
			pr = parser.parse( myClassSourceFile );
			Optional<CompilationUnit> ocu = pr.getResult();
			
			if( ocu.isPresent() ) {
				CompilationUnit cu = ocu.get();
				this.imports = cu.getImports();
				if (cu.getTypes().isEmpty()) {
					throw new ParseException("The source code of " + myClassSourceFile.getName() + " does not compile.", 48);
				}
				this.className = cu.getTypes().get(0).getNameAsString();
				this.packageName = cu.getPackageDeclaration().isPresent() ? cu.getPackageDeclaration().get().getName().asString() : "";
				Optional<ClassOrInterfaceDeclaration> optional = cu.getClassByName(this.className); 
				if (optional.isPresent()) {
					this.decl = optional.get();
				} else {
					optional = cu.getInterfaceByName(this.className);
					if (optional.isPresent()) {
						this.decl = optional.get();
					} else {
						Optional<EnumDeclaration> enumOptional = cu.getEnumByName(this.className);
						if (enumOptional.isPresent()) {
							this.enumDecl = enumOptional.get();
						} else {
							throw new UnsupportedOperationException();
						}
					}
				}
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
		if (Objects.isNull(this.decl)) {
			return new ArrayList<>();
		} else {
			return decl.getFields();
		}
	}
	
	public NodeList<EnumConstantDeclaration>  getEntries() {
		if (Objects.isNull(this.enumDecl)) {
			return new NodeList<>();
		} else {
			return enumDecl.getEntries();
		}
	}
	
	public List<ConstructorDeclaration>  getConstructors() {
		if (Objects.isNull(this.decl)) {
			return new ArrayList<>();
		}
		return decl.getConstructors();
	}
	
	public List<MethodDeclaration>  getMethods() {
		if (Objects.isNull(this.decl)) {
			return new ArrayList<>();
		}
		return decl.getMethods();
	}
	
	public String getSuperClassName() {
		if (Objects.isNull(this.decl)) {
			return "";
		}
		if (decl.getExtendedTypes().isNonEmpty())
			return decl.getExtendedTypes(0).getNameAsString();
		else return "";
	}
	
	public boolean isInterface() {
		if (Objects.isNull(decl)) {
			return false;
		}
		return decl.isInterface();
	}
	
	public boolean isEnum() {
		if (Objects.isNull(enumDecl)) {
			return false;
		}
		return enumDecl.isEnumDeclaration();
	}

	public String getPackageName() {
		return packageName;
	}
	
	public NodeList<ImportDeclaration> getImports() {
		return this.imports;
	}
	
	public static int getInstanceId() {
		return instanceId ++;
	}
	
	public static void resetInstanceId() {
		instanceId = 0;
	}
}
