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
	
	public static final String UNARY_PROPERTIES_SCRIPT =	" {" +
			"startSocket: 'right'," +
			"endSocket: 'top'," +
			"path: 'grid'," +
			"startSocketGravity: 100," +
			"endSocketGravity: 50," +
			"color: 'red'," +
			"endPlug: 'behind'" +
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
	
	public void resetInstanceId() {
		INSTANCE_ID = 0;
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
