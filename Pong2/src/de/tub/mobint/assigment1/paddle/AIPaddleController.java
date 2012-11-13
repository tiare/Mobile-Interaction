package de.tub.mobint.assigment1.paddle;

import de.tub.mobint.assigment1.ai.ArtificialIntelligence;

public class AIPaddleController extends KeyPaddleController {

	ArtificialIntelligence ai;
	int smoothingFactor;
	
	public AIPaddleController(Paddle paddle, ArtificialIntelligence ai) {
		super(paddle);
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
