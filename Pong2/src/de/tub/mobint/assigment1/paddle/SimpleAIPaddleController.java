package de.tub.mobint.assigment1.paddle;

import de.tub.mobint.assigment1.Ball;

public class SimpleAIPaddleController extends KeyPaddleController {

	Ball ball;
	
	int smoothingFactor;
	
	public SimpleAIPaddleController(Paddle paddle, Ball ball) {
		super(paddle);
		this.ball = ball;
	}

	@Override
	public void update(float dT) {
		if (ball.velocity < 100) smoothingFactor = 1 + (101-(int)ball.velocity)/10;
		else smoothingFactor = 1;
		if( paddle.y < ball.y - 1*smoothingFactor ) setMovement(1);
		else if( paddle.y > ball.y + 1*smoothingFactor ) setMovement(-1);
		else setMovement(0);

		super.update(dT);
	}

}
