package de.tub.mobint.assignment3;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class InfoText {
	PApplet parent;
	String text;
	int color;
	public int size = 15;
	public int timeout = 50;
	public int countdown = 0;
	int x;
	int y;
	public int align = PApplet.LEFT;
	
	public InfoText( PApplet parent, int color, int x, int y ) {
		this.parent = parent;
		this.color = color;
		
		this.x=x;
		this.y=y;
	}
	
	public void draw(){
		
		if( countdown < 0 || countdown > 0){
			parent.textSize(size);
			parent.textAlign(align);
			parent.fill(color);
			parent.text(text, x, y);
			
			if( countdown > 0) countdown--;
		}
	}
	
	public void setText(String text){
		this.text = text;
		
		countdown = timeout;
	}

}
