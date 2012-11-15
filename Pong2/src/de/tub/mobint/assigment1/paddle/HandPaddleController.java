package de.tub.mobint.assigment1.paddle;

import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class HandPaddleController implements PaddleController {

	SimpleOpenNI context;
	
	PVector hand3d;
	PVector hand2d;
	
	Paddle paddle;
	
	public HandPaddleController( Paddle paddle, SimpleOpenNI context) {
		this.paddle = paddle;
		hand3d = new PVector();
		hand2d = new PVector();
		this.context = context;
	}

	@Override
	public void update(float dT) {
		if(context.isTrackingSkeleton(1)){
			context.getJointPositionSkeleton(1, SimpleOpenNI.SKEL_LEFT_HAND, hand3d);
			context.convertRealWorldToProjective(hand3d, hand2d);
			
			paddle.updatePosition(hand2d.x, hand2d.y);
		}

	}

	@Override
	public String getName() {
		
		return "HandPaddleController";
	}

}
