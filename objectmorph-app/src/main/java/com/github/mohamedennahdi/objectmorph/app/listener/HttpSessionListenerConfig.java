package com.github.mohamedennahdi.objectmorph.app.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class HttpSessionListenerConfig implements HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log.info("Session created: " + se.getSession().getId());
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.info("Session destroyed: " + se.getSession().getId());
		String sessionId = se.getSession().getId();
		String path = System.getProperty("user.home");
        String sessionFolder = path + File.separator + sessionId + File.separator;
        
        try {
			Files.walk(Paths.get(sessionFolder))
				 .sorted(Comparator.reverseOrder())
				 .map(Path::toFile)
				 .forEach(File::delete);
		} catch (IOException e) {
			log.error("Error removing files", e);
		}  
	}
}
