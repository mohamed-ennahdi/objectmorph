package com.github.mohamedennahdi.objectmorph.renderer.helper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RendererHelper {

	private RendererHelper() {}

	public static String readResource(String properties, Class<?> clazz) {
		try {
			return Files.readString(Paths.get(clazz.getResource(properties).toURI()), StandardCharsets.UTF_8);
		} catch (IOException | URISyntaxException e) {
			log.error("", e);
		}
		return "";
	}
}
