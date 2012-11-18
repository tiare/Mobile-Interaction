package de.tub.mobint.assigment2.paddle;

import de.tub.mobint.assigment2.gui.icon.Icon;

public class KeyPaddleController extends PaddleController {
	
	float x;
	float y;
	
	float velocity = 120.0f;
	public int movement = 0;
	
	
	public KeyPaddleController( Paddle paddle, Icon icon ) {
		super(paddle, icon);
	}

	@Override
	public void update(float dT) {
		if( movement != 0 ){
			float x;
			if( paddle.side == Paddle.LEFT_SIDE )	x = paddle.bounds.left;
			else									x = paddle.bounds.right;
			paddle.updatePosition(x, paddle.y + velocity * movement * dT );
		}
	}
	
	public void setMovement(int move){
		movement = move;
	}

	@Override
	public String getName() {
		return "Keyboard";
	}
}
