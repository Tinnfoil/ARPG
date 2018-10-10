package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Subclass of AI 
 * AI That follows the square and jumps at the square when within a distance from it. 
 */
public class Jumper extends AI{

	int jumpdistance;
	Time jumptime=new Time();
	boolean jumping;
	public Jumper(int x, int y, int size, double vel){
		super(x,y,size,vel);
		setMaxhealth(100);
		setHealth(getMaxhealth());
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		
		setRange(250);//pixels
		jumping =false;
		jumpdistance=300;
		setShootcooldown(50);
	}
	
	/**
	 * thinking and action method of the AI 
	 * method is constantly called by Play class.
	 * AI Movement System(Based on Square/Player movement)
	 * 1.Moves object according to velocities
	 * 2.Sets new velocities according to AI or methods
	 * 3.Collision system checks if the "next position" will collide with anything and fix velocities accordingly
	 * 4.Repeat
	 * @param p
	 */
	public void tick(Play p) throws Exception{//thinking methodd
		super.tick(p);
		int smidx=p.getSquare().getMidx();
		int smidy=p.getSquare().getMidy();
		int midx=(int)(getX()+getX()+getRect().getWidth())/2;
		int midy=(int)(getY()+getY()+getRect().getHeight())/2;
		
		if(distance(midx,midy,smidx,smidy)<p.bm.scale*30){
		move((double)getVelx(),(double)getVely());
		if(jumping==true){//Jumps at player after given charging time.
			getCooldown().tick();
			if(getCooldown().getTimer()>getShootcooldown()){//The actual jump
				launch(getMidx(),getMidy(),smidx,smidy);
				Point a= p.bm.getMapPos(getX(), getY());
				nextnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
			}
			else{
				if(getCooldown().getTimer()<getShootcooldown()-15){//The charge up period
					setAngle((int)findAngle((smidy-getMidy())+(int)p.s.getVely()*20, smidx-getMidx()+(int)p.s.getVelx()*20));
				}
				setVelx(0);
				setVely(0);
			}
		}
		else if(jumping==false&&distance(smidx, smidy)<getRange()){//prepares to jump
			getCooldown().time=0;
			jumping=true;
		}
		else if(path(smidx,smidy,getRange(),p)==true){// Path to player if player is further than the distance provide Jumprange(200)
			getCooldown().time=0;
			jumping=false;
		}
		else if(jumping==false&&distance(smidx, smidy)>getRange()){//If player is between jumprange and pathrange, simply follows player
			followPoint(midx,midy,smidx,smidy);
			//angle=(int) findAngle((smidy-getY())+(int)p.s.getVely()*10, smidx-getX()+(int)p.s.getVelx()*10);
			jumptime.time=0;
		}

		}
	}
	
	/**
	 * The jump of the jumper. jump in 
	 */
	public void launch(int x, int y, int midx, int midy){
			jumptime.tick();
			move((Math.cos(Math.toRadians(getAngle()))*10),(Math.sin(Math.toRadians(getAngle()))*10));
			if(jumptime.getTimer()>=30){
				jumping=false;
				jumptime.time=0;
			}
	}
	
	/**
	 * The extremely inaccurate form of jumping. Seems to be closer to dashing in the general location
	public void launch2(int x, int y, int midx, int midy){
		//System.out.println("Jumping");
		if(currjumpdistance<jumpdistance && distance(midx,midy)<jumprange){
			if(getVelx()<0){
				setVelx(getVelx()-.2);
			}
			else if(getVelx()>0){
				setVelx(getVelx()+.2);
			}
			if(getVely()<0){
				setVelx(getVelx()-.2);
			}
			else if(getVely()<0){
				setVely(getVely()+.2);
			}
		currjumpdistance+=40;
		}
		else{
			currjumpdistance=0;
			jumping=false;
		}

	}
	*/
	


}
