package de.tub.mobint.assigment2;

public class Field extends Area{
	
	
	public int width;
	public int height;
	
	public int horizontalCenter;
	public int verticalCenter;
	
	public int screenLeft;
	public int screenRight;
	
	public Field(int top, int bottom, int left, int right, int screenLeft, int screenRight){
		super(top, bottom, left, right);
		
		this.screenLeft = screenLeft;
		this.screenRight = screenRight;
		
		height = bottom - top;
		width = right - left;
		horizontalCenter = left + width/2;
		verticalCenter = top + height/2;
	}
	
	public Area getLeftArea(int centerSpacing){
		return new Area(top, bottom, left, horizontalCenter - centerSpacing);
	}
	
	public Area getRightArea(int centerSpacing){
		return new Area(top, bottom, horizontalCenter + centerSpacing, right);
	}
}
