package ARPG;

public class Camera {

	private float currx;
	private float curry;
	private float lastx;
	private float lasty;
	
	public Camera(float x, float y){
		lastx=x;
		lasty=y;
		
	}
	
	public void move(play p){
		currx=p.getSquare().getX()-300;
		curry=p.getSquare().getY()-300;
		if(Math.abs(currx-lastx)>1){
			lastx=(currx+lastx)/2;
		}
		else{
			lastx=currx;
		}
		if(Math.abs(curry-lasty)>1){
			lasty=(curry+lasty)/2;
		}
		else{
			lasty=curry;
		}
	
	}

	public float getY() {
		return lasty;
		//return curry;
	}

	public void setY(float y) {
		lasty = y;
	}

	public float getX() {
		return lastx;
		//return currx;
	}

	public void setX(float x) {
		lastx = x;
	}
	
	
}