package de.tub.mobint.assigment1.ai;

import de.tub.mobint.assigment1.Ball;
import de.tub.mobint.assigment1.Field;
import de.tub.mobint.assigment1.paddle.Paddle;
import de.tub.mobint.assigment1.paddle.PaddleController;

public class SimpleAI extends ArtificialIntelligence implements PaddleController{

	
	
	public SimpleAI(Ball ball, Field field, Paddle paddle) {
		super(ball, field, paddle);
	}

	
	@Override
	public void update(float dT) {
		if (ball.velocity < 100) smoothingFactor = 1 + (101-(int)ball.velocity)/10;
		else smoothingFactor = 1;
		if( paddle.y < ball.y - 1*smoothingFactor ) kpc.setMovement(1);
		else if( paddle.y > ball.y + 1*smoothingFactor ) kpc.setMovement(-1);
		else kpc.setMovement(0);
	}

	@Override
	public String getName() {
		return "Simple";
	}
	
	
	
}
