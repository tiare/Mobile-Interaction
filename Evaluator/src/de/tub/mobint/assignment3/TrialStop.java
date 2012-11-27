package de.tub.mobint.assignment3;

import java.util.TimerTask;

public class TrialStop extends TimerTask{
	Trial trial;
	Evaluator eval;
	public TrialStop(Trial trial, Evaluator eval) {
		this.trial = trial;
		this.eval = eval;
	}
	@Override
	public void run() {
		trial.stop();
		eval.stopTrial();
		
	}

}
