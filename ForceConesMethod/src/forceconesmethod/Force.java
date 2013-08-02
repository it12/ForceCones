/**
 * 
 */
package forceconesmethod;
import processing.core.*;
/**
 * @author ddr
 *
 * creates a force object with a normalized direction and magnitude
 */
public class Force {

	/**
	 * @param args
	 */
	PVector direction;
	float magnitude;
	
	public Force(PVector dir, float mag){
		direction = dir;
		this.magnitude = mag;
	}
	
	public PVector getDirection(){
		return this.direction;
	}
	
	public float getMagnitude(){
		return this.magnitude;
	}
	
	public void setDirection(PVector newDir){
		this.direction = newDir;
	}
	
	public void setMagnitude(float newMag){
		this.magnitude = newMag;
	}
	public void invertDirection(){
		this.direction.mult(-1);
	}
}
