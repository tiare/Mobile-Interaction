package de.tub.mobint.assigment2.paddle;

import java.awt.geom.Point2D;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import de.tub.mobint.assigment2.Area;

import processing.core.PApplet;



public class Paddle extends Point2D.Float{

	Area bounds;
	public float velX,velY;
	
	public PApplet parent;
	public int strokeWeight = 10;
	int color = 255;
	
	public int halfSize = 45;
	
	float heading = 0;
	public int direction;
	
	public static final int LEFT_SIDE = 1;
	public static final int RIGHT_SIDE = 2;
	
	public int side;
	
	public Paddle(PApplet p, float x, float y, int direction,Area bounds){
		super( x, y);
		velX = 0;
		velY = 0;
		parent = p; 
		this.bounds = bounds;
		// dirty, change maybe later
		if( x < 320 ) side = LEFT_SIDE;
		else side = RIGHT_SIDE;
		this.direction = direction;
	}
	
	
	public void draw(){
		parent.strokeWeight(strokeWeight);
		parent.stroke(color);
		parent.line(x,y + halfSize, x, y - halfSize);
	}
	
	public void updateVelocity( float x, float y ){
		float newX = x;
		if( newX > bounds.right) newX = bounds.right;
		if( newX < bounds.left ) newX = bounds.left;
		float newY = y;
		if( newY + halfSize > bounds.bottom ) newY = bounds.bottom - halfSize;
		if( newY - halfSize < bounds.top ) newY = bounds.top + halfSize;
		velX = newX - this.x;
		velY = newY - this.y;
	}
	
	public void updatePosition(float x, float y){
		this.x = x;
		this.y = y;
		velX = 0;
		velY = 0;
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
	
	public Vector3D getLine(float externalOffset){
		float offset = externalOffset;
		// add own offset
		if( externalOffset != 0.0f){
			if( externalOffset > 0.0f )	offset += strokeWeight * 0.5f;
			else						offset -= strokeWeight * 0.5f;
		}
		return Vector3D.crossProduct(	new Vector3D(x + offset, y, 1),
										new Vector3D(x + offset, y+halfSize, 1));
	}


	public void move() {
		this.x += velX;
		this.y += velY;
		
		considerBounds();
	}

}
