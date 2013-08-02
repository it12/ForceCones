package forceconesmethod;

import processing.core.PVector;

/*
 * Support node only has a position right now
 * 
 */
public class SupportNode extends Node {

	
	public SupportNode(PVector pos, Force F, int nodeType) {
		super(pos, nodeType);
		// sets the force as opposite
		F.invertDirection();
		
	}

	public SupportNode(float x, float y, float z, Force F, int nodeType) {
		super(x, y, z, nodeType);
		// sets the force as opposite
		F.invertDirection();
	}

}
