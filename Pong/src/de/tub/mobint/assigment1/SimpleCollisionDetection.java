package de.tub.mobint.assigment1;

public class SimpleCollisionDetection extends CollisionDetection{

	public SimpleCollisionDetection(Ball ball, Field field, VerticalPaddle lPaddle, VerticalPaddle rPaddle) {
		super(ball, field, lPaddle, rPaddle);
	}

	@Override
	public int update(float dT) {
		if( ball.bottom() > field.bottom || ball.top() < field.top) ball.horizontalBounce(); 
		
		if( ball.left() < lPaddle.right() ){
			if( lPaddle.inRange(ball.y) )	ball.verticalBounce();
			else							return -1;
		}
		
		if( ball.right() > rPaddle.left() ){
			if( rPaddle.inRange(ball.y) )	ball.verticalBounce();
			else							return 1;
		}
		
		return 0;
	}

}
