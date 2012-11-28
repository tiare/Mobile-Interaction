package de.tub.mobint.assignment3;


import java.util.ArrayList;

import processing.core.PApplet;

public class Evaluator extends PApplet {
	
	int screenWidth = 640;
	int screenHeight = 360;
	
	int displayMode = 0;
	final static int START_SCREEN = 0;
	final static int COUNTDOWN_SCREEN = 1;
	final static int EVALUATION_SCREEN = 2;
	final static int RESULT_SCREEN = 3;
	
	InfoText startScreenText;
	
	ArrayList<Trial> trials;
	int trialIndex = 0;
	
	
	boolean nextHitLeft;
	
	Result result;
	
	
	public Evaluator() {
		trials = new ArrayList<Trial>();
		trials.add( new Trial(this, 20,100) );
		
		trials.add( new Trial(this, 100,300) );
		
		
		result = new Result(this, trials);
	}	
	
	@Override
	public void setup(){
		size(screenWidth,screenHeight,P2D);
		background(0xcc);
		
		
		startScreenText = new InfoText(this, 0,screenWidth/2, screenHeight/2 );
		startScreenText.setText("Click to start");
		startScreenText.align = CENTER;
		startScreenText.countdown = -1;
		
		
	}
	
	
	
	@Override
	public void draw(){
		background(0);
		switch( displayMode ){
		case START_SCREEN:
			drawStart();
			break;
		case COUNTDOWN_SCREEN:
			drawCountdown();
			break;
			
		case EVALUATION_SCREEN:
			trials.get(trialIndex).drawTest();
			break;
			
		case RESULT_SCREEN:
			result.draw();
			break;
		
		}
	}
	
	private void drawStart(){
		background(0xcc);
		
		startScreenText.draw();
	}
	
	
	@Override
	public void mousePressed() {
		switch( displayMode){
		case START_SCREEN:
			displayMode = COUNTDOWN_SCREEN;
			break;
			
		case EVALUATION_SCREEN:
			trials.get(trialIndex).testClick(mouseX, mouseY);
			break;
		}
	}
	
	public void stopTrial(){
		if( ++trialIndex < trials.size() ){
			displayMode = COUNTDOWN_SCREEN;
		} else {
			result.linearRegression();
			displayMode = RESULT_SCREEN;
		}
		 
	}
	
	private void drawCountdown(){
		displayMode = EVALUATION_SCREEN;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] { "de.tub.mobint.assignment3.Evaluator" });
	}

}
