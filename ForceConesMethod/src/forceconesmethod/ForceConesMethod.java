package forceconesmethod;

import java.awt.Color;

//import main.cantilever;
//import processing.core.PApplet;
//import processing.core.PVector;
import processing.core.*;
import Jama.*;
//libraries to connect with Grasshopper/Rhino
import oscP5.*;
import netP5.*;

public class ForceConesMethod extends PApplet {

	// myNodes will hold all my initial nodes (support and force)
	ForceNode[] myNodes;
	// SupportNode[] mySuppNodes;
	Node[] intersectionNodes;
	Force F;
	// colors
	int R, B, G, Y, K;
	int[] nodeColors, nodeTypes;

	// Number of force nodes, support nodes, intersection nodes
	int numNodes, numSupp, numInter, totalNodes;
	// array of support positions
	PVector[] sPos;

	// GRASHOPPER CONNECTION
	// declare oscP5 object
	OscP5 oscP5;
	// declare NetAdress object
	NetAddress myRemoteLocation;

	public void setup() {
		// set size & 3d support
		size(1300, 800, P3D);

		// create main force
		PVector fDirection;
		fDirection = new PVector(0, -1, 0);
		F = new Force(fDirection, 100);
		println(F.getDirection());

		// setup colors
		B = color(0, 105, 180);
		Y = color(251, 176, 59);
		R = color(255, 0, 0);
		G = color(140, 198, 63);
		K = color(0);
		nodeColors = new int[3];
		nodeColors[0] = R;
		nodeColors[1] = K;
		nodeColors[2] = G;

		// Number of force nodes and support nodes
		numNodes = 1;
		numSupp = 2;
		totalNodes = numNodes + numSupp;

		// define support positions
		sPos = new PVector[totalNodes];
		sPos[0] = new PVector(300, 500, 0);
		sPos[1] = new PVector(800, 500, 0);
		sPos[2] = new PVector(0, 0, 0);

		nodeTypes = new int[totalNodes];
		nodeTypes[0] = 1; // support
		nodeTypes[1] = 1; // support
		nodeTypes[2] = 0; // force

		// create nodes list
		myNodes = new ForceNode[totalNodes];

		// insert static support nodes in list
		for (int i = 0; i < totalNodes; i++) {
			myNodes[i] = new ForceNode(sPos[i], F, nodeTypes[i]);
	
		}

		// support notes are fixed for the moment
		// mySuppNodes[0] = new SupportNode(300, 500, 0, F);
		// mySuppNodes[1] = new SupportNode(800, 500, 0, F);

		// number of intersection nodes is 2*numSupp*numNodes
		int numInter = 2 * numSupp * numNodes;
		intersectionNodes = new Node[numInter];

		// //////////////////////////////////////////////////////////////
		// GRASHOPPER CONNECTION
		// initialize oscP5, sending from this port
		// oscP5 = new OscP5(this, 12000);
		// initialize myRemoteLocation. Sending to this port on local IP
		// netaddress
		// myRemoteLocation = new NetAddress("127.0.0.1", 12001);

	}

	public void draw() {

		background(255);

		// vary force node for the moment by mouse tracking
		myNodes[2].setPosition(new PVector(mouseX, mouseY, 0));

		// draw force nodes, support nodes and all cones
		// force nodes
		for (int i = 0; i < totalNodes; i++) {
			
				cpoint(myNodes[i]);			
				drawCone2d(myNodes[i]);
				drawVector(F, myNodes[i]);
			
			
			 tag(myNodes[i]);
		}

		// compute intersections
		/*
		 * Every connection of 2 cones with the same force vector results in 2
		 * intersection points, stored temporarily in Node[] sols. In case there
		 * is no intersection (eg. if base point lies on the other cone) the
		 * intersection node has coordinates Float.MAX_VALUE (TODO think about
		 * what to do in that case - not so important for the moment...)
		 */

		/*--------------------------------------------------------------------------
		
		Node[] sols = new Node[2];

		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < numSupp; j++) {

				// intersect each force cone with each support cone
				sols = intersectWithSupport2d(myNodes[i].getCone(),
						mySuppNodes[j]);
				intersectionNodes[2 * (i + j)] = sols[0];
				intersectionNodes[2 * (i + j) + 1] = sols[1];

				// draw connecting lines to the force and support node
				cline(myNodes[i], sols[0], Color.black, 3);
				cline(myNodes[i], sols[1], Color.black, 3);
				cline(mySuppNodes[j], sols[0], Color.black, 3);
				cline(mySuppNodes[j], sols[1], Color.black, 3);
			}
		}

		// draw intersection nodes in green
		for (int i = 0; i < numInter; i++) {
			cpoint(intersectionNodes[i], Color.red, 10);
		}
		-----------------------------------------------------------*/
		/*
		 * nline(allSols[0],allSols[2]); nline(allSols[0],allSols[3]);
		 * nline(allSols[1],allSols[3]); nline(allSols[1],allSols[2]);
		 */

		// GH
		// first message that is expected by GHowl
		// OscMessage myMessage = new OscMessage("/sending data");
		// adds data
		// PVector forcePos = myNodes[0].getPosition();

		// String coords = "(" + String.valueOf(forcePos.x) + ","
		// + String.valueOf(forcePos.y) + ")";
		/*
		 * fill(45, 23, 53); textSize(32); text(coords, mouseX, mouseY);
		 */

		// myMessage.add(coords);
		// send the arranged message to Grasshopper
		// oscP5.send(myMessage, myRemoteLocation);

	}

	// main method just starts processing applet
	public static void main(String _args[]) {

		PApplet.main(new String[] { forceconesmethod.ForceConesMethod.class
				.getName() });
	}

	// Here starts the methods section, maybe move to an own class later

	// draw a point for a given node with color and stroke weight
	public void cpoint(Node n) {
		PVector pt = n.getPosition();
		// draw point
		// println(n.getType());
		strokeWeight(10);
		stroke(nodeColors[n.getType()]);
		point(pt.x, pt.y, pt.z);

	}

	// draw a line between two given nodes with color and stroke weight
	public void cline(Node n1, Node n2, Color col, int weight) {
		PVector pt1 = n1.getPosition();
		PVector pt2 = n2.getPosition();
		strokeWeight(weight);
		stroke(col.getRed(), col.getGreen(), col.getBlue());
		line(pt1.x, pt1.y, pt1.z, pt2.x, pt2.y, pt2.z);
	}

	// draw a force cone at a node
	public void drawCone2d(ForceNode n) {

		// display attributes
		int scale = 5000;
		int fillAlpha = 25;
		int strokeW = 2;
		int nodeType = n.getType();		
		PVector base = n.getPosition();
		//get cone
		ForceCone cone = n.getCone();
		float phi = cone.getAngle();
		PVector dir1 = cone.getConeDirection2D();	
		
		//TODO can't we get this calculation in the ForceCone class??
		PVector dir2 = new PVector(dir1.x * PApplet.cos(2 * phi) + dir1.y
				* PApplet.sin(2 * phi), dir1.x * PApplet.sin(-2 * phi) + dir1.y
				* PApplet.cos(2 * phi));

		
		strokeWeight(strokeW);
		stroke(B);
		fill(B, fillAlpha);

		line(base.x, base.y, base.x - scale * dir1.x, base.y - scale * dir1.y);
		line(base.x, base.y, base.x - scale * dir2.x, base.y - scale * dir2.y);

		triangle(base.x, base.y, base.x - scale * dir1.x, base.y - scale
				* dir1.y, base.x - scale * dir2.x, base.y - scale * dir2.y);

		strokeWeight(strokeW);

		stroke(Y);
		fill(Y, fillAlpha);

		line(base.x, base.y, base.x + scale * dir1.x, base.y + scale * dir1.y);
		line(base.x, base.y, base.x + scale * dir2.x, base.y + scale * dir2.y);

		triangle(base.x, base.y, base.x + scale * dir1.x, base.y + scale
				* dir1.y, base.x + scale * dir2.x, base.y + scale * dir2.y);

	}

	// draw the force vector at specified location
	public void drawVector(Force f, Node n) {
		// vector to store end coordinated of vector arrow (end)
		//realDir is to get the real direction of the force (if support, is reverted)
		//TODO is there a cleaner way to do this? I tried everything and only this seemed to work...
		PVector realDir, end;
		PVector dir = f.getDirection();
		PVector pos = n.getPosition();
		dir.setMag(f.getMagnitude());
		// if is support node, invert direction and divide by 2
		if (n.getType() == 1) {
			realDir = PVector.mult(dir, -1);
		}else{
			realDir = dir;
		}
		end = PVector.sub(pos, realDir);
		// define style
		strokeWeight(2);
		stroke(G);
		// draw line
		line(pos.x, pos.y, end.x, end.y);
		// draw arrow
		pushMatrix();
		translate(end.x, end.y);
		float a = atan2(pos.x - end.x, end.y - pos.y);
		rotate(a);
		line(0, 0, -10, -10);
		line(0, 0, 10, -10);
		popMatrix();

	}

	/*
	 * Intersect force node with support node.
	 */
	public Node[] intersectWithSupport2d(ForceCone cone1, SupportNode sn) {

		float phi = cone1.getAngle();
		PVector base1 = cone1.getBasePoint();

		// / a1 and b1 are the directions that produce the cone
		PVector a1 = cone1.getConeDirection2D();
		// rotate a1 by -2*phi
		PVector b1 = new PVector(a1.x * PApplet.cos(2 * phi) + a1.y
				* PApplet.sin(2 * phi), a1.x * PApplet.sin(-2 * phi) + a1.y
				* PApplet.cos(2 * phi));

		PVector base2 = sn.getPosition();

		Node[] interNodes = new Node[2];
		// interNodes[0] = new Node(intersectLines2d(base1, a1, base2, b1));
		// interNodes[1] = new Node(intersectLines2d(base1, b1, base2, a1));

		return interNodes;
	}

	/*
	 * base_i: base point, vec_i: direction returns s,t such that base_1+s*vec_1
	 * = base_2+t*vec_2
	 */
	public PVector intersectLines2d(PVector base1, PVector vec1, PVector base2,
			PVector vec2) {
		float eps = 0.000001f;
		if (PVector.angleBetween(vec1, vec2) < eps)
			return new PVector(Float.MAX_VALUE, Float.MAX_VALUE);
		else {
			double[] abcd = new double[4];
			abcd[0] = -vec1.x;
			abcd[1] = -vec1.y;
			abcd[2] = -vec2.x;
			abcd[3] = -vec2.y;
			Matrix A = new Matrix(abcd, 2);

			double[] diff = new double[2];
			diff[0] = base1.x - base2.x;
			diff[1] = base1.y - base2.y;
			Matrix b = new Matrix(diff, 2);

			Matrix sol = A.solve(b);
			PVector pt = new PVector();
			PVector dir = PVector.mult(vec1, (float) sol.get(0, 0));
			PVector.add(base1, dir, pt);
			return pt;
		}

	}

	public void tag(Node n) {
		// draw tag
		PVector pos = n.getPosition();
		fill(0);
		textSize(12);
		textAlign(CENTER, TOP);
		text(n.getType(), pos.x, pos.y - 30);
	}

}
