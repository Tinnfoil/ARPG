package ARPG;

import java.awt.Rectangle;

public class Square extends Object
{
	
    private int width;
    private int height;
    private int health;
    private int maxhealth;
    private boolean attacking;
    private double attackangle;
    private int attackcooldown;
    private int currattackcooldown;
    private int attackamount;
    private boolean isDashing;//Boolean to determine whether or not the player is dashing.
    private int dashcooldown;
    private int currdashcooldown;
    /**
     * Constructor for objects of class Square
     */
    public Square()
    {
    	width=30;
    	height=30;
        setX(500);//4150
        setY(500);
        Rectangle rect= new Rectangle(getX(),getY(),width,height);
        setRect(rect);
        
        setMaxspeed(4);
        setAcceleration(.3);
        setHealth(100);//Base 100, can change
        setMaxhealth(getHealth());
        setInvunerable(false);
        isDashing=false;
        setDashcooldown(60);//60 frames
        setAttackcooldown(20);
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

	public int getDashcooldown() {
		return dashcooldown;
	}

	public void setDashcooldown(int dashcooldown) {
		this.dashcooldown = dashcooldown;
	}

	public int getCurrdashcooldown() {
		return currdashcooldown;
	}

	public void setCurrdashcooldown(int currdashcooldown) {
		this.currdashcooldown = currdashcooldown;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if(health>maxhealth){
		this.health=maxhealth;
		}
		this.health = health;
	}

	public int getMaxhealth() {
		return maxhealth;
	}

	public void setMaxhealth(int maxhealth) {
		this.maxhealth = maxhealth;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public double getAttackangle() {
		return attackangle;
	}

	public void setAttackangle(double attackangle) {
		this.attackangle = attackangle;
	}

	public int getAttackamount() {
		return attackamount;
	}

	public void setAttackamount(int attackamount) {
		this.attackamount = attackamount;
	}

	public int getAttackcooldown() {
		return attackcooldown;
	}

	public void setAttackcooldown(int attackcooldown) {
		this.attackcooldown = attackcooldown;
	}

	public int getCurrattackcooldown() {
		return currattackcooldown;
	}

	public void setCurrattackcooldown(int currattackcooldown) {
		this.currattackcooldown = currattackcooldown;
	}


    
}


