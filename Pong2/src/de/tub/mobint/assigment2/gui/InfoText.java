package de.tub.mobint.assigment2.gui;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class InfoText {
	PApplet parent;
	String text;
	int color;
	public int size = 15;
	public int timeout = 50;
	public int countdown = 0;
	Point2D.Float pos;
	public int align = PApplet.LEFT;
	
	public InfoText( PApplet parent, int color, Point2D.Float pos ) {
		this.parent = parent;
		this.color = color;
		
		this.pos = pos;
	}
	
	public void draw(){
		
		if( countdown < 0 || countdown > 0){
			parent.textSize(size);
			parent.textAlign(align);
			parent.fill(color);
			parent.text(text, pos.x, pos.y);
			
			if( countdown > 0) countdown--;
		}
	}
	
	public void setText(String text){
		this.text = text;
		
		countdown = timeout;
	}

}
