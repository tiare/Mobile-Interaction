package de.tub.mobint.assigment2.collision;

import de.tub.mobint.assigment2.Ball;
import de.tub.mobint.assigment2.Field;
import de.tub.mobint.assigment2.paddle.Paddle;

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
