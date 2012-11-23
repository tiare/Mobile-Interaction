package de.tub.mobint.assigment2.gui;

import de.tub.mobint.assigment2.Pong;

public class ResetCommand implements ButtonActivationListener {

	Pong pong;
	RingButton btn;
	
	public ResetCommand(Pong pong, RingButton btn) {
		this.pong = pong;
		this.btn = btn;
	}

	@Override
	public void buttonActivated(RingButton button) {
		if( btn == button ){
			pong.resetGame();
		}
	}

}
