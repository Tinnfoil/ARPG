package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

public class Teleporter extends AI{
	Time shotcooldown = new Time();
	Time chargetime= new Time();
	private boolean canshoot;
	private boolean locked;
	private int shootcooldown;
	private int shootrange;
	private int currcastduration;
	private int castduration;
	double angle;
	public Teleporter(int x, int y, int size, double vel){
		super(x,y,size,vel);
		
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setMaxhealth(160);
		setHealth(getMaxhealth());
		setShootrange(400);
		setCastduration(60);
		setCurrcastduration(getCastduration());
		setShootcooldown(90);
		setCanshoot(false);
	}
	
	public void tick(Play p){
		super.tick();
		int smidx=(p.getSquare().getX()+(int)p.getSquare().getRect().getMaxX())/2;
		int smidy=(p.getSquare().getY()+(int)p.getSquare().getRect().getMaxY())/2;
		int midx=(int)(getX()+getX()+getRect().getWidth())/2;
		int midy=(int)(getY()+getY()+getRect().getHeight())/2;
		
		if(distance(midx,midy,smidx,smidy)<p.bm.scale*16){
			move((double)getVelx(),(double)getVely());
			if(canshoot==true){//Shoot at player if they can
				chargetime.tick();
				shotcooldown.tick();
				if(locked){
					setVelx(0);
					setVely(0);
					if(shotcooldown.time>getCastduration()){
						canshoot=false;
						locked=false;
					}
				}
				else if(distance(midx,midy,smidx,smidy)<shootrange-(shootrange/2)){
					double a=findAngle(smidy-midy,smidx-midx);
					//move((Math.cos(Math.toRadians(a))*vel),(Math.sin(Math.toRadians(a))*vel));
					setVelx((Math.cos(Math.toRadians(a))*(getMaxspeed()/2)));
					setVely((Math.sin(Math.toRadians(a))*(getMaxspeed()/2)));
				}
				else if(chargetime.getTimer()>getShootcooldown()){
		        	Circle co= new Circle(getMidx(),getMidy(),100,false,false,getCastduration()+1);
		        	co.setXoff(p.getSquare().getMidx()-getMidx()+(int)(p.getSquare().getVelx()*30));
		        	co.setYoff(p.getSquare().getMidy()-getMidy()+(int)(p.getSquare().getVely()*30));
		        	co.setCanteleport(true);
		        	p.spawnCircle(getMidx(), getMidy(), 100, false, false, getCastduration());
		        	p.spawnDelayedcircle(co,getCastduration());
					Point a= p.bm.getMapPos(getX(), getY());
					nextnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
					locked=true;
					shotcooldown.time=0;
				}
				else{
					setVelx(0);
					setVely(0);
				}
			}
			else if(path(smidx,smidy,shootrange+50,p)==true){// Path to player if player is further than the shootrange
				//Resets
				chargetime.time=0;
				canshoot=false;
			}
			else if(canshoot==false&&distance(smidx, smidy)<shootrange){//prepares to shoot!
				chargetime.time=0;
				canshoot=true;
			}
			else if(canshoot==false){//If player is between shoot and pathrange, simply follows player
				followPoint(midx,midy,smidx,smidy);
				shotcooldown.tick();
			}
		}
		}
	
	public boolean isCanshoot() {
		return canshoot;
	}
	public void setCanshoot(boolean canshoot) {
		this.canshoot = canshoot;
	}
	public int getShootcooldown() {
		return shootcooldown;
	}
	public void setShootcooldown(int shootcooldown) {
		this.shootcooldown = shootcooldown;
	}
	public int getShootrange() {
		return shootrange;
	}
	public void setShootrange(int shootrange) {
		this.shootrange = shootrange;
	}

	public int getCurrcastduration() {
		return currcastduration;
	}

	public void setCurrcastduration(int currcastduration) {
		this.currcastduration = currcastduration;
	}

	public int getCastduration() {
		return castduration;
	}

	public void setCastduration(int castduration) {
		this.castduration = castduration;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
