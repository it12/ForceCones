package forceconesmethod;

import processing.core.*;


public class ForceCone {
	
	//base point of force cone
	PVector base;
	//opening angle
	float phi;
	//directions limiting the cone
	PVector a1, b1;
	
	//!cone applies compression into positive axes direction!
	
	public ForceCone(Node n, Force F, float angle){
		this.base = n.getPosition();
		PVector axes = F.getDirection(); // already normalized
		PVector realDir;
		if (n.getType()==1){ //support points?
			realDir = PVector.mult(axes,-1);
		}else{
			realDir = axes;
		}
		phi = angle;
		
		a1 = new PVector(realDir.x* PApplet.cos(phi) + realDir.y* PApplet.sin(-phi), realDir.x* PApplet.sin(phi) + realDir.y* PApplet.cos(phi));
		b1 = new PVector(realDir.x* PApplet.cos(phi) + realDir.y* PApplet.sin(phi), realDir.x* PApplet.sin(-phi) + realDir.y* PApplet.cos(phi));


	}
	
	public PVector getBasePoint(){
		return this.base;
	}
	
	public float getAngle(){
		return this.phi;
	}
	public PVector getConeDirection2D(){		
		return this.a1;
	}
}
