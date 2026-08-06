package com.github.mohamedennahdi.objectmorph.renderer.relation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

import com.github.javaparser.utils.Log;
import com.github.mohamedennahdi.objectmorph.renderer.helper.RendererHelper;
import com.github.mohamedennahdi.objectmorph.renderer.relation.enums.Cardinality;
import com.github.mohamedennahdi.objectmorph.renderer.relation.enums.LinkTypes;

import lombok.Getter;

@Getter
public class Relation {
	String from;
	String to;
	LinkTypes linkType;
	Cardinality cardinality;

	private static int instanceId = 0;

	public static final String GENERALIZATION_PROPERTIES_SCRIPT = RendererHelper.readResource("generalization.properties", Relation.class);

	public static final String ASSOCIATION_PROPERTIES_SCRIPT = RendererHelper.readResource("association.properties", Relation.class);

	public static final String UNARY_PROPERTIES_SCRIPT = RendererHelper.readResource("unary-association.properties", Relation.class);

	public Relation(String from, String to, LinkTypes linkType) {
		super();

		this.from = from;
		this.to = to;
		this.linkType = linkType;
	}

	public Relation(String from, String to, LinkTypes linkType, Cardinality cardinality) {
		this(from, to, linkType);
		this.cardinality = cardinality;
	}

	public static int getInstanceId() {
		return instanceId ++;
	}

	public static void resetInstanceId() {
		instanceId = 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
        }
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Relation)) {
			return false;
		}
		Relation r = (Relation) obj;
		return (this.from.equals(r.getFrom()) && this.to.equals(r.getTo())) || (this.from.equals(r.getTo()) && this.to.equals(r.getFrom())) && this.linkType.equals(r.getLinkType());
	}

	@Override
	public int hashCode() {
		return (this.from + this.to).hashCode();
	}
}
