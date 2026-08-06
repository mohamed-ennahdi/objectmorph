package com.github.mohamedennahdi.objectmorph.renderer.relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
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
		if (interpreter1 != interpreter2) {
			relations.addAll(updateRelations(fields2, interpreter2, interpreter1));
		}

		return relations;
	}

	private List<Relation> updateRelations(List<FieldDeclaration> fields1, JavaClassInterpreter interpreter1,
			JavaClassInterpreter interpreter2) {
		List<Relation> relations = new ArrayList<>();
		String currentClass = interpreter1.getClassName();
		String targetClass = interpreter2.getClassName();

		for (FieldDeclaration fieldDecl : fields1) {
			for (VariableDeclarator varDecl : fieldDecl.getVariables()) {
				Type type = varDecl.getType();
				boolean relationAdded = false;

				try {
					ResolvedType resolved = type.resolve();

					if (resolved.isTypeVariable()) {
						continue;
					}

					if (resolved.isArray()) {
						ResolvedType componentType = resolved.asArrayType().getComponentType();
						if (componentType.isTypeVariable()) {
							continue;
						}
						if (isSameClass(componentType, targetClass)) {
							addRelation(relations, currentClass, targetClass, LinkTypes.ASSOCIATION,
									Cardinality.ONE_TO_MANY);
							relationAdded = true;
						}
						continue;
					}

					if (resolved.isReferenceType()) {
						ResolvedReferenceType refType = resolved.asReferenceType();

						if (refType.isTypeVariable()) {
							continue;
						}

						if (isSameClass(refType, targetClass)) {
							addRelation(relations, currentClass, targetClass, LinkTypes.ASSOCIATION, null);
							relationAdded = true;
							continue;
						}

						if (isCollectionType(refType) && containsTypeArgument(refType, targetClass)) {
							addRelation(relations, currentClass, targetClass, LinkTypes.ASSOCIATION,
									Cardinality.ONE_TO_MANY);
							relationAdded = true;
						} else if (containsTypeArgument(refType, targetClass)) {
							addRelation(relations, currentClass, targetClass, LinkTypes.ASSOCIATION, null);
							relationAdded = true;
						}
					}
				} catch (Exception e) {
				}

				if (!relationAdded) {
					handleFallback(relations, varDecl, currentClass, targetClass, interpreter1);
				}
			}
		}
		return relations;
	}

	private boolean isListArrayOrCollectionOrMap(JavaClassInterpreter interpreter, VariableDeclarator varDecl) {
		NodeList<ImportDeclaration> imports = interpreter.getImports();
		var childNodes = varDecl.getType().getChildNodes();
		String typeName =  childNodes.isEmpty() ? "" : childNodes.get(0).toString();
		Optional<ImportDeclaration> importDecl = imports.stream()
				.filter(imp -> (imp.getNameAsString() + ";").contains("." + typeName + ";")).findAny();
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

	private boolean isSameClass(ResolvedType type, String className) {
		if (type == null) {
			return false;
		}
		if (type.isTypeVariable()) {
			return false;
		}
		if (type.isReferenceType()) {
			String qualifiedName = type.asReferenceType().getQualifiedName();
			return qualifiedName.equals(className)
					|| qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1).equals(className);
		}
		return false;
	}

	private boolean isCollectionType(ResolvedReferenceType type) {
		String qName = type.getQualifiedName();
		return qName.equals("java.util.Collection") || qName.equals("java.util.List") || qName.equals("java.util.Set")
				|| qName.equals("java.util.Map") || qName.startsWith("java.util."); // naive but works for most
	}

	private boolean containsTypeName(String typeStr, String targetClass) {
	    String cleaned = typeStr.replaceAll("\\s+", "");

	    String escapedTarget = Pattern.quote(targetClass);

	    String regex = "(^|[<,(])" + escapedTarget + "([>,\\<).]|$)";

	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(cleaned);

	    if (matcher.find()) {
	        return true;
	    }

	    String regexWithWildcard = "\\?\\s*(extends|super)\\s+" + escapedTarget;
	    pattern = Pattern.compile(regexWithWildcard);
	    matcher = pattern.matcher(typeStr);

	    return matcher.find();
	}

	private void addRelation(List<Relation> relations, String from, String to, LinkTypes linkType,
			Cardinality cardinality) {
		if (from.equals(to)) {
			relations.add(new Relation(from, to + "Unary", LinkTypes.UNARY));
		} else {
			relations.add(new Relation(from, to, linkType, cardinality));
		}
	}

	private void handleFallback(List<Relation> relations, VariableDeclarator varDecl, String currentClass,
			String targetClass, JavaClassInterpreter interpreter1) {
		String typeStr = varDecl.getTypeAsString();

		if (typeStr.matches("^[A-Z]$")) {
			return;
		}

		String rawType = stripGenericsAndArrays(typeStr);
		if (rawType.isEmpty()) {
			return;
		}

		if (rawType.equalsIgnoreCase(targetClass)) {
			if (typeStr.endsWith("[]") || isListArrayOrCollectionOrMap(interpreter1, varDecl)) {
				addRelation(relations, currentClass, targetClass, LinkTypes.ASSOCIATION, Cardinality.ONE_TO_MANY);
			} else {
				addRelation(relations, currentClass, targetClass, LinkTypes.ASSOCIATION, null);
			}
			return;
		}

		if (isListArrayOrCollectionOrMap(interpreter1, varDecl)) {
			if (containsTypeName(typeStr, targetClass)) {
				addRelation(relations, currentClass, targetClass, LinkTypes.ASSOCIATION, Cardinality.ONE_TO_MANY);
				return;
			}
		}

		if (containsTypeName(typeStr, targetClass)) {
			addRelation(relations, currentClass, targetClass, LinkTypes.ASSOCIATION, null);
		}
	}

	private String stripGenericsAndArrays(String typeStr) {
	    String raw = typeStr.replaceAll("\\[\\]", "");
	    raw = raw.replaceAll("<[^>]*>", "");
	    raw = raw.trim();
	    if (raw.matches("^[A-Z]$")) {
	        return "";
	    }
	    return raw;
	}

	private boolean containsTypeArgument(ResolvedReferenceType refType, String targetClass) {
	    if (refType.isTypeVariable()) {
	        return false;
	    }

	    if (isSameClass(refType, targetClass)) {
	        return true;
	    }

	    List<ResolvedType> typeArgs = refType.typeParametersValues();
	    for (ResolvedType arg : typeArgs) {
	        if (arg.isTypeVariable()) {
	            continue;
	        }

	        if (arg.isReferenceType()) {
	            if (containsTypeArgument(arg.asReferenceType(), targetClass)) {
	                return true;
	            }
	        } else if (arg.isArray()) {
	            ResolvedType comp = arg.asArrayType().getComponentType();
	            if (comp.isTypeVariable()) {
	                continue;
	            }
	            if (comp.isReferenceType() && containsTypeArgument(comp.asReferenceType(), targetClass)) {
	                return true;
	            }
	        } else if (arg.isWildcard()) {
	            ResolvedType bound = arg.asWildcard().getBoundedType();
	            if (bound != null && !bound.isTypeVariable() &&
	                bound.isReferenceType() &&
	                containsTypeArgument(bound.asReferenceType(), targetClass)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
}
