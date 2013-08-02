package forceconesmethod;

import com.sun.org.apache.bcel.internal.generic.ISUB;

import processing.core.PVector;

public class ForceNode extends Node {

	// Force Vector
	PVector forceVec;

	// Unit vector in direction of cone axis
	PVector axis;

	// Opening angle of the cone
	float angle = (float) (Math.PI / 4);

	ForceCone fCone;

	public ForceNode(float posx, float posy, float posz, float forcex,
			float forcey, float forcez) {

		this(new PVector(posx, posy, posz), new PVector(forcex, forcey, forcez));

	}

	public ForceNode(PVector pos, PVector force) {

		super(pos);

		forceVec = new PVector(force.x, force.y, force.z);

		fCone = new ForceCone(pos, forceVec, angle);

	}

	public ForceCone getCone() {
		return fCone;
	}

}
