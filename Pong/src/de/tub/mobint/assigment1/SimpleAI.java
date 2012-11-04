package de.tub.mobint.assigment1;

public class SimpleAI extends ArtificialIntelligence{

	public SimpleAI(Ball ball, Field field, VerticalPaddle rPaddle) {
		super(ball, field, rPaddle);
	}

	@Override
	public void update(float dT) {
		if( rPaddle.y < ball.y ) rPaddle.setMovement(1);
		else if( rPaddle.y > ball.y ) rPaddle.setMovement(-1);
		else rPaddle.setMovement(0);
	}

	@Override
	public String getName() {
		return "Simple";
	}
	
}
