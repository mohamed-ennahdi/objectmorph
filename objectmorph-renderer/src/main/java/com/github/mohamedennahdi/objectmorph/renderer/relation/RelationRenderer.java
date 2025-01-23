package com.github.mohamedennahdi.objectmorph.renderer.relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import com.github.mohamedennahdi.objectmorph.logic.JavaClassInterpreter;
import com.github.mohamedennahdi.objectmorph.renderer.relation.enums.Cardinality;
import com.github.mohamedennahdi.objectmorph.renderer.relation.enums.LinkTypes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RelationRenderer {
	List<JavaClassInterpreter> interpreters;
	
	public RelationRenderer(List<JavaClassInterpreter> interpreters) {
		this.interpreters = interpreters;
	}
	
	public  List<Relation> getGeneralizationRelations() {
		List<Relation> relations = new ArrayList<>();
		if (interpreters.size() > 1) {
			for (int i = 0; i < interpreters.size() - 1; i ++) {
				JavaClassInterpreter interpreter1 = interpreters.get(i);
				for (int j = 1; j < interpreters.size(); j ++) {
					JavaClassInterpreter interpreter2 = interpreters.get(j);
					if (interpreter1.getSuperClassName().equals(interpreter2.getClassName())) {
						relations.add(new Relation(interpreter1.getClassName(), interpreter2.getClassName(), LinkTypes.GENERALIZATION));
					} else if (interpreter2.getSuperClassName().equals(interpreter1.getClassName())) {
						relations.add(new Relation(interpreter2.getClassName(), interpreter1.getClassName(), LinkTypes.GENERALIZATION));
					}
				}
			}
		}
		return relations;
	}
	
	public List<Relation> getAssociationRelations() {
		List<Relation> relations = new ArrayList<>();
		if (interpreters.size() > 1) {
			for (int i = 0; i < interpreters.size() - 1; i ++) {
				JavaClassInterpreter interpreter1 = interpreters.get(i);
				for (int j = i + 1; j < interpreters.size(); j ++) {
					JavaClassInterpreter interpreter2 = interpreters.get(j);
					relations.addAll(establishRelation(interpreter1, interpreter2));
				}
			}
		}
		return relations;
	}
	
	public List<Relation> getRecursiveRelations() {
		List<Relation> relations = new ArrayList<>();
		for (int i = 0; i < interpreters.size(); i ++) {
			JavaClassInterpreter interpreter = interpreters.get(i);
			relations.addAll(establishRelation(interpreter, interpreter));
		}
		return relations;
	}
	
	private List<Relation> establishRelation(JavaClassInterpreter interpreter1, JavaClassInterpreter interpreter2) {
		
		List<Relation> relations = new ArrayList<>();
		
		List<FieldDeclaration> fields1 = interpreter1.getFields();
		List<FieldDeclaration> fields2 = interpreter2.getFields();
		
		relations.addAll(updateRelations(fields1, interpreter1, interpreter2));
		relations.addAll(updateRelations(fields2, interpreter2, interpreter1));
		
		return relations;
	}
	
	private List<Relation> updateRelations(List<FieldDeclaration> fields1, JavaClassInterpreter interpreter1, JavaClassInterpreter interpreter2) {
		List<Relation> relations = new ArrayList<>();
		for (FieldDeclaration fieldDeclaration : fields1) {
			for (VariableDeclarator varDecl : fieldDeclaration.getVariables()) {
				if (varDecl.getTypeAsString().equalsIgnoreCase(interpreter2.getClassName())) {
					if (interpreter1.getClassName().equals(interpreter2.getClassName())) {
						relations.add(new Relation(interpreter1.getClassName(), interpreter2.getClassName() + "Unary", LinkTypes.UNARY));
					} else {
						relations.add(new Relation(interpreter1.getClassName(), interpreter2.getClassName(), LinkTypes.ASSOCIATION));
					}
				} else {
					if ((varDecl.getTypeAsString().contains("<" + interpreter2.getClassName() + ">") && isLisArrayOrCollectionOrMap(interpreter1, varDecl))) {
						if (interpreter1.getClassName().equals(interpreter2.getClassName())) {
							relations.add(new Relation(interpreter1.getClassName(), interpreter2.getClassName() + "Unary", LinkTypes.UNARY));
						} else {
							relations.add(new Relation(interpreter1.getClassName(), interpreter2.getClassName(), LinkTypes.ASSOCIATION, Cardinality.ONE_TO_MANY));
						}
					} else if (varDecl.getTypeAsString().equalsIgnoreCase(interpreter2.getClassName()+"[]") && varDecl.getTypeAsString().contains("[]")) {
						relations.add(new Relation(interpreter1.getClassName(), interpreter2.getClassName(), LinkTypes.ASSOCIATION, Cardinality.ONE_TO_MANY));
					}
				}
			}
		}
		return relations;
	}
	
	private boolean isLisArrayOrCollectionOrMap(JavaClassInterpreter interpreter, VariableDeclarator varDecl) {
		NodeList<ImportDeclaration> imports = interpreter.getImports();
		String typeName = varDecl.getType().getChildNodes().get(0).toString();
		Optional<ImportDeclaration> importDecl = imports.stream()
														.filter(imp -> (imp.getNameAsString() + ";").contains("." + typeName + ";"))
														.findAny();
		if (importDecl.isPresent()) {
			try {
				Class<?> cls = Class.forName(importDecl.get().getNameAsString());
				boolean isCollection = Collection.class.isAssignableFrom(cls);
				boolean isMap = Map.class.isAssignableFrom(cls);
				return isCollection || isMap;
			} catch (ClassNotFoundException e) {
				log.warn(e.getMessage());
			}
		}
		return false;
	}
}
