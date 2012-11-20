package de.tub.mobint.assigment2.gui.icon;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class AiIcon extends Icon {

	boolean active = false;
	public AiIcon(PApplet parent) {
		super(parent);
	}

	@Override
	public void draw( Point2D.Float pos) {
		
		parent.strokeWeight(1);
		parent.stroke(255, active? 255 : 170);
		parent.noFill();
		//pc
		parent.rect(pos.x-6, pos.y+1, 12, 5);
		parent.rect(pos.x-3, pos.y-7, 6, 6);
		
		parent.line(pos.x+4, pos.y+3, pos.x+4, pos.y+5);
		parent.line(pos.x+2, pos.y+3, pos.x+2, pos.y+5);
		
		/*parent.point(pos.x, pos.y-3);
		// left, right
		parent.point(pos.x-5, pos.y+3);
		parent.point(pos.x+5, pos.y+3);*/
		
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
