package de.tub.mobint.assigment2;

import java.awt.geom.Point2D.Float;

public class Point2DDepth extends Float {

	float depth;
	public Point2DDepth() {
		super();
		depth = 0;
	}

	public Point2DDepth(float arg0, float arg1, float depth) {
		super(arg0, arg1);
		this.depth = depth;
	}

}
