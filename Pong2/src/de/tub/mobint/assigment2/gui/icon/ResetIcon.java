package de.tub.mobint.assigment2.gui.icon;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class ResetIcon extends Icon {

	boolean active = false;
	public ResetIcon(PApplet parent) {
		super(parent);
	}

	@Override
	public void draw( Point2D.Float pos) {
		parent.strokeWeight(1);
		parent.stroke(255, active? 255 : 255);
		parent.noFill();
		//0:0
		parent.rect(pos.x-6, pos.y-4, 4, 8);
		parent.rect(pos.x+2, pos.y-4, 4, 8);
		
		parent.point(pos.x, pos.y - 2);
		parent.point(pos.x, pos.y + 2);
		
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
