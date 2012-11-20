package de.tub.mobint.assigment2;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.tub.mobint.assigment2.gui.ButtonActivationListener;
import de.tub.mobint.assigment2.gui.HandMarker;
import de.tub.mobint.assigment2.gui.HandToPointerPositionAdapter;
import de.tub.mobint.assigment2.gui.InfoText;
import de.tub.mobint.assigment2.gui.LeftHandPositionListener;
import de.tub.mobint.assigment2.gui.RightHandPositionListener;
import de.tub.mobint.assigment2.gui.RingButton;
import de.tub.mobint.assigment2.paddle.Paddle;
import de.tub.mobint.assigment2.paddle.PaddleController;


public class User implements ButtonActivationListener{

	public int id;
	public int score;
	Paddle paddle;
	PaddleController currentPC;
	RingButton currentRingButton;
	private List<PaddleController> paddleControllers;
	
	//private List<RingButton> pcButtons;
	private HashMap<RingButton, Integer> buttonToPCIndex;
	
	private List<LeftHandPositionListener> leftHandListener;
	private List<RightHandPositionListener> rightHandListener;
	
	OpenNiControlRecognition context;
	
	InfoText infoText;
	
	HandMarker alternativeHandMarker;
	
	public User( 	Paddle paddle,
					List<PaddleController> paddleControllers,
					OpenNiControlRecognition context, 
					InfoText infoText, 
					HandMarker alternativeHandMarker ){
		
		this.id = 0;
		this.paddle = paddle;
		this.paddleControllers = paddleControllers;
		
		this.leftHandListener = new LinkedList<LeftHandPositionListener>();
		this.rightHandListener = new LinkedList<RightHandPositionListener>();
		
		//this.pcButtons = new LinkedList<RingButton>();
		this.buttonToPCIndex = new HashMap<RingButton, Integer>();
		
		this.context = context;
		this.infoText = infoText;
		this.alternativeHandMarker = alternativeHandMarker;
		
		this.score = 0;
		
		// Prepare menu
		boolean left = Paddle.LEFT_SIDE == paddle.side; 
		
		if( left ){
			addRightHandPositionListener(this.alternativeHandMarker);
		} else {
			addLeftHandPositionListener(this.alternativeHandMarker);
		}
		
		Point2D.Float pos = new Point2D.Float(left?60:580,30);
		
		RingButton btn;
		int index = 0;
		for( PaddleController pc : paddleControllers){
			btn = new RingButton(paddle.parent, new Point2D.Float(pos.x, pos.y), 30, pc.getIcon());
			btn.addButtonActivationListener(this);
			buttonToPCIndex.put(btn, index);
			if( index == 0){
				btn.activate();
				currentRingButton = btn;
				
			}
			index++;
			
			if( left ){
				addRightHandPositionListener(new HandToPointerPositionAdapter(btn));
			} else {
				addLeftHandPositionListener(new HandToPointerPositionAdapter(btn));
			}
			
			
			
			pos.x += (left? 60.0f : -60.0f);

			
			
			try{
				if( left ){
					addLeftHandPositionListener( (LeftHandPositionListener)pc );
				} else {
					addRightHandPositionListener( (RightHandPositionListener)pc );
				}
			}catch( ClassCastException e){
				// ignore :)
			}
		}
		
		useController(0);
		
	}
	
	
	
	public void update( float dT){
		if(id > 0 && context.isTrackingSkeleton(id)){
			notifyLeftHandListener( context.getLeftHandPosition(id) );
			notifyRightHandListener( context.getRightHandPosition(id) );
		}
		
		currentPC.update(dT);
	}
	
	private void useController(int index){
		currentPC = paddleControllers.get(index);
		infoText.setText(currentPC.getName());
	}
	
	public void addLeftHandPositionListener(LeftHandPositionListener pl){
		leftHandListener.add(pl);
	}
	
	public void addRightHandPositionListener(RightHandPositionListener pl){
		rightHandListener.add(pl);
	}
	
	private void notifyLeftHandListener(Point2DDepth pos){
		for( LeftHandPositionListener pl : leftHandListener ){
			pl.leftHandPositionChanged(this, pos);
		}
	}
	
	private void notifyRightHandListener(Point2DDepth pos){
		for( RightHandPositionListener pl : rightHandListener ){
			pl.rightHandPositionChanged(this, pos);
		}
	}
	
	
	public boolean isInUse(){
		return id > 0;
	}
	
	public boolean lostUser( int userId ){
		if( userId == id ){
			id = 0;
			return true;
		}
		return false;
	}
	
	public void draw(float dT){
		paddle.draw();
		infoText.draw();
		alternativeHandMarker.draw();
		
		
		for( RingButton rc : buttonToPCIndex.keySet() ){
			rc.draw(dT);
			
		}
	}
	
	public void emulateHand(float x, float y){
		notifyRightHandListener(new Point2DDepth(x, y, 200.0f));
	}



	@Override
	public void buttonActivated(RingButton button) {
		if( currentRingButton != null){
			currentRingButton.deactivate();
		}
		useController( buttonToPCIndex.get(button) );
		currentRingButton = button;
		
	}

}
