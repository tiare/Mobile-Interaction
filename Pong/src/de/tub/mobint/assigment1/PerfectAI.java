package de.tub.mobint.assigment1;

import java.awt.geom.Point2D;

public class PerfectAI extends ArtificialIntelligence{

	Point2D nextHit; // position of next hit on own side
	
	public PerfectAI(Ball ball, Field field, VerticalPaddle paddle) {
		super(ball, field, paddle);
	}

	@Override
	public void update(float dT) {
		if( paddle.y < nextHit.getY()) paddle.setMovement(1);
		else if( paddle.y > nextHit.getY()) paddle.setMovement(-1);
		else paddle.setMovement(0);
	}

	@Override
	public String getName() {
		return "Perfect";
	}
	
}
