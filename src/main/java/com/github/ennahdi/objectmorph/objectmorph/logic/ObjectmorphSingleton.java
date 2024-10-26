package com.github.ennahdi.objectmorph.objectmorph.logic;

import java.io.File;

import org.springframework.stereotype.Component;

import net.sourceforge.ennahdi.objectmorph.renderer.HTMLGenerator;

@Component
public class ObjectmorphSingleton {

	static HTMLGenerator INSTANCE;
	
	private ObjectmorphSingleton() {}

	public static HTMLGenerator getHtmlGenerator(File... files) {
		if (INSTANCE == null) {
			INSTANCE = new HTMLGenerator(files);
		}
		return INSTANCE;
	}
}
