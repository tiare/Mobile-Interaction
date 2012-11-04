package de.tub.mobint.assigment1;

public abstract class CollisionDetection {

	Ball ball;
	Field field;
	VerticalPaddle lPaddle;
	VerticalPaddle rPaddle;
	
	public CollisionDetection( Ball ball, Field field, VerticalPaddle lPaddle, VerticalPaddle rPaddle){
		this.ball = ball;
		this.field = field;
		this.lPaddle = lPaddle;
		this.rPaddle = rPaddle;
	}
	
	public abstract int update(float dT);
	
	public abstract String getName();
	
	public abstract void init();
}
