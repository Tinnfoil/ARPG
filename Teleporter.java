package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

public class Teleporter extends AI{
	
	Time shotcooldown = new Time();
	private boolean locked;
	private int currcastduration;
	private int castduration;
	
	public Teleporter(int x, int y, int size, double vel){
		super(x,y,size,vel);
		
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setMaxhealth(150);
		setHealth(getMaxhealth());
		setRange(400);
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
		
		if(distance(midx,midy,smidx,smidy)<p.bm.scale*30){
			move((double)getVelx(),(double)getVely());
			if(canShoot()==true){//Shoot at player if they can
				getCooldown().tick();
				shotcooldown.tick();
				if(locked){
					setVelx(0);
					setVely(0);
					if(shotcooldown.time>getCastduration()){
						setCanshoot(false);
						locked=false;
					}
				}
				else if(distance(midx,midy,smidx,smidy)<getRange()-(getRange()/2)){
					double a=findAngle(smidy-midy,smidx-midx);
					//move((Math.cos(Math.toRadians(a))*vel),(Math.sin(Math.toRadians(a))*vel));
					setVelx((Math.cos(Math.toRadians(a))*(getMaxspeed()/2)));
					setVely((Math.sin(Math.toRadians(a))*(getMaxspeed()/2)));
				}
				else if(getCooldown().getTimer()>getShootcooldown()){
		        	Circle co= new Circle(getMidx(),getMidy(),100,false,false,getCastduration()+1);
		        	co.setXoff(p.getSquare().getMidx()-getMidx()+(int)(p.getSquare().getVelx()*30));
		        	co.setYoff(p.getSquare().getMidy()-getMidy()+(int)(p.getSquare().getVely()*30));
		        	co.setCanteleport(true);
		        	p.spawnCircle(getMidx(), getMidy(), 100, false, false, getCastduration(),0);
		        	p.spawnDelayedcircle(co,getCastduration(),10);
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
			else if(canShoot()==false&&distance(smidx, smidy)<getRange()){//prepares to shoot!
				getCooldown().time=0;
				setCanshoot(true);
			}
			else if(path(smidx,smidy,getRange()+50,p)==true){// Path to player if player is further than the shootrange
				//Resets
				getCooldown().time=0;
				setCanshoot(false);
			}
			else if(canShoot()==false){//If player is between shoot and pathrange, simply follows player
				followPoint(midx,midy,smidx,smidy);
				shotcooldown.tick();
			}
		}
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
