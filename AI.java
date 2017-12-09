package ARPG;

import java.awt.Rectangle;

public class AI extends Object{

	Rectangle rect;
	int size;
	public AI(){
		rect= new Rectangle(0,0,0,0);	
		size=0;
	}
	
	public Rectangle getRect(){
		return rect;
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size=size;
	}
	
	public void tick(){//override in subclass(Thinking method in the AIs)
		
	}
	
}
