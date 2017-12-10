package ARPG;

import java.awt.Rectangle;

public class Square extends Object
{
	
    private int width;
    private int height;

    /**
     * Constructor for objects of class Square
     */
    public Square()
    {
    	width=50;
    	height=50;
        setX(51);
        setY(51);
        Rectangle rect= new Rectangle(getX(),getY(),50,50);
        setRect(rect);
    }
    
    public int getWidth(){
    	return width;
    }
    public int getHeight(){
    	return height;
    }



    
}


