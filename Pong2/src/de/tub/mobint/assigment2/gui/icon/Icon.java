package de.tub.mobint.assigment2.gui.icon;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public abstract class Icon {
	PApplet parent;
	
	public Icon(PApplet parent){
		this.parent = parent;
	}
	
	abstract public void draw(Point2D.Float pos);
	abstract public void activate();
	abstract public void deactivate();
}
