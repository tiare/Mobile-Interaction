package de.tub.mobint.assigment2;

import java.util.Vector;

import de.tub.mobint.assigment2.ai.ArtificialIntelligence;
import de.tub.mobint.assigment2.ai.PerfectAI;
import de.tub.mobint.assigment2.ai.SimpleAI;
import de.tub.mobint.assigment2.collision.CollisionDetection;
import de.tub.mobint.assigment2.collision.PreciseCollisionDetection;
import de.tub.mobint.assigment2.paddle.AIPaddleController;
import de.tub.mobint.assigment2.paddle.HandPaddleController;
import de.tub.mobint.assigment2.paddle.KeyPaddleController;
import de.tub.mobint.assigment2.paddle.MousePaddleController;
import de.tub.mobint.assigment2.paddle.Paddle;
import de.tub.mobint.assigment2.paddle.PaddleController;

import processing.core.*;

import SimpleOpenNI.*;

public class Pong extends PApplet {

	private OpenNiControlRecognition onicr;
	
	/**
	 * @author Marcel Karsten (343619), Tiare Feuchtner (343...)
	 */
	private static final long serialVersionUID = 1L;

	int width = 640;
	int height = 480;
	
	float ballVelocity; // px/s
	float fps = 30.0f;
	
	int margin = 50;
	int paddleOffset = 0;
	
	int scoreSize = 40;

	int scoreP1;
	int scoreP2;
	
	String infoText = "";
	int infoTextSize = 15;
	int[] infoTextColor = {0, 102, 153};
	int infoTextTimeout = 50;
	int infoTextCountdown = 0;
	int infoTextX = 60;
	int infoTextY = 60;
	
	String leftAIText = "";
	String rightAIText = "";
	int AITextSize = 15;
	int[] AITextColor = {52, 190, 68};
//	int AITextTimeout = 50;
//	int AITextCountdown = 0;
	int leftAITextX = 60;
	int leftAITextY = 470;
	int rightAITextX = 575;
	int rightAITextY = 470;
		
	Field field;
	Ball ball;
	Paddle lPaddle;
	Paddle rPaddle;
	
	MousePaddleController mousePC;
	KeyPaddleController leftKeyPC;
	KeyPaddleController rightKeyPC;
	
	AIPaddleController rightSimpleAIPC;
	AIPaddleController rightStrongAIPC;
	
	PaddleController activeLeftPC;
	PaddleController activeRightPC;
	
	PaddleController prevLeftPC;
	PaddleController prevRightPC;
	
	HandPaddleController leftHandPC;
	HandPaddleController rightHandPC;
	
	Vector<CollisionDetection> collisionDetections = new Vector<CollisionDetection>();
	int cdIndex = 0;
	
	//Store the AIs for the right paddle
	Vector<ArtificialIntelligence> rightPaddleAIs = new Vector<ArtificialIntelligence>();
	int rightAILevel = 0;
	
	//Store the AIs for the left paddle
	Vector<ArtificialIntelligence> leftPaddleAIs = new Vector<ArtificialIntelligence>();
	int leftAILevel = 0;
	
	public void setup(){
		size(width,height,P2D);
		background(0);
		frameRate(fps);
		
		// OpenNI stuff
		
		//context = new SimpleOpenNI(this, SimpleOpenNI.RUN_MODE_MULTI_THREADED);
		onicr = new OpenNiControlRecognition(this, SimpleOpenNI.RUN_MODE_SINGLE_THREADED);
		onicr.enableDepth();
		onicr.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
		
		
		field = new Field(scoreSize + margin/2, height - margin/2, margin, width-margin, 0, width);
		
		ball = new Ball(this);
		ballVelocity = ball.velocity;
		
		lPaddle = new Paddle(this, field.left+paddleOffset, field.verticalCenter, field.getLeftArea(ball.strokeWeight));
		rPaddle = new Paddle(this, field.right-paddleOffset, field.verticalCenter, field.getRightArea(ball.strokeWeight));
		
		resetBall(false);
		
		//collisionDetections.add(new SimpleCollisionDetection(ball, field, lPaddle, rPaddle));
		
		PreciseCollisionDetection pcd = new PreciseCollisionDetection(ball, field, lPaddle, rPaddle);
		pcd.setPApplet(this);
		collisionDetections.add(pcd);
		
		mousePC = new MousePaddleController(lPaddle);
		leftKeyPC = new KeyPaddleController(lPaddle);
		rightKeyPC = new KeyPaddleController(rPaddle);
		
		rightSimpleAIPC = new AIPaddleController(rPaddle, new SimpleAI(ball, field, rPaddle));
		rightStrongAIPC = new AIPaddleController(rPaddle, new PerfectAI(ball, field, rPaddle, lPaddle));
		
		activeLeftPC = mousePC;
		activeRightPC = rightStrongAIPC;
		
		leftHandPC = new HandPaddleController(lPaddle, onicr);
		rightHandPC = new HandPaddleController(rPaddle, onicr);

	}
	
	public void draw(){
		float dT = 1.0f / frameRate;
		//processInputs(); // user inputs
		
		onicr.update();
		
		// not necessary anymore, recalculate every cycle
		/*if(ballVelocity != ball.velocity){
			ball.velocity = ballVelocity;
			collisionDetections.get(cdIndex).init();
		}*/
		
		update(dT); // move objects
		
		
		
		render(); // draw
	}
	
	private void resetBall( boolean p1 ){
		ball.setLocation( p1 ? field.left + margin*0.5f : field.right - margin*0.5f,
				p1 ? lPaddle.getY() : rPaddle.getY());
		float pi = processing.core.PConstants.PI;
		ball.heading = p1 ? pi/4.0f : pi-pi/4.0f;
		ball.out = false;
	}
	
	private void render(){
		//clear bg
		background(0);
		
		
		
		//mirror image for better usability
		pushMatrix();
		scale(-1,1);		//flip across x axis

		//The x position is negative because we flipped
		image(onicr.depthImage(), -width, 0);
		//restore previous translation,etc
		popMatrix(); 
		
		
		
		
		strokeWeight(0);
		fill(0, 180); // don't let depth image be to intensive
		rect(0,0, width, height);
		
		
		
		
		//Draw field and score
		stroke(255);
		strokeWeight(10);
		textSize(scoreSize);
		textAlign(CENTER);
		fill(255);
		text(scoreP1 + " : " + scoreP2, field.horizontalCenter, scoreSize);
		
		//info display
		textSize(infoTextSize);
		textAlign(LEFT);
		fill(infoTextColor[0], infoTextColor[1], infoTextColor[2]);
		text(infoText, infoTextX, infoTextY);
		
		//left AI display
		textSize(AITextSize);
		textAlign(LEFT);
		fill(AITextColor[0], AITextColor[1], AITextColor[2]);
		text(leftAIText, leftAITextX, leftAITextY);
		
		//right AI display
		textSize(AITextSize);
		textAlign(RIGHT);
		fill(AITextColor[0], AITextColor[1], AITextColor[2]);
		text(rightAIText, rightAITextX, rightAITextY);
		
		// dashed line
		for(int i = field.top; i < field.bottom; i=i+20){
			line(field.horizontalCenter, i, field.horizontalCenter, i+10);
		}
		strokeWeight(0);
		fill(0xff, 80);
		rect(1,field.top, field.left, field.height);
		rect(field.right,field.top, width, field.height);
		
		strokeWeight(1);
		stroke(99);
		line(0,field.top-1,width,field.top-1);
		line(0,field.bottom,width,field.bottom);
		
		// draw Paddels
		lPaddle.draw();
		rPaddle.draw();
		
		// draw ball
		ball.draw(); 
	}
	
	public void update(float dT){
		
		activeLeftPC.update(dT);
		activeRightPC.update(dT);
		//activeRightPC.update(dT);
		
		
		/*if( leftAILevel != 0)
			leftPaddleAIs.get(leftAILevel-1).update(dT);
		lPaddle.update(dT);
		
		if( rightAILevel != 0)
			rightPaddleAIs.get(rightAILevel-1).update(dT);
		rPaddle.update(dT);
		*/
		
		
		int collision = collisionDetections.get(cdIndex).update(dT);
		if( collision > 0){
			scoreP1++;
			resetBall(true);
		} else if(collision < 0){
			scoreP2++;
			resetBall(false);
		}
		
		if (infoTextCountdown > 0)
			infoTextCountdown--;
		else
			infoText = "";
	}
	
	public void keyPressed( ) {
		//Move left paddle
		if(leftAILevel == 0){
			if( key == 'q' ) leftKeyPC.setMovement(-1);
			if( key == 'a' ) leftKeyPC.setMovement(1);
		}
		
		//Move right paddle
		if(rightAILevel == 0){
			if( key == 'o' ) rightKeyPC.setMovement(-1);
			if( key == 'l' ) rightKeyPC.setMovement(1);
		}
		
		//Increase ball speed
		if( key == '+'){ 
			ballVelocity += 20.0f;
			infoText = "Ball speed: " + ballVelocity;
			infoTextCountdown = infoTextTimeout;
		}
		//Decrease ball speed
		if( key == '-'){
			ballVelocity -= 20.0f;
			if( ballVelocity < 20.0f) ballVelocity = 20.0f;
			infoText = "Ball speed: " + ballVelocity;
			infoTextCountdown = infoTextTimeout;
		}
		
		//Increase framerate
		if( key == '*') { 
			fps += 5.0f;
			infoText = "Fps: " + fps;
			infoTextCountdown = infoTextTimeout;
		}
		//Decrease framerate
		if( key == '/'){
			fps -= 5.0f;
			if( fps < 1.0f) fps = 1.0f;
			infoText = "Fps: " + fps;
			infoTextCountdown = infoTextTimeout;
		}
		if(frameRate != fps ){
			frameRate(fps);
		}
		
/*		//AI for left paddle
		if( key == 'v' ){
			leftAILevel += 1;
			if(leftAILevel > leftPaddleAIs.size() ) leftAILevel = 0;
			
			if (leftAILevel == 0) {
				lPaddle.movement = 0;
				System.out.println("AI: off");
				leftAIText = "AI: off";
//				AITextCountdown = AITextTimeout;
			}
			else {
				System.out.println("AI: " + leftPaddleAIs.get(leftAILevel-1).getName() );
				leftAIText = "AI: " + leftPaddleAIs.get(leftAILevel-1).getName();
//				AITextCountdown = AITextTimeout;
			}
		}
		
		//AI for right paddle
		if( key == 'b' ){
			rightAILevel += 1;
			if(rightAILevel > rightPaddleAIs.size() ) rightAILevel = 0;
			
			if (rightAILevel == 0) {
				rPaddle.movement = 0;
				System.out.println("AI: off");
				rightAIText = "AI: off";
//				AITextCountdown = AITextTimeout;
			}
			else {
				System.out.println("AI: " + rightPaddleAIs.get(rightAILevel-1).getName() );
				rightAIText = "AI: " + rightPaddleAIs.get(rightAILevel-1).getName();
//				AITextCountdown = AITextTimeout;
			}
		}
		*/
		
		//Select type of collision detection
		/*
		if( key == 'c' ){
			cdIndex ++;
			if(cdIndex >= collisionDetections.size() ) cdIndex = 0;
			collisionDetections.get(cdIndex).init();
			System.out.println("Collision detection: "+ collisionDetections.get(cdIndex).getName());
			infoText = "Collision detection: "+ collisionDetections.get(cdIndex).getName();
			infoTextCountdown = infoTextTimeout;
		}
		*/
	}
	
	public void keyReleased( ) {
		if(leftAILevel == 0){
			if( key == 'q' && leftKeyPC.movement == -1) leftKeyPC.setMovement(0);
			if( key == 'a' && leftKeyPC.movement == 1) leftKeyPC.setMovement(0);
		}
		
		if(rightAILevel == 0){
			if( key == 'o' && rightKeyPC.movement == -1) rightKeyPC.setMovement(0);
			if( key == 'l' && rightKeyPC.movement == 1) rightKeyPC.setMovement(0);
		}
	}
	
	
	public void mouseMoved(){
		mousePC.setMousePosition(mouseX, mouseY);
	}
	
	public void onNewUser(int userId){
		System.out.println("new user: " + userId);
		onicr.requestCalibrationSkeleton(userId, true);
	}
	
	public void onLostUser(int userId){
		System.out.println("lost user: " + userId);
		if( userId == 1){
			activeLeftPC = prevLeftPC;
		} else if( userId == 2){
			activeLeftPC = prevRightPC;
		}
		
	}
	
	public void onEndCalibration( int userId, boolean successfull){
		System.out.println("calibration complete");
		if(successfull) onicr.startTrackingSkeleton(userId);
		
		if( userId == 1){
			prevLeftPC = activeLeftPC;
			activeLeftPC = leftHandPC;
		} else if( userId == 2){
			prevRightPC = activeLeftPC;
			activeRightPC = rightHandPC;
		}
	}

	
	public static void main(String args[]) {
		//PApplet.main(new String[] { "--present", "de.tub.mobint.assigment2.Pong" });
		PApplet.main(new String[] { "de.tub.mobint.assigment2.Pong" });
	}
}


