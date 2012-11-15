package de.tub.mobint.assigment2.paddle;

import java.awt.geom.Point2D;

import de.tub.mobint.assigment2.OpenNiControlRecognition;

public class HandPaddleController implements PaddleController {

	OpenNiControlRecognition context;
	Paddle paddle;
	
	public HandPaddleController( Paddle paddle, OpenNiControlRecognition context) {
		this.paddle = paddle;
		this.context = context;
	}

	@Override
	public void update(float dT) {
		if(context.isTrackingSkeleton(1)){
			Point2D.Float pos;
			if( paddle.side == Paddle.LEFT_SIDE)	pos = context.getLeftHandPosition(1);
			else 									pos = context.getRightHandPosition(2);
			paddle.updatePosition(pos.x,pos.y);
			
		}

	}

	@Override
	public String getName() {
		
		return "HandPaddleController";
	}

}
