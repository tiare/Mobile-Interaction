package de.tub.mobint.assigment1.collision;

import de.tub.mobint.assigment1.Ball;
import de.tub.mobint.assigment1.Field;
import de.tub.mobint.assigment1.paddle.Paddle;

public abstract class CollisionDetection {

	Ball ball;
	Field field;
	Paddle lPaddle;
	Paddle rPaddle;
	
	public CollisionDetection( Ball ball, Field field, Paddle lPaddle, Paddle rPaddle){
		this.ball = ball;
		this.field = field;
		this.lPaddle = lPaddle;
		this.rPaddle = rPaddle;
	}
	
	public abstract int update(float dT);
	
	public abstract String getName();
	
	public abstract void init();
}
