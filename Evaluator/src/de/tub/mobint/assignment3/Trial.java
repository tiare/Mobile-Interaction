package de.tub.mobint.assignment3;

import java.util.Stack;
import java.util.Timer;


public class Trial {

	int width;
	int distance;
	public Stack<Click> hits;
	boolean nextHitLeft;
	Evaluator parent;
	
	Timer timer;
	
	
	public Trial(Evaluator parent,int width, int distance) {
		this.parent = parent;
		this.width = width;
		this.distance = distance;
		
		hits = new Stack<Click>();
	}
	
	public void stop(){
		
	}
	
	public boolean testClick(int x, int y){
		boolean hit = false;
		if( hits.size() == 0){
			if(hitLeftButton(x)){
				nextHitLeft = false;
				hit = true;
				
				// start timer
			}else if (hitRightButton(x)){
				nextHitLeft = true;
				hit = true;
				//start timer
			} else {
				return false;
			}
			
			timer = new Timer();// nach 2 Sek geht’s los
			timer.schedule  ( new TrialStop(this, parent), 10000 );
			
			
		} else {
			hit = nextHitLeft && hitLeftButton(x) || !nextHitLeft&&hitRightButton(x);
			hits.push( new Click(hit,x,y) );
			nextHitLeft = !nextHitLeft;
			
		}
		hits.push(new Click(hit,x,y));
		return hit;
	}
	
	private boolean hitLeftButton(int x){
		return x > parent.width/2 - distance/2 - width/2  && x < parent.width/2 - distance/2 + width/2;
	}
	
	private boolean hitRightButton(int x){
		return x > parent.width/2 + distance/2 - width/2  && x < parent.width/2 + distance/2 + width/2;
	}

	public void drawTest(){
		int leftCenter = parent.width/2 - distance/2;
		int rightCenter = parent.width/2 + distance/2;
		parent.strokeWeight(width);
		parent.stroke(255);
		parent.line(leftCenter,0,leftCenter,parent.height);
		parent.line(rightCenter,0,rightCenter,parent.height);
		
		drawClicks();
	}
	
	public void drawClicks(){
		for( Click click : hits){
			parent.noStroke();
			parent.fill(click.hit?parent.color(0,255,0):parent.color(255,0,0));
			parent.ellipse(click.x, click.y, 8, 8);
		}
	}
	
}
