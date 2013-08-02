package forceconesmethod;

import processing.core.*;


public class ForceCone {
	
	//base point of force cone
	PVector base;
	//direction of axes
	PVector axes;
	//opening angle
	float phi;
	//directions limiting the cone
	PVector a1, b1;
	
	//!cone applies compression into positive axes direction!
	
	public ForceCone(Node n, Force F, float angle){
		PVector base = n.getPosition();
		axes = F.getDirection(); // already normalized
		phi = angle;
		
		a1 = new PVector(axes.x* PApplet.cos(phi) + axes.y* PApplet.sin(-phi), axes.x* PApplet.sin(phi) + axes.y* PApplet.cos(phi));
		b1 = new PVector(axes.x* PApplet.cos(phi) + axes.y* PApplet.sin(phi), axes.x* PApplet.sin(-phi) + axes.y* PApplet.cos(phi));

	}
	
	public PVector getBasePoint(){
		return base;
	}
	
	public float getAngle(){
		return phi;
	}
	public PVector getConeDirection2D(){		
		return a1;
	}
}
