package de.tub.mobint.assigment1;

import java.util.Vector;

import processing.core.*;

public class Pong extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int width = 640;
	int height = 480;
	
	float ballStartVelocity = 160.0f; // px/s
	float fps = 30.0f;
	
	int margin = 50;
	
	int scoreSize = 40;

	int scoreP1;
	int scoreP2;
	
	int AILevel = 0;
	
	Field field;
	Ball ball;
	VerticalPaddle lPaddle;
	VerticalPaddle rPaddle;
	
	Vector<CollisionDetection> collisionDetections = new Vector<CollisionDetection>();
	int cdIndex = 0;
	
	Vector<ArtificialIntelligence> AIs = new Vector<ArtificialIntelligence>();
	int aiIndex = 0;
	
	public void setup(){
		size(width,height,P2D);
		background(0);
		frameRate(fps);
		
		field = new Field(scoreSize + margin/2, height - margin/2, margin, width-margin);
		
		ball = new Ball(this);
		resetBall(true);
		
		lPaddle = new VerticalPaddle(this, field.left, field.verticalCenter);
		lPaddle.setBounds(field.top, field.bottom);
		rPaddle = new VerticalPaddle(this, field.right, field.verticalCenter);
		rPaddle.setBounds(field.top, field.bottom);
		
		collisionDetections.add(new SimpleCollisionDetection(ball, field, lPaddle, rPaddle));
		
		PreciseCollisionDetection pcd = new PreciseCollisionDetection(ball, field, lPaddle, rPaddle);
		pcd.setPApplet(this);
		collisionDetections.add(pcd);
		
		AIs.add( new SimpleAI(ball, field, rPaddle));
	}
	
	public void draw(){
		float dT = 1.0f / frameRate;
		//processInputs(); // user inputs
		update(dT); // move objects
		
		
		
		render(); // draw
	}
	
	private void resetBall( boolean p1 ){
		ball.setLocation( p1 ? field.left + margin*0.5f : field.right - margin*0.5f,
							field.verticalCenter);
		float pi = processing.core.PConstants.PI;
		ball.heading = p1 ? pi/4.0f : pi-pi/4.0f;
	}
	
	private void render(){
		//clear bg
		background(0);
		
		//Draw field and score
		stroke(255);
		strokeWeight(10);
		textSize(scoreSize);
		textAlign(CENTER);
		fill(255);
		text(scoreP1 + " : " + scoreP2, field.horizontalCenter, scoreSize);
		// dashed line
		for(int i = field.top; i < field.bottom; i=i+20){
			line(field.horizontalCenter, i, field.horizontalCenter, i+10);
		}
		strokeWeight(0);
		fill(33);
		rect(1,field.top, field.left, field.height);
		rect(field.right,field.top, width, field.height);
		strokeWeight(1);
		stroke(99);
		line(0,field.top,width,field.top);
		line(0,field.bottom,width,field.bottom);
		
		// draw Paddels
		lPaddle.draw();
		rPaddle.draw();
		
		// draw ball
		ball.draw(); 
	}
	
	public void update(float dT){
		lPaddle.update(dT);
		if( AILevel != 0){
			AIs.get(AILevel-1).update(dT);
		}
		rPaddle.update(dT);
		
		
		
		int collision = collisionDetections.get(cdIndex).update(dT);
		if( collision > 0){
			scoreP1++;
			resetBall(true);
		} else if(collision < 0){
			scoreP2++;
			resetBall(false);
		}
	}
	
	public void keyPressed( ) {
		if( key == 'q' ) lPaddle.setMovement(-1);
		if( key == 'a' ) lPaddle.setMovement(1);
		
		if(AILevel == 0){
			if( key == 'o' ) rPaddle.setMovement(-1);
			if( key == 'l' ) rPaddle.setMovement(1);
		}
		
		if( key == '+') ball.velocity += 20.0f;
		if( key == '-'){
			ball.velocity -= 20.0f;
			if( ball.velocity < 20.0f) ball.velocity = 20.0f;
		}
		
		if( key == '*') fps += 5.0f;
		if( key == '/'){
			fps -= 5.0f;
			if( fps < 1.0f) fps = 1.0f;
		}
		if(frameRate != fps ){
			frameRate(fps);
		}
		
		
		if( key == 'b' ){
			AILevel += 1;
			if(AILevel > AIs.size() ) AILevel = 0;
			System.out.println("use AI: " + AIs.get(AILevel-1).getName() );
		}
		
		if( key == 'c' ){
			cdIndex ++;
			if(cdIndex >= collisionDetections.size() ) cdIndex = 0;
			collisionDetections.get(cdIndex).init();
			System.out.println("use collision detection: "+ collisionDetections.get(cdIndex).getName());
		}
	}
	
	public void keyReleased( ) {
		if( key == 'q' && lPaddle.movement == -1) lPaddle.setMovement(0);
		if( key == 'a' && lPaddle.movement == 1) lPaddle.setMovement(0);
		
		if(AILevel == 0){
			if( key == 'o' && rPaddle.movement == -1) rPaddle.setMovement(0);
			if( key == 'l' && rPaddle.movement == 1) rPaddle.setMovement(0);
		}
	}
	
	
	public static void main(String args[]) {
		//PApplet.main(new String[] { "--present", "de.tub.mobint.assigment1.Pong" });
		PApplet.main(new String[] { "de.tub.mobint.assigment1.Pong" });
	}
}


