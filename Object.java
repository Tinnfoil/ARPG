package ARPG;

import java.awt.Rectangle;

public class Object {

    private int x;
    private int y;
    private double velx;
    private double vely;
    Rectangle rect;
    public Object(){
    	x=0;
    	y=0;
    	velx=0;
    	vely=0;
    	rect= new Rectangle(0,0,0,0);
    }
    
    public Rectangle getRect(){
    	return rect;
    }
    
    public void setRect(Rectangle rect){
    	this.rect=rect;
    }
    
    public void setRect(int x, int y, int width, int height){
    	rect.setBounds(x, y, width, height);
    }
    
    public int getX(){
    	return x;
    }
    
	public void setX(int x){
    	rect.translate(x-this.x,0);
    	this.x=x;
    }
    
    public int getY(){
    	return y;
    }
    public void setY(int y){
    	rect.translate(0,y-this.y);
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
