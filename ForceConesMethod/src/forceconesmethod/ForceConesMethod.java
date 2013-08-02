package forceconesmethod;

import java.awt.Color;

//import main.cantilever;
//import processing.core.PApplet;
//import processing.core.PVector;
import processing.core.*;
import Jama.*;

public class ForceConesMethod extends PApplet {

	
	ForceNode[] myNodes;
	SupportNode[] mySuppNodes;
	Node[] intersectionNodes;

	// Number of force nodes
	int numNodes = 1;
	// Number of support nodes
	int numSupp = 2;
	// Number of intersection nodes is 2*numSupp*numNodes
	int numInter;
	
	//setup colors
	int B = color(0, 105, 180);
	int Y = color(251, 176, 59);
			

	public void setup() {
		//set size & 3d support
		size(1300, 800, P3D);
		
		//create nodes
		myNodes = new ForceNode[numNodes];
		mySuppNodes = new SupportNode[numSupp];
		
		//support notes are fixed for  the moment
		mySuppNodes[0] = new SupportNode(300, 500, 0);
		mySuppNodes[1] = new SupportNode(800, 500, 0);
		
		//number of intersection nodes is 2*numSupp*numNodes
		numInter = 2*numSupp*numNodes;
		intersectionNodes = new Node[numInter];

	}
	

	public void draw() {

		background(255);
		
		//vary force node for the moment by mouse tracking
		myNodes[0] = new ForceNode(mouseX, mouseY, 0, 0, -200, 0);

		//draw force nodes, support nodes and all cones
		for (int i = 0; i < numNodes; i++) {
			cpoint(myNodes[i], Color.red, 10);
			drawCone2d(myNodes[i].getCone(), null, false);
		}

		for (int i = 0; i < numSupp; i++) {
			cpoint(mySuppNodes[i], Color.black, 10);
			drawCone2d(myNodes[0].getCone(), mySuppNodes[i].getPosition(), true);
		}
		
		
		//compute intersections
		/*
		 * Every connection of 2 cones with the same force vector results in 2 intersection points, stored temporarily in Node[] sols.
		 * In case there is no intersection (eg. if base point lies on the other cone) the intersection node has coordinates Float.MAX_VALUE 
		 * (TODO think about what to do in that case - not so important for the moment...)
		 */
		
		Node[] sols = new Node[2];
		
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < numSupp; j++) {
				
				//intersect each force cone with each support cone
				sols = intersectWithSupport2d(myNodes[i].getCone(), mySuppNodes[j]);
				intersectionNodes[2*(i+j)] = sols[0]; 
				intersectionNodes[2*(i+j)+1] = sols[1]; 			
							
				//draw connecting lines to the force and support node
				cline(myNodes[i], sols[0], Color.green,1);
				cline(myNodes[i], sols[1], Color.green,1);
				cline(mySuppNodes[j], sols[0], Color.green,1);
				cline(mySuppNodes[j], sols[1], Color.green,1);
			}
		}
		
		//draw intersection nodes in green
		for(int i=0; i<numInter; i++){
			cpoint(intersectionNodes[i], Color.green, 10);
		}
		
		
		
/*		nline(allSols[0],allSols[2]);
		nline(allSols[0],allSols[3]);
		nline(allSols[1],allSols[3]);
		nline(allSols[1],allSols[2]);*/
	}

	
	//main method just starts processing applet 
	public static void main(String _args[]) {

		PApplet.main(new String[] { forceconesmethod.ForceConesMethod.class.getName() });
	}

	
	
	// Here starts the methods section, maybe move to an own class later

	//draw a point for a given node with color and stroke weight
	public void cpoint(Node n, Color col, int weight) {
		PVector pt = n.getPosition();
		strokeWeight(weight);
		stroke(col.getRed(), col.getGreen(), col.getBlue());
		point(pt.x, pt.y, pt.z);
	}
	
	//draw a line between two given nodes with color and stroke weight
	public void cline(Node n1, Node n2, Color col, int weight) {
		PVector pt1 = n1.getPosition();
		PVector pt2 = n2.getPosition();
		strokeWeight(weight);
		stroke(col.getRed(), col.getGreen(), col.getBlue());
		line(pt1.x, pt1.y, pt1.z, pt2.x, pt2.y, pt2.z);
	}

	
	//draw a force cone at a node
	public void drawCone2d(ForceCone cone, PVector basePt, Boolean supp) {
		
		//display attributes
		int scale = 5000;
		int fillAlpha = 25;
		int strokeW = 2;
		
		
		PVector base = cone.getBasePoint();
		float phi = cone.getAngle();

		PVector dir1 = new PVector(cone.getConeDirection2D().x, cone.getConeDirection2D().y);
		
		if(supp){
			base = basePt;
			dir1.mult(-1);
		}
		
		PVector dir2 = new PVector(dir1.x * PApplet.cos(2 * phi) + dir1.y
				* PApplet.sin(2 * phi), dir1.x * PApplet.sin(-2 * phi) + dir1.y
				* PApplet.cos(2 * phi));
		
		
		
		
		strokeWeight(strokeW);
		stroke(B);
		fill(B, fillAlpha);		
		
		line(base.x, base.y, base.x - scale * dir1.x, base.y - scale * dir1.y);
		line(base.x, base.y, base.x - scale * dir2.x, base.y - scale * dir2.y);
		
		triangle(base.x, base.y,base.x - scale * dir1.x, base.y - scale * dir1.y,base.x - scale * dir2.x, base.y - scale * dir2.y);

		strokeWeight(strokeW);
		
		stroke(Y);
		fill(Y, fillAlpha);
		
		line(base.x, base.y, base.x + scale * dir1.x, base.y + scale * dir1.y);
		line(base.x, base.y, base.x + scale * dir2.x, base.y + scale * dir2.y);
		
		triangle(base.x, base.y,base.x + scale * dir1.x, base.y + scale * dir1.y,base.x + scale * dir2.x, base.y + scale * dir2.y);

	}
	
	
	
	/*
	 * Intersect force node with support node. 
	 * 
	 */
	public Node[] intersectWithSupport2d(ForceCone cone1, SupportNode sn) {

		float phi = cone1.getAngle();
		PVector base1 = cone1.getBasePoint();
		
		/// a1 and b1 are the directions that produce the cone
		PVector a1 = cone1.getConeDirection2D();
		// rotate a1 by -2*phi
		PVector b1 = new PVector(a1.x * PApplet.cos(2 * phi) + a1.y
				* PApplet.sin(2 * phi), a1.x * PApplet.sin(-2 * phi) + a1.y
				* PApplet.cos(2 * phi));

		PVector base2 = sn.getPosition();

		Node[] interNodes = new Node[2];
		interNodes[0] = new Node(intersectLines2d(base1, a1, base2, b1));
		interNodes[1] = new Node(intersectLines2d(base1, b1, base2, a1));

		return interNodes;
	}

	/*
	 * base_i: base point, vec_i: direction 
	 * returns s,t such that base_1+s*vec_1 = base_2+t*vec_2
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
			PVector dir = PVector.mult(vec1, (float) sol.get(0,0));
			PVector.add(base1, dir, pt);
			return pt;
		}

	}

}
