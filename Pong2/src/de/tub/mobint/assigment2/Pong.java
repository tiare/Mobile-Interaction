package de.tub.mobint.assigment2;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.LinkedList;

import processing.core.PApplet;
import SimpleOpenNI.SimpleOpenNI;
import de.tub.mobint.assigment2.ai.PerfectAI;
import de.tub.mobint.assigment2.ai.SimpleAI;
import de.tub.mobint.assigment2.collision.CollisionDetection;
import de.tub.mobint.assigment2.collision.PreciseCollisionDetection;
import de.tub.mobint.assigment2.gui.HandMarker;
import de.tub.mobint.assigment2.gui.HandToPointerPositionAdapter;
import de.tub.mobint.assigment2.gui.InfoText;
import de.tub.mobint.assigment2.gui.ResetCommand;
import de.tub.mobint.assigment2.gui.RingButton;
import de.tub.mobint.assigment2.gui.icon.AiIcon;
import de.tub.mobint.assigment2.gui.icon.HandIcon;
import de.tub.mobint.assigment2.gui.icon.KeyIcon;
import de.tub.mobint.assigment2.gui.icon.MouseIcon;
import de.tub.mobint.assigment2.gui.icon.ResetIcon;
import de.tub.mobint.assigment2.paddle.AIPaddleController;
import de.tub.mobint.assigment2.paddle.HandPaddleController;
import de.tub.mobint.assigment2.paddle.KeyPaddleController;
import de.tub.mobint.assigment2.paddle.MousePaddleController;
import de.tub.mobint.assigment2.paddle.Paddle;

public class Pong extends PApplet {

	private OpenNiControlRecognition onicr;
	
	/**
	 * @author Marcel Karsten (343619), Tiare Feuchtner (343...)
	 */
	private static final long serialVersionUID = 1L;

	int width = 640;
	int height = 480;
	
	float ballVelocity; // px/s
	float ballSpeedIncrease = 0;
	float ballSpeedStepSize = 20.0f;
	float fps = 30.0f;
	
	int margin = 50;
	
	int scoreSize = 40;
	
	float resetTimeout = 0.8f;
	float timeout;
	int collisionScore;
	boolean player1;

	User user1;
	User user2;
	
	InfoText commonInfo;
	InfoText scoreBoard;
		
	Field field;
	Ball ball;
	Paddle lPaddle;
	Paddle rPaddle;
	
	MousePaddleController mousePC;
	KeyPaddleController leftKeyPC;
	KeyPaddleController rightKeyPC;
	
	CollisionDetection collisionDetection;
	
	// Test
	RingButton leftBallSpeedButton;
	RingButton rightBallSpeedButton;
	
	RingButton resetButton;
	
	boolean deviceConnected;
	
	LinkedList<Integer> waitingUsers;
	
	@Override
	public void setup(){
		size(width,height,P2D);
		background(0);
		frameRate(fps);
		
		smooth();
		
		// OpenNI stuff
		
		//context = new SimpleOpenNI(this, SimpleOpenNI.RUN_MODE_MULTI_THREADED);
		deviceConnected = SimpleOpenNI.deviceCount() > 0; 
		if( deviceConnected ){
			onicr = new OpenNiControlRecognition(this, SimpleOpenNI.RUN_MODE_SINGLE_THREADED);
			onicr.enableDepth();
			onicr.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
		}
		
		
		field = new Field(scoreSize + margin/2, height - margin, margin, width-margin, 0, width);
		
		ball = new Ball(this);
		ballVelocity = ball.velocity;
		
		lPaddle = new Paddle(this, field.left, field.verticalCenter, 1, field.getLeftArea(ball.strokeWeight));
		rPaddle = new Paddle(this, field.right, field.verticalCenter, -1, field.getRightArea(ball.strokeWeight));
		
		resetBall(false);

		collisionDetection = new PreciseCollisionDetection(ball, field, lPaddle, rPaddle);
		timeout = 0;
		collisionScore = 0;
		player1 = false;
		
		// hold references to update on mouse or keyboard action
		mousePC = new MousePaddleController(lPaddle,new MouseIcon(this));
		leftKeyPC = new KeyPaddleController(lPaddle,new KeyIcon(this));
		rightKeyPC = new KeyPaddleController(rPaddle,new KeyIcon(this));
		
		// Info text
		commonInfo = new InfoText(this, color(0,102,153), new Point2D.Float(60,60));
		
		InfoText leftPCInfo = new InfoText(this, color(52,190,68), new Point2D.Float(60,470));
		leftPCInfo.timeout = -1;
		
		InfoText rightPCInfo = new InfoText(this, color(52,190,68), new Point2D.Float(575,470));
		rightPCInfo.timeout = -1;
		rightPCInfo.align = PApplet.RIGHT;
		
		scoreBoard = new InfoText( this, color(255), new Point2D.Float(field.horizontalCenter, scoreSize));
		scoreBoard.size = scoreSize;
		scoreBoard.align = CENTER;
		
		
		user1 = new User(	lPaddle,
							Arrays.asList(
								mousePC,
								new AIPaddleController(lPaddle, new AiIcon(this), new SimpleAI(ball, field, lPaddle)),
								new HandPaddleController(lPaddle, new HandIcon(this)),
								leftKeyPC
							),
							onicr,
							leftPCInfo,
							new HandMarker(new HandIcon(this))
						);
		
		user2 = new User(	rPaddle,
							Arrays.asList(
								new AIPaddleController(rPaddle,new AiIcon(this), new PerfectAI(ball, field, rPaddle, lPaddle)),
								new HandPaddleController(rPaddle, new HandIcon(this)),
								rightKeyPC
							),
							onicr,
							rightPCInfo,
							new HandMarker(new HandIcon(this))
						);
		
		waitingUsers = new LinkedList<Integer>();
		
		resetButton = new RingButton(this, new Point2D.Float(field.horizontalCenter, field.bottom+30), 30.0f, new ResetIcon(this));
		resetButton.setStayActive(false);
		resetButton.setColor(this.color(0,0,255));
		
		user1.addRightHandPositionListener(new HandToPointerPositionAdapter( resetButton ));
		user2.addLeftHandPositionListener(new HandToPointerPositionAdapter(resetButton));
		
		resetButton.addButtonActivationListener(new ResetCommand(this, resetButton));

	}
	
	public void draw(){
		float dT = 1.0f / frameRate;
		
//		if(ballVelocity > ball.velocity){
//			ball.velocity = ballVelocity;
//		}
		
		if(deviceConnected){
			onicr.update();
		}
		
		update(dT); // move objects
		
		render(dT); // draw
	}
	
	private void resetBall( boolean p1 ){
//		ball.setLocation( p1 ? field.left + margin*0.5f : field.right - margin*0.5f,
//				p1 ? lPaddle.getY() : rPaddle.getY());
		ball.setLocation(new Point(field.horizontalCenter,field.verticalCenter));
		float pi = processing.core.PConstants.PI;
		ball.heading = p1 ? pi/4.0f : pi-pi/4.0f;
		ball.out = false;
		ball.velocity = ballVelocity + ballSpeedIncrease;
		ballSpeedIncrease += ballSpeedStepSize;
	}
	
	private void render(float dT){
		//clear bg
		background(0);
		
		
		if( deviceConnected ){
			//mirror image for better usability
			pushMatrix();
			scale(-1,1);		//flip across x axis
	
			//The x position is negative because we flipped
			image(onicr.depthImage(), -width, 0);
			popMatrix();  //restore previous translation,etc 
		}
		
		strokeWeight(0);
		fill(0, 180); // don't let depth image be to intensive
		rect(0,0, width, height);
		
		
		//Draw field and score
		scoreBoard.setText(user1.score + " : " + user2.score);
		scoreBoard.draw();
		
		//underline asus controller player score
		/*
		strokeWeight(1);
		if(user1.isInUse()){
			line(field.horizontalCenter-10, scoreSize+5, field.horizontalCenter-20, scoreSize+5);
		}
		
		if(user2.isInUse()){
			line(field.horizontalCenter+10, scoreSize+5, field.horizontalCenter+20, scoreSize+5);
		}*/
		
		//info display
		commonInfo.draw();
		
		// dashed line
		int lineBot;
		strokeWeight(10);
		for(int i = field.top; i < field.bottom; i=i+20){
			lineBot = i + 10;
			if( lineBot >= field.bottom ){
				lineBot = field.bottom;
			}
			
			line(field.horizontalCenter, i, field.horizontalCenter, lineBot);
		}
		// out field
		strokeWeight(0);
		fill(0xff, 80);
		rect(1,field.top, field.left, field.height);
		rect(field.right,field.top, width, field.height);
		// horizontal bounds
		strokeWeight(1);
		stroke(99);
		line(0,field.top-1,width,field.top-1);
		line(0,field.bottom,width,field.bottom);
		
		
		user1.draw(dT);
		user2.draw(dT);
		
		// draw ball
		ball.draw();
		
		//draw lost ball when reached outer border
		if(collisionScore != 0 && timeout >= 0)
			ball.drawLostBall(timeout, resetTimeout);
		
		resetButton.draw(dT);
	}
	
	public void update(float dT){
		user1.update(dT);
		user2.update(dT);
		
		if (timeout > 0) {
			timeout -= dT;
			return;
		}
		if (collisionScore != 0)
			resetBall(player1);
		
		int collision = collisionDetection.update(dT);
		collisionScore = collision;
		if (collisionScore != 0) {
			timeout = resetTimeout;
		}
		
		if(collisionScore > 0){
			user1.score++;
			player1 = true;
			//resetBall(true);
		} else if(collisionScore < 0){
			user2.score++;
			player1 = false;
			//resetBall(false);
		}
	}
	
	public void keyPressed( ) {
		//Move left paddle
		if( key == 'q' ) leftKeyPC.setMovement(-1);
		if( key == 'a' ) leftKeyPC.setMovement(1);
		
		//Move right paddle
		if( key == 'o' ) rightKeyPC.setMovement(-1);
		if( key == 'l' ) rightKeyPC.setMovement(1);
		
		//Increase ball speed
		if( key == '+'){ 
			ballVelocity += 20.0f;
			commonInfo.setText("Ball speed: " + ballVelocity);
		}
		//Decrease ball speed
		if( key == '-'){
			ballVelocity -= 20.0f;
			if( ballVelocity < 20.0f) ballVelocity = 20.0f;
			commonInfo.setText("Ball speed: " + ballVelocity);
		}
		
		//Increase framerate
		if( key == '*') { 
			fps += 5.0f;
			commonInfo.setText("Fps: " + fps);
		}
		//Decrease framerate
		if( key == '/'){
			fps -= 5.0f;
			if( fps < 1.0f) fps = 1.0f;
			commonInfo.setText( "Fps: " + fps );
		}
		if(frameRate != fps ){
			frameRate(fps);
		}
		

	}
	
	public void keyReleased( ) {
		if( key == 'q' && leftKeyPC.movement == -1) leftKeyPC.setMovement(0);
		if( key == 'a' && leftKeyPC.movement == 1) leftKeyPC.setMovement(0);
		
		if( key == 'o' && rightKeyPC.movement == -1) rightKeyPC.setMovement(0);
		if( key == 'l' && rightKeyPC.movement == 1) rightKeyPC.setMovement(0);
	}
	
	
	public void mouseMoved(){
		mousePC.setMousePosition(mouseX, mouseY);
		
		user1.emulateHand(mouseX, mouseY);
	}
	
	public void onNewUser(int userId){
		System.out.println("new user: " + userId);
		onicr.requestCalibrationSkeleton(userId, true);
	}
	
	public void onLostUser(int userId){
		System.out.println("lost user: " + userId);
		if( user1.lostUser(userId) ){
			if( waitingUsers.size() > 0){
				user1.id = waitingUsers.removeFirst();
				System.out.println("took user from waiting list");
			}
		} else if( user2.lostUser(userId) ){
			if( waitingUsers.size() > 0){
				user2.id = waitingUsers.removeFirst();
				System.out.println("took user from waiting list");
			}
		} else {
			// remove from waiting list
			waitingUsers.remove(new Integer(userId));
			System.out.println("removed user from waiting list");
		}
		
	}
	
	public void onEndCalibration( int userId, boolean successfull){
		System.out.println("calibration complete");
		if(successfull){
			onicr.startTrackingSkeleton(userId);
			
			if( !user1.isInUse() ){
				user1.id = userId;
			} else if ( !user2.isInUse() ){
				user2.id = userId;
			} else {
				waitingUsers.add(userId);
			}
		}
	}

	public void resetGame(){
		user1.score = 0;
		user2.score = 0;
		ballSpeedIncrease = 0;
		resetBall(true);
	}
	
	public static void main(String args[]) {
		//PApplet.main(new String[] { "--present", "de.tub.mobint.assigment2.Pong" });
		PApplet.main(new String[] { "de.tub.mobint.assigment2.Pong" });
	}
}


