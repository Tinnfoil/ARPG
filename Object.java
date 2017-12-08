package ARPG;

public class Object {

    private int x;
    private int y;
    private double velx;
    private double vely;
    public Object(){
    	x=0;
    	y=0;
    	velx=0;
    	vely=0;
    }
    
    public int getX(){
    	return x;
    }
    public void setX(int x){
    	this.x=x;
    }
    
    public int getY(){
    	return y;
    }
    public void setY(int y){
    	this.y=y;
    }

	public double getVelx() {
		return velx;
	}

	public void setVelx(double velx){
		this.velx=velx;
	}

	public double getVely() {
		return vely;
	}

	public void setVely(double vely) {
		this.vely = vely;
	}
}
