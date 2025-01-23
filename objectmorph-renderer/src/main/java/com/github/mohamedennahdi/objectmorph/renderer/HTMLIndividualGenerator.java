package com.github.mohamedennahdi.objectmorph.renderer;

import static j2html.TagCreator.b;
import static j2html.TagCreator.each;
import static j2html.TagCreator.img;
import static j2html.TagCreator.table;
import static j2html.TagCreator.td;
import static j2html.TagCreator.tr;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import j2html.tags.specialized.TableTag;
import j2html.tags.specialized.TrTag;
import com.github.mohamedennahdi.objectmorph.logic.JavaClassInterpreter;

public class HTMLIndividualGenerator {
	
	private JavaClassInterpreter interpreter;
	private static final String ECLIPSE_PATH = "https://help.eclipse.org/latest/topic/org.eclipse.jdt.doc.user/images/org.eclipse.jdt.ui";
	private static final String RESOURCES_PATH = ECLIPSE_PATH  + "/obj16";
	private static final String RESOURCES_PATH_SPEC = ECLIPSE_PATH + "/ovr16";
	
	public HTMLIndividualGenerator(File srcCodeJavaClass) throws FileNotFoundException, ParseException {
		interpreter = new JavaClassInterpreter(srcCodeJavaClass);
	}
	
	private TrTag generateClassNameHTML() {
		return tr(
				td(
						img().withSrc(RESOURCES_PATH + (interpreter.isInterface() ? "/int_obj.svg" : interpreter.isEnum() ? "/enum_obj.svg" : "/class_obj.svg")).withStyle("top: 4px;position: relative;"),
						b(this.interpreter.getClassName())
					).attr("valign", "top").attr("align", "center")
				
		);
	}
	
	private TrTag generatePackageNameHTML() {
		return tr(
				td(this.interpreter.getPackageName()).withStyle("background-color: lightyellow;").attr("align", "center")
				
		);
	}
	
	private TableTag generateClassHeaderHTML() {
		return table(
					generateClassNameHTML(),
					generatePackageNameHTML()
		).withStyle("width: 100%; font-size: small");
	}
	
	private TableTag generateAttributesHTML() {
		List<FieldDeclaration> fields = interpreter.getFields();
		NodeList<EnumConstantDeclaration> nodes = interpreter.getEntries();
		return table(
				each(fields, field -> tr(
											td(
													img().withSrc(RESOURCES_PATH + "/field_" + ("none".equalsIgnoreCase(field.getAccessSpecifier().name()) ? "default" : field.getAccessSpecifier().name().toLowerCase() )  + "_obj.svg")
												).withStyle("width: 6%"),
											td(
													field.getVariables().stream().map(v ->  v.getName().asString()).collect(Collectors.joining(", ")) + ": " + field.getVariable(0).getTypeAsString()
											)
						)),
				each(nodes, field -> tr(
						td(
								img().withSrc(RESOURCES_PATH + "/field_public_obj.svg"),
								img().withSrc(RESOURCES_PATH_SPEC + "/static_co.svg").withStyle("position: relative; left: -17px; top: -8px;"),
								img().withSrc(RESOURCES_PATH_SPEC + "/final_co.svg").withStyle("position: relative; left: -26px; top: -8px;")
						),
						td(
								
								field.toString()
						)))
				).withStyle("font-size: small");
	}
	
	private TableTag generateConstructorsHTML() {
		List<ConstructorDeclaration> constructors = this.interpreter.getConstructors();
		return table(
				each(constructors, constructor -> tr(
											td(
													img().withSrc(RESOURCES_PATH + "/meth" + ("none".equalsIgnoreCase(constructor.getAccessSpecifier().name()) ? "def" : constructor.getAccessSpecifier().name().toLowerCase().substring(0, 3)) + "_obj.svg"),
													img().withSrc(RESOURCES_PATH_SPEC + "/constr_ovr.svg").withStyle("position: relative; left: -13px; top: -4px;")
													),
											td( constructor.getName().asString() + "(" + constructor.getParameters().stream().map(p -> p.getTypeAsString()).collect(Collectors.joining(",")) + ")")
										)
				)
		).withStyle("; font-size: small");
	}
	
	private TableTag generateMethodsHTML() {
		List<MethodDeclaration> methods = this.interpreter.getMethods();
		return table(
				each(methods, method -> tr(
						td(
								img().withSrc(RESOURCES_PATH + "/meth" + ("none".equalsIgnoreCase(method.getAccessSpecifier().name()) ? "def" : method.getAccessSpecifier().name().toLowerCase().substring(0, 3) )  + "_obj.svg")
							),
						td( 
								method.getName().asString() + "(" + method.getParameters().stream().map(p -> p.getTypeAsString()).collect(Collectors.joining(",")) + "): " + method.getTypeAsString()  )
							)
					)
				).withStyle("; font-size: small");
	}
	
	public TableTag generateFullClassHTML() {
		return
				table(
					tr(
							td(
									this.generateClassHeaderHTML()
								)
							).withId(this.interpreter.getClassName() + "Unary")
							 .withStyle("outline: thin solid"),
					tr(
							td(
									this.generateAttributesHTML()
								)
							).withStyle("outline: thin solid"),
					tr(
							td(
									this.generateConstructorsHTML()
								)
							).withStyle("outline: thin solid; display: " + (interpreter.getConstructors().isEmpty() ? "none" : "visible")),
					tr(
							td(
									this.generateMethodsHTML()
								)
							).withStyle("outline: thin solid")
					)
				.withStyle("background-color: white;width: 20%; font-size: small").attr("cellspacing", "0").withId(this.interpreter.getClassName());
	}

	public JavaClassInterpreter getInterpreter() {
		return interpreter;
	}
}
