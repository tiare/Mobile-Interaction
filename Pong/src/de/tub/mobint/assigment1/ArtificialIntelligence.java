package de.tub.mobint.assigment1;

public abstract class ArtificialIntelligence {
	
	Ball ball;
	Field field;
	VerticalPaddle rPaddle;
	
	
	public ArtificialIntelligence(Ball ball, Field field, VerticalPaddle rPaddle){
		this.ball = ball;
		this.field = field;
		this.rPaddle = rPaddle;
	}
	
	public abstract void update(float dT);
}
