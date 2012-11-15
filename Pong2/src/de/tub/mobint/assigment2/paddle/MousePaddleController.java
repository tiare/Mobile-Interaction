package de.tub.mobint.assigment2.paddle;

public class MousePaddleController implements PaddleController {

	Paddle paddle;
	
	int x;
	int y;
	
	public MousePaddleController(Paddle paddle) {
		this.paddle = paddle;
	}

	@Override
	public void update(float dT) {
		paddle.updatePosition(x, y);

	}

	
	public void setMousePosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public String getName() {
		return "MousePaddleController";
	}
}
