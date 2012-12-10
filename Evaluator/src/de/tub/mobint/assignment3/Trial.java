package de.tub.mobint.assignment3;

import java.util.Stack;
import java.util.Timer;

import processing.core.PApplet;


public class Trial {

	int trialNumber = 0;
	int width;
	int distance;
	public Stack<Click> clicks;
	boolean nextHitLeft;
	Evaluator parent;
	
	int errors;
	int hits;
	
	Timer timer;
	
	double duration; // in s;
	double avgMovementTime;
	double indexOfDifficulty;
	
	int[] insetColor1 = {190, 190, 190};
	int[] insetColor2 = {230, 230, 230};
	//TODO: add more
	
	public Trial(Evaluator parent,int width, int distance, int trialNumber) {
		this.parent = parent;
		this.width = width;
		this.distance = distance;
		this.trialNumber = trialNumber;
		
		clicks = new Stack<Click>();
		errors = 0;
		hits = 0;
		
		//duration = 10; //TODO: Make sure to change this!
		duration = 5;
	}
	
	public void stop(){
		avgMovementTime = duration / hits;
		indexOfDifficulty = Math.log(1.0+distance/width) / Math.log(2.0);
	}
	
	public boolean testClick(int x, int y){
		boolean hit = false;
		if( clicks.size() == 0){
			if(hitLeftButton(x)){
				nextHitLeft = false;
				hit = true;
			}else if (hitRightButton(x)){
				nextHitLeft = true;
				hit = true;
			} else {
				return false;
			}
			
			timer = new Timer();// nach 2 Sek geht’s los
			timer.schedule( new TrialStop(this, parent), Math.round(duration*1000) );
			
			
		} else {
			hit = nextHitLeft && hitLeftButton(x) || !nextHitLeft&&hitRightButton(x);
			nextHitLeft = !nextHitLeft;
		}
		
		pushClick( new Click(hit,x,y) );
		return hit;
	}
	
	private void pushClick(Click click){
		clicks.push(click);
		if(click.hit) hits++; else errors++;
	}
	
	private boolean hitLeftButton(int x){
		return x > parent.width/2 - distance/2 - width/2  && x < parent.width/2 - distance/2 + width/2;
	}
	
	private boolean hitRightButton(int x){
		return x > parent.width/2 + distance/2 - width/2  && x < parent.width/2 + distance/2 + width/2;
	}

	public void drawTest(){
		parent.textSize(15);
		parent.textAlign(PApplet.CENTER);
		parent.fill(0,0,255);
		parent.text( "Trial " + trialNumber, parent.width/2, 20);
		
		int leftCenter = parent.width/2 - distance/2;
		int rightCenter = parent.width/2 + distance/2;
		
		if( clicks.size() > 0) {
			if ( nextHitLeft ){
				//right bar inset	
				parent.strokeWeight(width);
				parent.stroke(255);
				parent.line(leftCenter,0,leftCenter,parent.height);
				
				parent.stroke(insetColor1[0], insetColor1[1], insetColor1[2]);
				parent.line(rightCenter,0,rightCenter,parent.height);
				
				parent.strokeWeight(width*0.9f);
				parent.stroke(insetColor2[0], insetColor2[1], insetColor2[2]);
				parent.line(rightCenter,0,rightCenter,parent.height);
			}	else {
				//left bar inset
				parent.strokeWeight(width);
				parent.stroke(255);
				parent.line(rightCenter,0,rightCenter,parent.height);
				
				parent.stroke(insetColor1[0], insetColor1[1], insetColor1[2]);
				parent.line(leftCenter,0,leftCenter,parent.height);
				
				parent.strokeWeight(width*0.9f);
				parent.stroke(insetColor2[0], insetColor2[1], insetColor2[2]);
				parent.line(leftCenter,0,leftCenter,parent.height);
			}
		} else {
			parent.strokeWeight(width);
			parent.stroke(255);
			parent.line(leftCenter,0,leftCenter,parent.height);
			parent.line(rightCenter,0,rightCenter,parent.height);
		}
		drawClicks();
	}
	
	public void drawClicks(){
		for( Click click : clicks){
			parent.noStroke();
			parent.fill(click.hit?parent.color(0,255,0):parent.color(255,0,0));
			parent.ellipse(click.x, click.y, 8, 8);
		}
	}
	
	public double getAvgMT(){
		return avgMovementTime;
	}
	
	public double getID(){
		return indexOfDifficulty;
	}
	
}
