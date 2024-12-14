package com.github.mohamedennahdi.objectmorph.renderer.relation;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import com.github.mohamedennahdi.objectmorph.logic.JavaClassInterpreter;
import com.github.mohamedennahdi.objectmorph.renderer.relation.enums.LinkTypes;

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
			VariableDeclarator varDecl = fieldDeclaration.getVariables().get(0);
			if (varDecl.getTypeAsString().equalsIgnoreCase(interpreter2.getClassName())) {
				if (interpreter1.getClassName().equals(interpreter2.getClassName())) {
					relations.add(new Relation(interpreter1.getClassName(), interpreter2.getClassName() + "Unary", LinkTypes.UNARY));
				} else {
					relations.add(new Relation(interpreter1.getClassName(), interpreter2.getClassName(), LinkTypes.ASSOCIATION));
				}
			}
		}
		return relations;
	}
}
