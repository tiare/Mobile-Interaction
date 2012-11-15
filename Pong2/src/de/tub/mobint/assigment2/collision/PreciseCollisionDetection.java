package de.tub.mobint.assigment2.collision;

import java.awt.geom.Point2D;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import de.tub.mobint.assigment2.Ball;
import de.tub.mobint.assigment2.Field;
import de.tub.mobint.assigment2.paddle.Paddle;

import processing.core.PApplet;

public class PreciseCollisionDetection extends CollisionDetection {

	PApplet parent;
	
	private static final int HEADING_TOP = 1;
	private static final int HEADING_BOTTOM = 2;
	private static final int HEADING_LEFT = 3;
	private static final int HEADING_RIGHT = 4;
	
	private static final int HEADING_LEFT_OUT = 5;
	private static final int HEADING_RIGHT_OUT = 6;
	
	// vectors representing bounds of the field using homogeneous representation
	Vector3D left;
	Vector3D right;
	Vector3D top;
	Vector3D bottom;
	
	Vector3D lPaddleLine;
	Vector3D rPaddleLine;
	
	Point2D nextHit; // position of next hit
	float remainingTime; // time until next hit
	int heading;
	float halfBallWidth;
	
	public PreciseCollisionDetection(Ball ball, Field field,
			Paddle lPaddle, Paddle rPaddle) {
		super(ball, field, lPaddle, rPaddle);
		
		halfBallWidth = ball.strokeWeight*0.5f;
		
		left = Vector3D.crossProduct(	new Vector3D(field.screenLeft + halfBallWidth, 0, 1),
										new Vector3D(field.screenLeft + halfBallWidth, 1, 1));
		
		right = Vector3D.crossProduct(	new Vector3D(field.screenRight - halfBallWidth, 0, 1),
										new Vector3D(field.screenRight - halfBallWidth, 1, 1));
		
		top = Vector3D.crossProduct(	new Vector3D(0, field.top+halfBallWidth, 1),
										new Vector3D(1, field.top+halfBallWidth, 1));
		
		bottom = Vector3D.crossProduct(	new Vector3D(0, field.bottom - halfBallWidth, 1),
										new Vector3D(1, field.bottom - halfBallWidth, 1));
		
		remainingTime = Float.MAX_VALUE;
	}

	@Override
	public int update(float dT) {
		
		lPaddleLine = lPaddle.getLine(ball.strokeWeight*0.5f);
		rPaddleLine = rPaddle.getLine(-ball.strokeWeight*0.5f);
		detectNextCollision();
		
		while( remainingTime < dT ){
			ball.update(remainingTime);
			dT -= remainingTime;
			int score = bounce();
			
			if( score != 0){
				return score;
			}
			
			detectNextCollision();
		}
		remainingTime -= dT;
		ball.update(dT);
		
		return 0;
	}
	
	private int bounce(){
		
		switch( heading ){
		case HEADING_BOTTOM:
		case HEADING_TOP:
			ball.horizontalBounce();
			break;
			
		case HEADING_LEFT_OUT:
			return -1;
			
		case HEADING_RIGHT_OUT:
			return 1;
			
		case HEADING_LEFT:
			if( lPaddle.inRange(ball) )	ball.verticalBounce();
			break;
			
		case HEADING_RIGHT:
			if( rPaddle.inRange(ball) )	ball.verticalBounce();
			break;
		}
		
		return 0;
		
	}
	
	private void detectNextCollision(){
		remainingTime = Float.MAX_VALUE;
		
		boolean positiveX = Math.cos(ball.heading) > 0;
		boolean positiveY = Math.sin(ball.heading) > 0;
		
		Vector3D path = Vector3D.crossProduct(	new Vector3D(ball.x, ball.y, 1),
						new Vector3D(ball.x + Math.cos(ball.heading),
									ball.y + Math.sin(ball.heading),
									1) );
		
		if( positiveX ){
			if( acceptIfCloser(path, right) ){
				heading = HEADING_RIGHT_OUT;
			}
			
			if( ball.x < rPaddle.x - ball.strokeWeight && acceptIfCloser(path, rPaddleLine) ){
				heading = HEADING_RIGHT;
			}
		} else {
			if( acceptIfCloser(path, left) ){
				heading = HEADING_LEFT_OUT;
			}
			
			if( ball.x > lPaddle.x + ball.strokeWeight && acceptIfCloser(path, lPaddleLine) ){
				heading = HEADING_LEFT;
			}
		}
		
		if( positiveY ){ //  && !(ball.x < field.screenLeft || ball.x > field.screenRight) ){
			if( acceptIfCloser(path, bottom)){
				heading = HEADING_BOTTOM;
			}
		} else {
			if( acceptIfCloser(path, top) ){
				heading = HEADING_TOP;
			}
		}
		
		
	}
	
	private boolean acceptIfCloser(Vector3D path, Vector3D bound){
		// homogeneousIntersection
		Vector3D hi = Vector3D.crossProduct(path, bound);
		Point2D.Float intersection = new Point2D.Float(	(float)(hi.getX() / hi.getZ()),
														(float)(hi.getY() / hi.getZ()));
		float tmpTime = (float) ball.distance(intersection) / ball.velocity;
		if( tmpTime < remainingTime){
			nextHit = intersection;
			remainingTime = tmpTime;
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getName() {
		return "Precise";
	}

	@Override
	public void init() {
		remainingTime = Float.MAX_VALUE;
	}

	public void setPApplet(PApplet parent){
		this.parent = parent;
	}
	
}
