package ARPG;

import java.awt.Rectangle;

/**
 * Class designed to be the superClass of all things that deal damage. Deciding factor for whom 
 * the object "hurts"
 * A "friendly" hitbox object should not hurt the player and viceversa for Ais
 * @author Kenny
 *
 */
public class Hitbox extends Object{
	private int lifeTime;
	private int currentTime=0;
    private boolean isfriendly;
    public Hitbox(int x, int y, int width, int height,int lifetime){
    	Rectangle rect= new Rectangle(x,y,width,height);
    	setRect(rect);
    	setIsfriendly(true);
    	setLifeTime(lifetime);
    }
    public Hitbox(){
    	super();
    }
    
    public void tick(){
    	setCurrentTime(getCurrentTime()+1);
    }
    
	public boolean isIsfriendly() {
		return isfriendly;
	}
	public void setIsfriendly(boolean isfriendly) {
		this.isfriendly = isfriendly;
	}
	public int getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}
	public int getLifeTime() {
		return lifeTime;
	}
	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}  
    
}
