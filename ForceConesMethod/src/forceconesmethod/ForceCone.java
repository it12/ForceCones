package forceconesmethod;

import ddf.minim.analysis.CosineWindow;
import ddf.minim.signals.SineWave;
import processing.core.PApplet;
import processing.core.PVector;


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
	
	public ForceCone(PVector pos, PVector dir, float angle){
		base = new PVector(pos.x,pos.y,pos.z);
		axes = new PVector(dir.x,dir.y,dir.z);
		axes.normalize();
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
