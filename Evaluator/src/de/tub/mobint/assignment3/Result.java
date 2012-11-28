package de.tub.mobint.assignment3;

import java.util.ArrayList;

import processing.core.PApplet;

public class Result {

	PApplet parent;
	ArrayList<Trial> trials;
	double maxX = 2.8073549;
	double maxY = 0.8333;
	
	double beta;
	double alpha;
	
	double squaredError;
	double error;
	double indexOfPerformance;
	
	public Result( PApplet parent, ArrayList<Trial> trials) {
		this.parent = parent;
		this.trials = trials;
	}
	
	public void linearRegression(){
		double sumX = 0.0;
		double sumY = 0.0;
		for( Trial trial : trials ){
			if( trial.getID() > maxX) maxX = trial.getID();
			if( trial.getAvgMT() > maxY) maxY = trial.getAvgMT();
			sumX += trial.getID();
			sumY += trial.getAvgMT();
		}
		
		double avgX = sumX / trials.size();
		double avgY = sumY / trials.size();
		
		
		double avgXX=0.0;
		double avgXY=0.0;
		double avgYY=0.0;
		
		double x,y;
		for( Trial trial : trials){
			x = trial.getID() - avgX;
			y = trial.getAvgMT() - avgY;
			avgXX += x * x;
			avgXY += x * y;
			avgYY += y * y;
		}
		
		beta = avgXY / avgXX;
		alpha = avgY - beta * avgX;
		
		double ssr = 0.0;
		double fit;
		for(Trial trial : trials){
			fit = alpha  + trial.getID() * beta;
			ssr += (fit - avgY ) * ( fit - avgY); 
		}
		squaredError = ssr / avgY;
		
		// whats e? (slide 8)
		
		//System.out.println("maxX: " +maxX + "maxY" + maxY);
	}
	
	public void draw(){
		parent.background(255);
		drawGrid();
	}
	
	private void drawGrid(){
		int margin = 20;
		parent.pushMatrix();
		
		
		
		parent.scale(1,-1);		//flip across y axis
		parent.translate(margin, -parent.height+margin); // place origin
		
		//scale
		//maxX/600 =  x/1;
		
		double scaleX =  (parent.width - margin*2) / Math.ceil(maxX);
		double scaleY =  (parent.height - margin*2) / Math.ceil(maxY);
		
		float scale = (float)(scaleX < scaleY ? scaleX : scaleY);
		
		parent.scale(scale,scale);
		
		
		
		parent.stroke(0);
		parent.strokeWeight(1);
		parent.line(0, 0, (float)Math.ceil(maxX), 0);
		parent.line(0, 0, 0, (float)Math.ceil(maxY) );
		
		parent.fill(33);
		parent.noStroke();
		for( Trial trial : trials ){
			parent.ellipse((float)trial.getID(), (float)trial.getAvgMT(), 8/scale, 8/scale);
		}
		
		
		parent.stroke(0);
		parent.strokeWeight(1);
		parent.line(0, (float)alpha, (float)maxX, (float)(alpha + maxX * beta));
		
		
		/*scaled text geht nicht, muss dann außerhalb gesetzt werden :/
		 * parent.fill(0);
		parent.textAlign(PApplet.RIGHT, PApplet.TOP);
		parent.textSize(1/scale);
		parent.text("feaafadkhlajkd", 3, 1);
		*/
		parent.popMatrix();
		
		
		
		
	}
	
	
	

}
