package forceconesmethod;

import processing.core.PVector;

public class ForceNode extends Node {

	// Force Vector
	Force forceVec;
	// Unit vector in direction of cone axis
	PVector axis;
	// Opening angle of the cone
	float angle = (float) (Math.PI / 4);
	// new cone object
	ForceCone fCone;
		
	public ForceNode(PVector pos, Force F, int nT) {
		super(pos, nT);
		fCone = new ForceCone(this, F, angle);

	}

	public ForceCone getCone() {
		return fCone;
	}
	
	public int getType() {
		return this.nodeType;
	}

}
