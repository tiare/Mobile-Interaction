package de.tub.mobint.assigment2.gui;

import de.tub.mobint.assigment2.Point2DDepth;
import de.tub.mobint.assigment2.User;

public class HandToPointerPositionAdapter implements LeftHandPositionListener,
		RightHandPositionListener {

	PointerPositionListener posListener;
	
	public HandToPointerPositionAdapter(PointerPositionListener posListener) {
		this.posListener = posListener;
	}

	@Override
	public void rightHandPositionChanged(User user, Point2DDepth pos) {
		posListener.pointerPositionChanged(pos);

	}

	@Override
	public void leftHandPositionChanged(User user, Point2DDepth pos) {
		posListener.pointerPositionChanged(pos);
	}

}
