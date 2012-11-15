package de.tub.mobint.assigment2;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import processing.core.*;

public class Ball extends Point2D.Float {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	PApplet parent;
	
	public int strokeWeight = 10;
	int color = 255;
	
	public boolean out = false;
	public float heading;
	
	public float velocity = 160.0f;
	
	LinkedList<Point2D.Float> tail;
	int tailLength = 5;
	
	public LinkedList<Point2D.Float> lastBounces;
	int bounceAmount = 3;
	
	public Ball(PApplet p){
		super(0,0);
		parent = p;
		heading = processing.core.PConstants.PI/4.0f;
		
		tail = new LinkedList<Point2D.Float>();
		lastBounces = new LinkedList<Point2D.Float>();
		
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
		
		transparency = 122.0f;
		step = 122.0f / (tail.size()+1.0f);
		for( Point2D.Float lb : lastBounces){
			transparency -= step;
			parent.stroke(255,0,0,transparency);
			parent.point(lb.x, lb.y);
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
		
		lastBounces.addFirst(new Point2D.Float(x, y));
		if( lastBounces.size() > bounceAmount ) lastBounces.removeLast();
		
	}
	
	public void verticalBounce(){
		heading = -(heading - processing.core.PConstants.PI/2.0f)
				+ processing.core.PConstants.PI/2.0f;
		
		lastBounces.addFirst(new Point2D.Float(x, y));
		if( lastBounces.size() > bounceAmount ) lastBounces.removeLast();
	}
	
}
