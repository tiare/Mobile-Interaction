package de.tub.mobint.assigment2.gui.icon;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class MouseIcon extends Icon {

	boolean active = false;
	final static float pi = processing.core.PConstants.PI;
	
	public MouseIcon(PApplet parent) {
		super(parent);
	}

	@Override
	public void draw( Point2D.Float pos) {
		
		parent.strokeWeight(1);
		parent.stroke(255, active? 255 : 170);
		parent.noFill();
		//mouse
		parent.rect(pos.x-3, pos.y+1, 6, 7);
		parent.rect(pos.x-3, pos.y-4, 6, 5);
		parent.line(pos.x, pos.y-4, pos.x, pos.y);
		parent.point(pos.x, pos.y-5);
		parent.point(pos.x-1, pos.y-6);
		parent.point(pos.x-2, pos.y-7);
		parent.point(pos.x-2, pos.y-8);
		parent.point(pos.x-1, pos.y-9);
		parent.point(pos.x, pos.y-10);
		
		/*parent.line(pos.x+4, pos.y+3, pos.x+4, pos.y+5);
		parent.line(pos.x+2, pos.y+3, pos.x+2, pos.y+5);
		*/
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
