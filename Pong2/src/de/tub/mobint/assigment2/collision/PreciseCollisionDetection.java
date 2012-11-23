package de.tub.mobint.assigment2.collision;

import java.awt.geom.Point2D;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import de.tub.mobint.assigment2.Ball;
import de.tub.mobint.assigment2.Field;
import de.tub.mobint.assigment2.paddle.Paddle;

public class PreciseCollisionDetection extends CollisionDetection {
	
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
	
	float ballDirX,ballDirY,ballVelX,ballVelY,ballSignX,ballSignY;
	
	float remainingTime; // time until next hit
	int heading;
	float halfBallWidth;
	float deltaTime;
	
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
		
		deltaTime = dT;
		
		while( remainingTime < dT ){
			ball.update(remainingTime);
			dT -= remainingTime;
			int score = bounce();
			
			if( score != 0){
				return score;
			}
			
			detectNextCollision();
		}
		//remainingTime -= dT;
		ball.update(dT);
		
		lPaddle.move();
		rPaddle.move();
		
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
			//if( lPaddle.inRange(ball) )	
			ball.verticalBounce();
			break;
			
		case HEADING_RIGHT:
			//if( rPaddle.inRange(ball) )	
			ball.verticalBounce();
			break;
		}
		
		return 0;
		
	}
	
	private boolean handlePaddleCollision(Paddle paddle) {
		
		if(ballVelX*paddle.direction>0 && paddle.velX*paddle.direction<ballVelX*paddle.direction)
			return false;
		
		float ballSize = ball.strokeWeight/2;
		float paddleWidth = paddle.strokeWeight/2;
		
		float delta = (ball.x-ballSize*paddle.direction) - (paddle.x+paddleWidth*paddle.direction);
		float t = delta / (paddle.velX/deltaTime - ballVelX);
		if(t<0)
			return false;
		
		float newPaddleX = paddle.x + t*paddle.velX;
		float newPaddleY = paddle.y + t*paddle.velY;
		float newBallY = ball.y + t*ballVelY;
		
		boolean collUp   = (newBallY-ballSize)>=newPaddleY-paddle.halfSize && (newBallY-ballSize)<=newPaddleY+paddle.halfSize;
		boolean collDown = (newBallY+ballSize)>=newPaddleY-paddle.halfSize && (newBallY+ballSize)<=newPaddleY+paddle.halfSize;

		if(collUp || collDown) {
			remainingTime = t;
			return true;
		}
		
		return false;
	}
	
	private void detectNextCollision(){
		remainingTime = Float.MAX_VALUE;
		
		ballDirX = (float)(Math.cos(ball.heading));
		ballDirY = (float)(Math.sin(ball.heading));
		ballVelX = ballDirX*ball.velocity;
		ballVelY = ballDirY*ball.velocity;
		ballSignX = ballVelX<0?-1:1;
		ballSignY = ballVelY<0?-1:1;
		boolean positiveX = ballDirX > 0;
		boolean positiveY = ballDirY > 0;
		
		Vector3D path = Vector3D.crossProduct(	new Vector3D(ball.x, ball.y, 1),
						new Vector3D(ball.x + Math.cos(ball.heading),
									ball.y + Math.sin(ball.heading),
									1) );
		//PADDLES
		if(handlePaddleCollision(lPaddle))
			heading = HEADING_LEFT;
		if(handlePaddleCollision(rPaddle))
			heading = HEADING_RIGHT;
		
		if( positiveX ){
			if( acceptIfCloser(path, right) ){
				heading = HEADING_RIGHT_OUT;
			}
			
//			if( ball.x < rPaddle.x - ball.strokeWeight && acceptIfCloser(path, rPaddleLine) ){
//				heading = HEADING_RIGHT;
//			}
		} else {
			if( acceptIfCloser(path, left) ){
				heading = HEADING_LEFT_OUT;
			}
			
//			if( ball.x > lPaddle.x + ball.strokeWeight && acceptIfCloser(path, lPaddleLine) ){
//				heading = HEADING_LEFT;
//			}
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
}
