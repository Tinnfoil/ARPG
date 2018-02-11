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
	private boolean hurtsenemy;
    private boolean hurtsallies;
    private boolean neutral;
    private int damage;
    public Hitbox(int x, int y, int width, int height,int lifetime){
    	Rectangle rect= new Rectangle(x,y,width,height);
    	setRect(rect);
    	setLifeTime(lifetime);
    }
    public Hitbox(){
    	super();
    }
    
    public void tick(){
    	setCurrentTime(getCurrentTime()+1);
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
	public boolean getNeutral() {
		return neutral;
	}
	public void setNeutral(boolean neutral) {
		if(neutral==true){
			setHurtsenemy(false);
			setHurtsallies(false);
		}
		this.neutral = neutral;
	}
	public boolean hurtsEnemy() {
		return hurtsenemy;
	}
	public void setHurtsenemy(boolean hurtsenemy) {
		this.hurtsenemy = hurtsenemy;
	}
	public boolean hurtsAllies() {
		return hurtsallies;
	}
	public void setHurtsallies(boolean hurtsallies) {
		this.hurtsallies = hurtsallies;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
    
}
