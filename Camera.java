package ARPG;

public class Camera {

	private float currx;
	private float curry;
	private float lastx;
	private float lasty;
	int xoffset;
	int yoffset;
	
	public Camera(float x, float y, int xoff, int yoff){
		lastx=x;
		lasty=y;
		xoffset=xoff;
		yoffset=yoff;
		
	}
	
	public void move(Play p){
		currx = p.getSquare().getX() - xoffset ;
		curry=  p.getSquare().getY() - yoffset ;
		if(Math.abs(currx-lastx)>1){
			lastx=(((currx+lastx)/2)+lastx)/2;
			//lastx=(currx+lastx)/2;// 2 times as fast as the current camera ^
		}
		else{
			lastx=currx;
		}
		if(Math.abs(curry-lasty)>1){
			lasty=(((curry+lasty)/2)+lasty)/2;
			//lasty=(curry+lasty)/2;
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
