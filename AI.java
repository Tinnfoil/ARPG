package ARPG;

import java.awt.Rectangle;

public class AI extends Object{

	int size;
	public AI(int x, int y, int size){
		rect= new Rectangle(x,y,size,size);
		setRect(rect);
		this.size= size;
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
