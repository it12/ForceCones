package forceconesmethod;

import processing.core.PVector;

public class Node {

	// Position of Node
	PVector position;

	// Constructor: needs position
	public Node(PVector pos) {

		position = new PVector(pos.x, pos.y, pos.z);

	}

	public Node(float x, float y, float z) {

		position = new PVector(x, y, z);

	}

	public PVector getPosition() {
		return position;
	}

	public void setPosition(PVector newPos) {
		position = newPos;
	}

}
