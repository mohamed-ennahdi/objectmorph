package net.sourceforge.ennahdi.objectmorph.logic;

public class TestClass extends ParentClass {
	private int attribute1;
	protected char attribute2;
	String attribute3;
	public float attribute4;
	boolean attribute5;
	
	protected ParentClass() {
		
	}
	
	public int getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(int attribute1) {
		this.attribute1 = attribute1;
	}
	public char getAttribute2() {
		return attribute2;
	}
	public void setAttribute2(char attribute2) {
		this.attribute2 = attribute2;
	}
	public String getAttribute3() {
		return attribute3;
	}
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	public float getAttribute4() {
		return attribute4;
	}
	public void setAttribute4(float attribute4) {
		this.attribute4 = attribute4;
	}
	public boolean isAttribute5() {
		return attribute5;
	}
	public void setAttribute5(boolean attribute5) {
		this.attribute5 = attribute5;
	}
	
	private String myMethod(double arg1, String arg2) {
		
	}
}
