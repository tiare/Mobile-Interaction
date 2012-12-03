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
		
		parent.stroke(0);
		parent.strokeWeight(1);
		parent.line(0, 0, parent.width - margin*2, 0);
		parent.line(0, 0, 0, parent.height - margin*2 );
		
		//scale
		//maxX/600 =  x/1;
		
		double scaleX =  (parent.width - margin*2) / Math.ceil(maxX);
		double scaleY =  (parent.height - margin*2) / Math.ceil(maxY);
		
		float scale = (float)(scaleX < scaleY ? scaleX : scaleY);
		
		parent.scale(scale,scale);
				
//		parent.stroke(0);
//		parent.strokeWeight(1);
//		parent.line(0, 0, (float)Math.ceil(maxX+0.5), 0);
//		parent.line(0, 0, 0, (float)Math.ceil(maxY+0.5) );
		
//		parent.stroke(0);
//		parent.strokeWeight(1);
		parent.stroke(50, 50, 50);
		parent.strokeWeight(2);
		parent.line(0, (float)alpha, (float)maxX, (float)(alpha + maxX * beta));
		
		for( Trial trial : trials ){
			parent.fill(33);
			parent.noStroke();
			parent.ellipse((float)trial.getID(), (float)trial.getAvgMT(), 8/scale, 8/scale);
			
			//Display ID and AvgMT values for each trial
			parent.scale(1,-1); //Flip back to avoid upside down text
			parent.textSize(15/scale);
			parent.textAlign(PApplet.LEFT);
			parent.fill(190, 10, 0);
			float offset = 20/scale;
			parent.text( "ID: " + (float)Math.round(trial.getID()*100)/100,
					(float)trial.getID(), -(float)trial.getAvgMT()+offset);
			parent.text( "AvgM: " + (float)Math.round(trial.getAvgMT()*100)/100, 
					(float)trial.getID(), -(float)trial.getAvgMT()+2*offset);
			parent.scale(1,-1);
		}
		
		parent.scale(1/scale,-1/scale); //Unscale for fixed positions

		//Write alpha and beta values
		String infoText = "Alpha: " + (float)Math.round(alpha*100)/100 + ", Beta: " + (float)Math.round(beta*100)/100;
		int[] infoTextColor = {0, 12, 153};
		
		parent.textSize(15);
		parent.textAlign(PApplet.LEFT);
		parent.fill(infoTextColor[0], infoTextColor[1], infoTextColor[2]);
		parent.text(infoText, 0, margin);//-(float)parent.height/(2*scale));
		
		parent.stroke(0);
		parent.strokeWeight(5);
		//Label axes
		parent.textAlign(PApplet.LEFT);
		parent.fill(0);
		parent.text("ID", parent.width-50, margin); //x-axis
		parent.text("AvgMT", 5, -parent.height+50); //y-axis
		
		parent.popMatrix();
		
	}

}
