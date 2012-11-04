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
	
	public Field(int top, int bottom, int left, int right){
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
		height = bottom - top;
		width = right - left;
		horizontalCenter = left + width/2;
		verticalCenter = top + height/2;
	}
}
