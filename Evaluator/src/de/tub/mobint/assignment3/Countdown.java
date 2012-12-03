package de.tub.mobint.assignment3;

public class Countdown {

	Evaluator parent;
	
	float time = 0.0f;
	float duration = 5.0f;
	int color;
	
	int x;
	int y;
	
	int steps = 10;
	
	int radius = 60;
	
	final static float pi = processing.core.PConstants.PI;
	
	public Countdown(Evaluator parent) {
		this.parent = parent;
		color = parent.color(0x33);
		x = parent.width/2;
		y = parent.height/2;
	}

	
	
	
	public void draw( float dT){
		
		time += dT;
		
		parent.background(0xcc);
		parent.noFill();
		parent.stroke(color, 255);
		
		// draw ring
		float stepsize = 255.0f/steps;
		for( int i = steps; i > 0; i--){
			parent.strokeWeight(i);
			parent.stroke(color, stepsize);
			parent.ellipse(x,y, radius, radius);
			//parent.ellipse(center.x, center.y, radius, radius);
		}
		
		parent.noStroke();
		float progress = time*100/duration;
		
		if( progress > 100 ){
			parent.stopCountdown();
			return;
		}
		
		parent.fill(color, progress * 2.55f * 0.8f);
		
		float angle = progress * pi * 0.02f; // 0.02 -> * 2 / 100
		if( angle < pi){
			parent.arc(x,y, radius-steps/2, radius-steps/2, 0, angle);
		} else {
			parent.arc(x,y, radius-steps/2, radius-steps/2, 0, pi);
			parent.arc(x,y, radius-steps/2, radius-steps/2, pi, angle );
		}
		
	}
	
	
	
	
	
}
