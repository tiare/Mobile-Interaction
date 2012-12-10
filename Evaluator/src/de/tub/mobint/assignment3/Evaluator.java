package de.tub.mobint.assignment3;


import java.awt.Font;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;

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
	int trialIndex = -1;
	
	
	boolean nextHitLeft;
	
	Result result;
	Countdown countdown;
	PFont font;
	
	
	public Evaluator() {
		trials = new ArrayList<Trial>();
		
		trials.add( new Trial(this, 80,400,1) );
		trials.add( new Trial(this, 80,200,2) );
		trials.add( new Trial(this, 80,100,3) );
		
		trials.add( new Trial(this, 40, 200, 4) );
		trials.add( new Trial(this, 40, 100, 5) );
		trials.add( new Trial(this, 40, 50, 6) );
		
		trials.add( new Trial(this, 20,100,7) );
		trials.add( new Trial(this, 20,50,8) );
		trials.add( new Trial(this, 20,25,9) );
	}	
	
	@Override
	public void setup(){
		size(screenWidth,screenHeight,P2D);
		background(0xcc);
		
//		String [] fontList = PFont.list();
//		for (int i = 0; i<fontList.length; i++)
//			System.out.println(fontList[i]);
		// The font must be located in the sketch's 
		// "data" directory to load successfully
		font = createFont("Tahoma", 50);//loadFont("Tahoma-15.vlw");
		//System.out.println(font);
		//setFont(font.getFont());
		textFont(font);
		
		smooth();
		
		startScreenText = new InfoText(this, 0,screenWidth/2, screenHeight/2 );
		startScreenText.setText("Click to start");
		startScreenText.align = CENTER;
		startScreenText.countdown = -1;
		
		countdown = new Countdown(this);
		result = new Result(this, trials);
		
	}
	
	
	
	@Override
	public void draw(){
		background(0);
		switch( displayMode ){
		case START_SCREEN:
			drawStart();
			break;
		case COUNTDOWN_SCREEN:
			countdown.draw(1.0f/30.0f);
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
		case COUNTDOWN_SCREEN:
			break;
		case EVALUATION_SCREEN:
			trials.get(trialIndex).testClick(mouseX, mouseY);
			break;
		}
	}
	
	public void stopTrial(){
		//if( ++trialIndex < trials.size() )
			displayMode = COUNTDOWN_SCREEN;
		// else {
			//result.linearRegression();
			//displayMode = RESULT_SCREEN;
		//}
		 
	}
	
	public void stopCountdown(){
		if( ++trialIndex < trials.size() )
			displayMode = EVALUATION_SCREEN;
		else {
			result.linearRegression();
			displayMode = RESULT_SCREEN;
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] { "de.tub.mobint.assignment3.Evaluator" });
	}

}
