package de.tub.mobint.assigment1;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import processing.core.*;

public class Ball extends Point2D.Float {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	PApplet parent;
	
	int strokeWeight = 10;
	int color = 255;
	
	float heading;
	
	float velocity = 160.0f;
	
	LinkedList<Point2D.Float> tail;
	int tailLength = 5;
	
	Point2D.Float lastBounce;
	
	public Ball(PApplet p){
		super(0,0);
		parent = p;
		heading = processing.core.PConstants.PI/4.0f;
		
		tail = new LinkedList<Point2D.Float>();
		lastBounce = new Point2D.Float(-1, -1);
		
	}
	
	public void draw(){
		parent.strokeWeight(strokeWeight);
		parent.stroke(color);
		parent.point(x, y);
		
		tail.addFirst(new Point2D.Float(x, y));
		if( tail.size() > tailLength ) tail.removeLast();
		
		float transparency = 255.0f;
		float step = 255.0f / (tail.size()+1.0f); 
		for( Point2D.Float p : tail){
			transparency -= step;
			parent.stroke(color,transparency);
			parent.point(p.x, p.y);
		}
		
		if( lastBounce.x >= 0){
			parent.stroke(255,0,0,122);
			parent.point(lastBounce.x, lastBounce.y);
		}
	}
	
	public void update(float dT){
		x += Math.cos(heading)*velocity*dT;
		y += Math.sin(heading)*velocity*dT;
	}
	
	public int bottom(){
		return (int)Math.ceil(y) + strokeWeight / 2;
	}
	
	public int top(){
		return (int)Math.floor(y) - strokeWeight / 2;
	}
	
	public int left(){
		return (int)Math.ceil(x) - strokeWeight / 2;
	}
	
	public int right(){
		return (int)Math.floor(x) + strokeWeight / 2;
	}
	
	
	public void horizontalBounce(){
		heading *= -1;
		lastBounce.setLocation(x, y);
	}
	
	public void verticalBounce(){
		heading = (heading - processing.core.PConstants.PI/2.0f) * -1.0f
				+ processing.core.PConstants.PI/2.0f;
		lastBounce.setLocation(x, y);
	}
	
}
