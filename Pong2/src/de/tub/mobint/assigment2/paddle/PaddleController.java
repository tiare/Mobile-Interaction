package de.tub.mobint.assigment2.paddle;

import de.tub.mobint.assigment2.gui.icon.Icon;

public abstract class PaddleController {

	protected Paddle paddle;
	protected Icon icon;
	
	public PaddleController(Paddle paddle, Icon icon){
		this.paddle = paddle;
		this.icon = icon;
	}
	
	public Icon getIcon(){
		return icon;
	}
	abstract public void update(float dT);
	abstract public String getName();
}
