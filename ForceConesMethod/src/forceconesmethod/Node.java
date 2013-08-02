package forceconesmethod;

import processing.core.*;

/*
 * 
 * simple location node, only accepts and returns its coordinates
 * 
 * 
 */
public class Node {

	// Position of Node
	PVector position;
	int nodeType;

	// Constructor 1: needs position as a vector
	public Node(PVector pos, int nT) {
		this.position = new PVector(pos.x, pos.y, pos.z);
		this.nodeType = nT;
	}

	// Constructor 2: needs position as x y z coordinates
	public Node(float x, float y, float z, int nT) {
		this.position = new PVector(x, y, z);
		this.nodeType = nT;
	}

	// method to return node position
	public PVector getPosition() {
		return this.position;
	}

	public int getType() {
		return this.nodeType;
	}

	// method to modify node position
	public void setPosition(PVector newPos) {
		this.position = newPos;
	}

	public void setType(int newType) {
		this.nodeType = newType;
	}

}
