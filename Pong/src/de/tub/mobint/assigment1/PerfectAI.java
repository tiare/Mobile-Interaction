package de.tub.mobint.assigment1;

import java.awt.geom.Point2D;
import java.util.LinkedList;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class PerfectAI extends ArtificialIntelligence{

	Point2D requiredPaddlePos; // position of next hit on own side
	Point2D hitPoint; 
	Ball fakeBall;
	float remainingTime; // time until next hit on own paddle
	VerticalPaddle ownPaddle;
	
	private static final int HEADING_TOP = 1;
	private static final int HEADING_BOTTOM = 2;
	private static final int HEADING_LEFT = 3;
	private static final int HEADING_RIGHT = 4;
	int heading;
	// vectors representing bounds of the field using homogeneous representation
	Vector3D left;
	Vector3D right;
	Vector3D top;
	Vector3D bottom;
	float halfBallWidth;
	
	public PerfectAI(Ball ball, Field field, VerticalPaddle ownPaddle) {
		super(ball, field, ownPaddle);
		this.ownPaddle = ownPaddle;
		
		
		halfBallWidth = ball.strokeWeight/2.0f;
		
		left = Vector3D.crossProduct( 	new Vector3D(field.left + halfBallWidth, field.verticalCenter, 1),
										new Vector3D(field.left + halfBallWidth, field.verticalCenter+1, 1));
		
		right = Vector3D.crossProduct( 	new Vector3D(field.right - halfBallWidth, field.verticalCenter, 1),
										new Vector3D(field.right - halfBallWidth, field.verticalCenter+1, 1));
		
		top = Vector3D.crossProduct(	new Vector3D(0, field.top+halfBallWidth, 1),
										new Vector3D(1, field.top+halfBallWidth, 1));
		
		bottom = Vector3D.crossProduct(	new Vector3D(0, field.bottom - halfBallWidth, 1),
										new Vector3D(1, field.bottom - halfBallWidth, 1));
		
		remainingTime = Float.MAX_VALUE;
		smoothingFactor = 10;
		fakeBall = (Ball) ball.clone();
		fakeBall.lastBounces = new LinkedList<Point2D.Float>();
		hitPoint = new Point2D.Float(0,0);
		requiredPaddlePos = new Point2D.Float(ownPaddle.x, ownPaddle.y);
	}

	@Override
	public void update(float dT) {
//		fakeBall.draw();
		fakeBall.setLocation(ball);
		fakeBall.heading = ball.heading;
		//Find the goal position to deflect ball
		detectNextCollision();

		if( paddle.y < requiredPaddlePos.getY() - smoothingFactor) paddle.setMovement(1);
		else if( paddle.y > requiredPaddlePos.getY() + smoothingFactor) paddle.setMovement(-1);
		else paddle.setMovement(0);
		
	}
	
	private void detectNextCollision(){
		if(ball.out) return;
		remainingTime = Float.MAX_VALUE;
		
		boolean positiveX = Math.cos(fakeBall.heading) > 0;
		boolean positiveY = Math.sin(fakeBall.heading) > 0;
		
		Vector3D path = Vector3D.crossProduct(	new Vector3D(fakeBall.x, fakeBall.y, 1),
						new Vector3D(fakeBall.x + Math.cos(fakeBall.heading),
								fakeBall.y + Math.sin(fakeBall.heading),
									1) );
		
		if( positiveX ){
			if( acceptIfCloser(path, right) ){
				heading = HEADING_RIGHT;
			}
		} else {
			if( acceptIfCloser(path, left) ){
				heading = HEADING_LEFT;
			}
		}
		
		if( positiveY ){
			if( acceptIfCloser(path, bottom) ){
				heading = HEADING_BOTTOM;
			}
		} else {
			if( acceptIfCloser(path, top) ){
				heading = HEADING_TOP;
			}
		}
		if (heading == HEADING_TOP || heading == HEADING_BOTTOM)
			fakeBall.horizontalBounce();
		else
			fakeBall.verticalBounce();
		
		fakeBall.x = (float)hitPoint.getX();
		fakeBall.y = (float)hitPoint.getY();
		
		if((ownPaddle.x < field.horizontalCenter && //left paddle and
				fakeBall.x <= ownPaddle.x + halfBallWidth + 0.01) //ball needs to be stopped.
				|| (ownPaddle.x > field.horizontalCenter && //right paddle and
						fakeBall.x >= ownPaddle.x - halfBallWidth - 0.01)) //ball needs to be stopped.
			requiredPaddlePos.setLocation(fakeBall.x,fakeBall.y);
		else detectNextCollision();
	}
	
	private boolean acceptIfCloser(Vector3D path, Vector3D bound){
		// homogeneousIntersection
		Vector3D hi = Vector3D.crossProduct(path, bound);
		Point2D.Float intersection = new Point2D.Float(	(float)(hi.getX() / hi.getZ()),
														(float)(hi.getY() / hi.getZ()));
		float tmpTime = (float) fakeBall.distance(intersection) / ball.velocity; 
		if( tmpTime < remainingTime){
			hitPoint = intersection;
			remainingTime = tmpTime;
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return "Perfect";
	}
	
}
