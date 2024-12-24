package com.github.mohamedennahdi.objectmorph.app.logic;

import java.io.File;

import org.springframework.stereotype.Component;

import com.github.mohamedennahdi.objectmorph.renderer.HTMLGenerator;

@Component
public class ObjectmorphComponent {

	HTMLGenerator htmlGenerator;
	
	private ObjectmorphComponent() {}

	public  HTMLGenerator getHtmlGenerator(File... files) throws Exception {
		htmlGenerator = new HTMLGenerator(files);
		return htmlGenerator;
	}
}
