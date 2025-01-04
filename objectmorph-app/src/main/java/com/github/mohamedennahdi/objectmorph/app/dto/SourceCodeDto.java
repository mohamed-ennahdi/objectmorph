package com.github.mohamedennahdi.objectmorph.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SourceCodeDto {
	@Schema(example= "Test.java", description = "File name of the source code")
	private String filename;
	@Schema(example= "class Test { private String attribute1; }", description = "Source code")
	private String sourceCode;
}
