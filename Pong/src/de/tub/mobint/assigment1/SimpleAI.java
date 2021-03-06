package de.tub.mobint.assigment1;

public class SimpleAI extends ArtificialIntelligence{

	public SimpleAI(Ball ball, Field field, VerticalPaddle paddle) {
		super(ball, field, paddle);
	}

	@Override
	public void update(float dT) {
		if (ball.velocity < 100) smoothingFactor = 1 + (101-(int)ball.velocity)/10;
		else smoothingFactor = 1;
		if( paddle.y < ball.y - 1*smoothingFactor ) paddle.setMovement(1);
		else if( paddle.y > ball.y + 1*smoothingFactor ) paddle.setMovement(-1);
		else paddle.setMovement(0);
	}

	@Override
	public String getName() {
		return "Simple";
	}
	
}
