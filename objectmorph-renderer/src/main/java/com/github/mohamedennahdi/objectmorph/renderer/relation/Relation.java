package com.github.mohamedennahdi.objectmorph.renderer.relation;

import com.github.mohamedennahdi.objectmorph.renderer.relation.enums.LinkTypes;

public class Relation {
	String from;
	String to;
	LinkTypes linkType;
	
	private int instanceId;
	
	private static int INSTANCE_ID = 0;
	
	public static final String GENERALIZATION_PROPERTIES_SCRIPT =	" {" +
			"endPlug: 'arrow3'," +
			"path: 'straight'," +
			"color: 'black'," +
			"endPlugColor: 'white',"+
			"endPlugOutline: true,"+
			"outline: true,"+
			"outlineColor: 'black',"+
			"endPlugSize: 5" +
			"});";

	public static final String ASSOCIATION_PROPERTIES_SCRIPT =	" {" +
		  	"endPlug: 'behind'," +
			"path: 'straight',"+
			"color: 'black'," +
			"outline: true," +
			"outlineColor: 'black',"+
		"});";
	
	public Relation(String from, String to, LinkTypes linkType) {
		super();
		INSTANCE_ID ++;
		
		this.instanceId = INSTANCE_ID;
		
		this.from = from;
		this.to = to;
		this.linkType = linkType;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public LinkTypes getLinkType() {
		return linkType;
	}

	public void setLinkType(LinkTypes linkType) {
		this.linkType = linkType;
	}
	
	public int getInstanceId() {
		return instanceId;
	}
}
