package de.tub.mobint.assigment2.gui;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import de.tub.mobint.assigment2.Point2DDepth;
import de.tub.mobint.assigment2.gui.icon.Icon;

import processing.core.PApplet;

public class RingButton implements PointerPositionListener {
	
	PApplet parent;
	Point2D.Float center;
	
	float radius;
	int color;
	int steps;
	
	Icon icon;
	
	int hoverTime= 0;
	int threshold = 100;
	
	boolean active = false;
	
	boolean pointerInside = false;
	
	private List<ButtonActivationListener> buttonListener;
	
	final static float pi = processing.core.PConstants.PI;
	
	public RingButton(PApplet parent, Point2D.Float center, float radius, Icon icon) {
		this.parent = parent;
		this.center = center;
		this.radius = radius;
		this.color = parent.color(0,255,0);
		this.icon = icon;
		steps = 10;
		hoverTime = 0;
		
		buttonListener = new LinkedList<ButtonActivationListener>();
	}
	
	public void addButtonActivationListener(ButtonActivationListener listener){
		buttonListener.add(listener);
	}
	
	public void notifyButtonActivationsListener(){
		for( ButtonActivationListener listener : buttonListener ){
			listener.buttonActivated(this);
		}
	}
	
	public void draw( float dT ){
		
		parent.noFill();
		parent.stroke(color, 255);
		
		// draw ring
		float stepsize = 255.0f/steps;
		for( int i = steps; i > 0; i--){
			parent.strokeWeight(i);
			parent.stroke(color, stepsize);
			parent.ellipse(center.x, center.y, radius, radius);
			//parent.ellipse(center.x, center.y, radius, radius);
		}
		
		if(active){
			
			parent.noStroke();
			parent.fill(color, 255 * 0.8f);
			parent.ellipse(center.x, center.y, radius-steps/2, radius-steps/2);
			
		} else {
		
			//handle pointer
			if( pointerInside ){
				hoverTime += 2.0f;
				if( hoverTime >= threshold ){
					activate();
					notifyButtonActivationsListener();
				}
			} else {
				if(hoverTime > 0){
					hoverTime -= 10;
					if( hoverTime < 0) hoverTime = 0;
				}
			}
		
			if( hoverTime > 0 ){
				parent.noStroke();
				float progress = hoverTime*100/threshold;
				parent.fill(color, progress * 2.55f * 0.8f);
				
				float angle = progress * pi * 0.02f; // 0.02 -> * 2 / 100
				if( angle < pi){
					parent.arc(center.x, center.y, radius-steps/2, radius-steps/2, 0, angle);
				} else {
					parent.arc(center.x, center.y, radius-steps/2, radius-steps/2, 0, pi);
					parent.arc(center.x, center.y, radius-steps/2, radius-steps/2, pi, angle );
				}
				
			}
		}
		
		icon.draw(center);
	}
	
	public void deactivate(){
		hoverTime = 0;
		active = false;
		icon.deactivate();
	}
	
	public void activate(){
		hoverTime = threshold;
		icon.activate();
		active = true;
	}
	
	public void pointerPositionChanged(Point2DDepth pos){
		pointerInside = pos.distance(center) <= radius;
	}
}
