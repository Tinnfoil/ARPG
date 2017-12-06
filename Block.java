package ARPG;

import java.awt.Rectangle;

public class Block extends Object
{
	Rectangle rect;
	public Block(int x, int y, int width, int height){
		rect= new Rectangle(x,y,width,height);
		setX(x);
		setY(y);
		rect.setBounds(rect);
	}
	public boolean intersects(Rectangle rect2) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Rectangle getRect(){
		return rect;
	}
	
    public int getMaxX(){
    	return (int)(rect.getX()+rect.getWidth());
    }
    public int getMaxY(){
    	return (int)(rect.getY()+rect.getHeight());
    }
    public int getWidth(){
    	return (int)rect.getWidth();
    }
    public int getHeight(){
    	return (int)rect.getHeight();
    }
}
