package de.tub.mobint.assigment2.paddle;

import de.tub.mobint.assigment2.gui.icon.Icon;

public class MousePaddleController extends PaddleController {
	
	int x;
	int y;
	
	public MousePaddleController(Paddle paddle, Icon icon) {
		super(paddle,icon);
	}

	@Override
	public void update(float dT) {
		paddle.updatePosition(x, y, true);

	}

	
	public void setMousePosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public String getName() {
		return "Mouse";
	}
}
