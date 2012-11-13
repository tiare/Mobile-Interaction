package de.tub.mobint.assigment1.paddle;

import java.awt.geom.Point2D;

import processing.core.*;


public class VerticalPaddle extends Point2D.Float{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PApplet parent;
	int strokeWeight = 10;
	int color = 255;
	
	int halfSize = 34;
	
	int topBound = 0;
	int bottomBound = 480;
	
	float velocity = 120.0f;
	public int movement = 0;
	
	public VerticalPaddle(PApplet p, float x, float y){
		super( x, y);
		parent = p; 
	}
	
	public void update(float dT){
		if( movement != 0 ){
			y += velocity * movement * dT;
			if( y - halfSize < topBound ) y = topBound + halfSize;
			if( y + halfSize > bottomBound ) y = bottomBound - halfSize;
		}
	}
	
	public void draw(){
		parent.strokeWeight(strokeWeight);
		parent.stroke(color);
		parent.line(x,y + halfSize, x, y - halfSize);
	}
	
	public void setBounds(int top, int bottom){
		topBound = top;
		bottomBound = bottom;
	}
	
	public void setMovement(int move){
		movement = move;
	}
	
	public boolean inRange(float y){
		return y < this.y + halfSize && y > this.y - halfSize;
	}
	
	public int bottom(){
		return (int)Math.ceil(y) + halfSize;
	}
	
	public int top(){
		return (int)Math.floor(y) - halfSize;
	}
	
	public int left(){
		return (int)Math.ceil(x) - strokeWeight / 2;
	}
	
	public int right(){
		return (int)Math.floor(x) + strokeWeight / 2;
	}
	
	public float getPosition() {
		return y;
	}
}
