package de.tub.mobint.assigment2.gui;

import de.tub.mobint.assigment2.Point2DDepth;
import de.tub.mobint.assigment2.User;
import de.tub.mobint.assigment2.gui.icon.Icon;

public class HandMarker implements RightHandPositionListener,
		LeftHandPositionListener {

	Icon icon;
	
	Point2DDepth pos;
	
	public HandMarker(Icon icon) {
		this.icon = icon;
	}

	@Override
	public void leftHandPositionChanged(User user, Point2DDepth pos) {
		this.pos = pos;
	}

	@Override
	public void rightHandPositionChanged(User user, Point2DDepth pos) {
		this.pos = pos;
	}
	
	public void draw(){
		if( pos != null){
			icon.draw(pos);
		}
	}

}
