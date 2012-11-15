package de.tub.mobint.assigment2.ai;

import de.tub.mobint.assigment2.Ball;
import de.tub.mobint.assigment2.Field;
import de.tub.mobint.assigment2.paddle.KeyPaddleController;
import de.tub.mobint.assigment2.paddle.Paddle;
import de.tub.mobint.assigment2.paddle.PaddleController;

public abstract class ArtificialIntelligence {
	
	Ball ball;
	Field field;
	Paddle paddle;
	int smoothingFactor;
	
	KeyPaddleController kpc;
	
	
	
	public ArtificialIntelligence(Ball ball, Field field, Paddle paddle){
		this.ball = ball;
		this.field = field;
		this.paddle = paddle;
	}
	
	public abstract void update(float dT);
	public abstract String getName();
	
	public void setKeyPaddleController( KeyPaddleController kpc ){
		this.kpc = kpc;
	}
}
