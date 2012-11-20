package de.tub.mobint.assigment2.gui.icon;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class KeyIcon extends Icon {

	boolean active = false;
	public KeyIcon(PApplet parent) {
		super(parent);
	}

	@Override
	public void draw( Point2D.Float pos) {
		
		parent.strokeWeight(4);
		parent.stroke(255, active? 255 : 170);
		
		//arrow keys (up, down)
		parent.point(pos.x, pos.y+2);
		parent.point(pos.x, pos.y-3);
		// left, right
		parent.point(pos.x-5, pos.y+2);
		parent.point(pos.x+5, pos.y+2);
		
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
