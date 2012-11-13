package de.tub.mobint.assigment1.paddle;

public class KeyPaddleController implements PaddleController {

	
	Paddle paddle;
	
	float x;
	float y;
	
	float velocity = 120.0f;
	public int movement = 0;
	
	
	public KeyPaddleController( Paddle paddle ) {
		this.paddle = paddle;
	}

	@Override
	public void update(float dT) {
		if( movement != 0 ){
			paddle.updatePosition(paddle.x, paddle.y + velocity * movement * dT );
		}
	}
	
	public void setMovement(int move){
		movement = move;
	}

	@Override
	public String getName() {
		return "Key controlled paddle";
	}
}
