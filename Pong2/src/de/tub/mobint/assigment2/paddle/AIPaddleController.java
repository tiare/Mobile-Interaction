package de.tub.mobint.assigment2.paddle;

import de.tub.mobint.assigment2.ai.ArtificialIntelligence;
import de.tub.mobint.assigment2.gui.icon.Icon;

public class AIPaddleController extends KeyPaddleController {

	ArtificialIntelligence ai;
	
	public AIPaddleController(Paddle paddle, Icon icon, ArtificialIntelligence ai) {
		super(paddle, icon);
		this.ai = ai;
		ai.setKeyPaddleController(this);
	}

	@Override
	public void update(float dT) {
		ai.update(dT);
		super.update(dT);
	}
	
	@Override
	public String getName() {
		return ai.getName() + " AI";
	}

}
