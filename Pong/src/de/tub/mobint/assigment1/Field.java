package de.tub.mobint.assigment1;

public class Field {
	int top;
	int bottom;
	int left;
	int right;
	
	int width;
	int height;
	
	int horizontalCenter;
	int verticalCenter;
	
	int screenLeft;
	int screenRight;
	
	public Field(int top, int bottom, int left, int right, int screenLeft, int screenRight){
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		this.screenLeft = screenLeft;
		this.screenRight = screenRight;
		
		height = bottom - top;
		width = right - left;
		horizontalCenter = left + width/2;
		verticalCenter = top + height/2;
	}
}
