package de.tub.mobint.assigment2.gui.icon;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class BallFastIcon extends Icon {

	boolean active = false;
	public BallFastIcon(PApplet parent) {
		super(parent);
	}

	@Override
	public void draw( Point2D.Float pos) {
		
		parent.strokeWeight(8);
		parent.stroke(255, active? 255 : 170);
		
		//ball
		parent.point(pos.x+4, pos.y);
		
		// fingers
		parent.strokeWeight(1);
		parent.line(pos.x-2, pos.y-2, pos.x-7, pos.y-2);
		parent.line(pos.x-4, pos.y  , pos.x-9, pos.y  );
		parent.line(pos.x-2, pos.y+2, pos.x-7, pos.y+2);
		
		

	}

	@Override
	public void activate() {
		active = true;
	}

	@Override
	public void deactivate() {
		active = false;
		
	}

}
