package de.tub.mobint.assigment1;

import java.util.Vector;

import processing.core.*;

public class Pong extends PApplet {

	/**
	 * @author Marcel Karsten (343619), Tiare Feuchtner (343...)
	 */
	private static final long serialVersionUID = 1L;

	int width = 640;
	int height = 480;
	
	float ballStartVelocity = 160.0f; // px/s
	float fps = 30.0f;
	
	int margin = 50;
	int paddleOffset = 0;
	
	int scoreSize = 40;

	int scoreP1;
	int scoreP2;
	
	String displayText = "";
	int displayTextSize = 15;
	int[] displayTextColor = {0, 102, 153};
	int displayTextTimeout = 50;
	int displayTextCountdown = 0;
	int displayTextX = 60;
	int displayTextY = 450;
	
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
		
		field = new Field(scoreSize + margin/2, height - margin/2, margin, width-margin, 0, width);
		
		ball = new Ball(this);
		
		lPaddle = new VerticalPaddle(this, field.left+paddleOffset, field.verticalCenter);
		lPaddle.setBounds(field.top, field.bottom);
		rPaddle = new VerticalPaddle(this, field.right-paddleOffset, field.verticalCenter);
		rPaddle.setBounds(field.top, field.bottom);
		
		resetBall(true);
		
		collisionDetections.add(new SimpleCollisionDetection(ball, field, lPaddle, rPaddle));
		
		PreciseCollisionDetection pcd = new PreciseCollisionDetection(ball, field, lPaddle, rPaddle);
		pcd.setPApplet(this);
		collisionDetections.add(pcd);
		
		AIs.add( new SimpleAI(ball, field, rPaddle));
		AIs.add( new PerfectAI(ball, field, rPaddle));
	}
	
	public void draw(){
		float dT = 1.0f / frameRate;
		//processInputs(); // user inputs
		update(dT); // move objects
		
		
		
		render(); // draw
	}
	
	private void resetBall( boolean p1 ){
		ball.setLocation( p1 ? field.left + margin*0.5f : field.right - margin*0.5f,
				p1 ? lPaddle.getPosition() : rPaddle.getPosition());
		float pi = processing.core.PConstants.PI;
		ball.heading = p1 ? pi/4.0f : pi-pi/4.0f;
		ball.out = false;
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
		
		//info display
		textSize(displayTextSize);
		textAlign(LEFT);
		fill(displayTextColor[0], displayTextColor[1], displayTextColor[2]);
		text(displayText, displayTextX, displayTextY);
		
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
		
		if (displayTextCountdown > 0)
			displayTextCountdown--;
		else
			displayText = "";
	}
	
	public void keyPressed( ) {
		if( key == 'q' ) lPaddle.setMovement(-1);
		if( key == 'a' ) lPaddle.setMovement(1);
		
		if(AILevel == 0){
			if( key == 'o' ) rPaddle.setMovement(-1);
			if( key == 'l' ) rPaddle.setMovement(1);
		}
		
		if( key == '+'){ 
			ball.velocity += 20.0f;
			displayText = "Ball speed: " + ball.velocity;
			displayTextCountdown = displayTextTimeout;
		}
		if( key == '-'){
			ball.velocity -= 20.0f;
			if( ball.velocity < 20.0f) ball.velocity = 20.0f;
			displayText = "Ball speed: " + ball.velocity;
			displayTextCountdown = displayTextTimeout;
		}
		
		if( key == '*') { 
			fps += 5.0f;
			displayText = "Fps: " + fps;
			displayTextCountdown = displayTextTimeout;
		}
		if( key == '/'){
			fps -= 5.0f;
			if( fps < 1.0f) fps = 1.0f;
			displayText = "Fps: " + fps;
			displayTextCountdown = displayTextTimeout;
		}
		if(frameRate != fps ){
			frameRate(fps);
		}
		
		
		if( key == 'b' ){
			AILevel += 1;
			if(AILevel > AIs.size() ) AILevel = 0;
			
			if (AILevel == 0) {
				System.out.println("AI: off");
				displayText = "AI: off";
				displayTextCountdown = displayTextTimeout;
			}
			else {
				System.out.println("AI: " + AIs.get(AILevel-1).getName() );
				displayText = "AI: " + AIs.get(AILevel-1).getName();
				displayTextCountdown = displayTextTimeout;
			}
		}
		
		if( key == 'c' ){
			cdIndex ++;
			if(cdIndex >= collisionDetections.size() ) cdIndex = 0;
			collisionDetections.get(cdIndex).init();
			System.out.println("Collision detection: "+ collisionDetections.get(cdIndex).getName());
			displayText = "Collision detection: "+ collisionDetections.get(cdIndex).getName();
			displayTextCountdown = displayTextTimeout;
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


