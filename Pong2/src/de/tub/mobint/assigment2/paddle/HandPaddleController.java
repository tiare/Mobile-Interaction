package de.tub.mobint.assigment2.paddle;

import de.tub.mobint.assigment2.Point2DDepth;
import de.tub.mobint.assigment2.User;
import de.tub.mobint.assigment2.gui.LeftHandPositionListener;
import de.tub.mobint.assigment2.gui.RightHandPositionListener;
import de.tub.mobint.assigment2.gui.icon.Icon;

public class HandPaddleController extends PaddleController implements LeftHandPositionListener, RightHandPositionListener {

	Point2DDepth pos;
	
	public HandPaddleController( Paddle paddle, Icon icon) {
		super( paddle, icon);
		pos = new Point2DDepth(0,0,2);
	}
	
	@Override
	public void update(float dT) {
		paddle.updatePosition(pos.x,pos.y,true);

	}

	@Override
	public String getName() {
		
		return "Hand";
	}
	@Override
	public void leftHandPositionChanged(User user, Point2DDepth pos) {
		this.pos = pos;
	}

	@Override
	public void rightHandPositionChanged(User user, Point2DDepth pos) {
		this.pos = pos;
	}

}
