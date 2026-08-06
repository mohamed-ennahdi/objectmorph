package com.github.mohamedennahdi.objectmorph.renderer.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RendererHelper {

	private RendererHelper() {}

	public static String readResource(String properties, Class<?> clazz) {
		try (InputStream input = clazz.getResourceAsStream(properties)) {
		    return IOUtils.toString(input, StandardCharsets.UTF_8);
		} catch (IOException e) {
			log.error("", e);
		}
		return "";
	}
}
