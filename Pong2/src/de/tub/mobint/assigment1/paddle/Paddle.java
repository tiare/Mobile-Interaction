package de.tub.mobint.assigment1.paddle;

import java.awt.geom.Point2D;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import de.tub.mobint.assigment1.Area;

import processing.core.PApplet;



public class Paddle extends Point2D.Float{

	Area bounds;
	
	PApplet parent;
	int strokeWeight = 10;
	int color = 255;
	
	int halfSize = 34;
	
	float heading = 0;
	
	public Paddle(PApplet p, float x, float y, Area bounds){
		super( x, y);
		parent = p; 
		this.bounds = bounds;
	}
	
	
	public void draw(){
		parent.strokeWeight(strokeWeight);
		parent.stroke(color);
		parent.line(x,y + halfSize, x, y - halfSize);
	}
	
	public void updatePosition(float x, float y){
		this.x = x;
		this.y = y;
		
		considerBounds();
	}
	
	private void considerBounds(){
		if( y + halfSize > bounds.bottom ) y = bounds.bottom - halfSize;
		if( y - halfSize < bounds.top ) y = bounds.top + halfSize;
		if( x > bounds.right) x = bounds.right;
		if( x < bounds.left ) x = bounds.left;
	}
	
	public boolean inRange(Point2D.Float possibleHit){
		return distance(possibleHit) <= halfSize;
	}
	
	public Vector3D getLine(float offset){
		
		// add own offset
		if( offset != 0.0f){
			if( offset > 0.0f )	offset += strokeWeight * 0.5f;
			else				offset -= strokeWeight * 0.5f;
		}
		return Vector3D.crossProduct(	new Vector3D(x + offset, y, 1),
										new Vector3D(x + offset, y+halfSize, 1));
	}

}
