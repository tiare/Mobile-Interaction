package de.tub.mobint.assigment2;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import processing.core.*;

public class Ball extends Point2D.Float {
	
	private static final long serialVersionUID = 1L;

	PApplet parent;
	
	public int strokeWeight = 10;
	int color = 255;
	
	public boolean out = false;
	public float heading;
	public float bounceSpeedX;
	public float bounceSpeedY;
	public float ballVelX,ballVelY;
	public float ballDirX,ballDirY;
	public float velocity = 160.0f;
	
	public boolean isLost = false;
	public float resetTimeout = 0.8f;
	private float timeout = 0.0f;
	
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
		updateSpeed();
	}
	
	public void draw(){
		updateSpeed();
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
		
		if( isLost ) drawLostBall();
	}
	
	public void explode(){
		isLost = true;
		timeout = resetTimeout;
	}
	
	public void drawLostBall(){
		if (timeout < resetTimeout*0.75f) {
			//8-neighbor
			parent.strokeWeight(strokeWeight);
			parent.stroke(255, 0, 0, 122.0f);
			parent.point(x+5, y);
			parent.point(x-5, y);
			parent.point(x, y+5);
			parent.point(x, y-5);
			parent.point(x+5, y+5);
			parent.point(x+5, y-5);
			parent.point(x-5, y+5);
			parent.point(x-5, y-5);
		}
		
		if (timeout < resetTimeout/2) {
			//4-neighbor
			parent.strokeWeight(strokeWeight);
			parent.stroke(252, 115, 5, 150.0f);
			parent.point(x+5, y);
			parent.point(x-5, y);
			parent.point(x, y+5);
			parent.point(x, y-5);
		}
				
		//center point
		parent.strokeWeight(strokeWeight);
		parent.stroke(252, 197, 5, 200);
		parent.point(x, y);
		
		if (timeout < resetTimeout/4) {
			parent.strokeWeight(strokeWeight);
			parent.stroke(70, 37, 27, 200);
			parent.point(x, y);
		}
	}
	
	public void update(float dT){
		if( !isLost ){
			updateSpeed();
			x += Math.cos(heading)*velocity*dT;
			y += Math.sin(heading)*velocity*dT;
		} else {
			timeout -= dT;
			if(timeout <= 0){
				isLost = false;
			}
		}
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
		heading = -(heading - processing.core.PConstants.PI/2.0f)+ processing.core.PConstants.PI/2.0f;
		float newVelX = -ballVelX + Math.min(bounceSpeedX,800)*0.3f;
		
		if(newVelX*ballVelX>0) {
			velocity *= 0.75f;
			if(velocity<170)
				velocity=170;
		}else{
			ballVelX = newVelX;
			ballVelY+=bounceSpeedY*0.32f;
			velocity=(float) Math.sqrt(ballVelX*ballVelX+ballVelY*ballVelY);
			if (ballVelY>0)	
				heading=(float)Math.acos(ballVelX/velocity);
			else
				heading=-(float)Math.acos(ballVelX/velocity);
		}
		
		lastBounces.addFirst(new Point2D.Float(x, y));
		if( lastBounces.size() > bounceAmount ) lastBounces.removeLast();
		
		bounceSpeedX = 0;
		bounceSpeedY = 0;
	}

	public void updateSpeed() {
		ballDirX = (float)(Math.cos(heading));
		ballDirY = (float)(Math.sin(heading));
		ballVelX = ballDirX*velocity;
		ballVelY = ballDirY*velocity;
	}
	
}
