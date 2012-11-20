package de.tub.mobint.assigment2.gui.icon;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class HandIcon extends Icon {

	boolean active = false;
	public HandIcon(PApplet parent) {
		super(parent);
	}

	@Override
	public void draw( Point2D.Float pos) {
		
		parent.strokeWeight(7);
		parent.stroke(active? 255 : 170);
		
		//palm of one's hands
		parent.point(pos.x, pos.y+4);
		
		// thumb
		parent.strokeWeight(2);
		parent.line(pos.x+3, pos.y+5, pos.x+5, pos.y+2);
		parent.strokeWeight(1);
		parent.line(pos.x+5, pos.y+2, pos.x+5, pos.y-2);
		
		// fingers
		parent.strokeWeight(1);
		parent.line(pos.x+2, pos.y, pos.x+2, pos.y-6);
		parent.line(pos.x  , pos.y, pos.x  , pos.y-7);
		parent.line(pos.x-2, pos.y, pos.x-2, pos.y-7);
		parent.line(pos.x-4, pos.y, pos.x-4, pos.y-5);
		
		

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
