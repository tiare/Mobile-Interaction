package de.tub.mobint.assigment1;

public abstract class ArtificialIntelligence {
	
	Ball ball;
	Field field;
	VerticalPaddle paddle;
	int smoothingFactor;
	
	
	public ArtificialIntelligence(Ball ball, Field field, VerticalPaddle paddle){
		this.ball = ball;
		this.field = field;
		this.paddle = paddle;
	}
	
	public abstract void update(float dT);
	public abstract String getName();
}
