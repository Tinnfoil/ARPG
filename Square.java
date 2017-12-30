package ARPG;

import java.awt.Rectangle;

public class Square extends Object
{
	
    private int width;
    private int height;
    private boolean isDashing;

    /**
     * Constructor for objects of class Square
     */
    public Square()
    {
    	width=50;
    	height=50;
        setX(70);
        setY(70);
        Rectangle rect= new Rectangle(getX(),getY(),50,50);
        setRect(rect);
        isDashing=false;
    }
    
    public int getWidth(){
    	return width;
    }
    public int getHeight(){
    	return height;
    }

    public boolean getDashing(){
    	return isDashing;
    }
    public void setDashing(boolean b){
    	isDashing=b;
    }


    
}


