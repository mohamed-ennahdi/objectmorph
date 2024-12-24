package com.github.mohamedennahdi.objectmorph.renderer;

import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.script;
import static j2html.TagCreator.title;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.mohamedennahdi.objectmorph.logic.JavaClassInterpreter;
import com.github.mohamedennahdi.objectmorph.renderer.relation.Relation;
import com.github.mohamedennahdi.objectmorph.renderer.relation.RelationRenderer;

import j2html.tags.specialized.TableTag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HTMLGenerator {
	
	List<JavaClassInterpreter> interpreters = new ArrayList<>();
	List<TableTag> tables = new ArrayList<>();
	
	public HTMLGenerator(File... sourceCodeClasses) throws Exception {
		for (File sourceCodeClass : sourceCodeClasses) {
			HTMLIndividualGenerator generator;
			try {
				generator = new HTMLIndividualGenerator(sourceCodeClass);
				tables.add(generator.generateFullClassHTML());
				JavaClassInterpreter interpreter = generator.getInterpreter();
				interpreters.add(interpreter);
			} catch (URISyntaxException | FileNotFoundException | ParseException e) {
				log.error(e.getMessage(), e);
				throw e;
			}
		}
	}
	
	public String generateFullHTML() throws FileNotFoundException, URISyntaxException {		
		String draggableScript = "";		
		for (JavaClassInterpreter interpreter : interpreters) {
			draggableScript += "\n var draggable" + interpreter.getInstanceId() +" = new PlainDraggable(document.getElementById('"+ interpreter.getClassName() +"'), {onMove: fixLine});";
		}
		
		List<Relation> relations = new ArrayList<>();
		
		RelationRenderer rr = new RelationRenderer(interpreters);
		
		relations.addAll(rr.getGeneralizationRelations());
		relations.addAll(rr.getAssociationRelations());
		relations.addAll(rr.getRecursiveRelations());
		
		Set<Relation> set = new HashSet<>(relations);
		relations = new ArrayList<>(set);
		
		
		String lineScript = "";
		String fixeLineScript = "function fixLine() {";
		for (Relation relation : relations) {
			String varName = "line" + relation.getInstanceId();
			lineScript += 	"\n var " + varName + " = new LeaderLine(document.getElementById('" + relation.getFrom() + "'), document.getElementById('" + relation.getTo() + "'),  " + 
					(switch (relation.getLinkType()) {
						case GENERALIZATION -> Relation.GENERALIZATION_PROPERTIES_SCRIPT;
						case ASSOCIATION -> Relation.ASSOCIATION_PROPERTIES_SCRIPT;
						case UNARY -> Relation.UNARY_PROPERTIES_SCRIPT;
						default -> throw new IllegalArgumentException("Unexpected value: " + relation.getLinkType());
					});
			fixeLineScript += varName + ".position(); ";
		}
		fixeLineScript += " } ";
		
		if (!relations.isEmpty()) {
			relations.get(0).resetInstanceId();
		}
		if (!interpreters.isEmpty()) {
			interpreters.get(0).resetInstanceId();
		}
				
		return html(
				head(
						script().withSrc("https://cdn.jsdelivr.net/npm/plain-draggable@2.5.12/plain-draggable.min.js"),
						script().withSrc("https://cdn.jsdelivr.net/npm/leader-line-new@1.1.9/leader-line.min.js"),
						title("Class Diagram")
					),
				body(
						div(
								tables.toArray(new TableTag[0])
						).withStyle("background-color: gray; margin:0 auto; width: 1080px; height: 768px; border:1px solid black;"),
						script(
								draggableScript + " \n" + lineScript + "\n " + fixeLineScript
							)
				).withStyle("font-family: consolas")
			).renderFormatted();
	}
}
