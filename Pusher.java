package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

public class Pusher extends AI{
	
	Time shotcooldown = new Time();
	Time chargetime= new Time();
	private boolean canshoot;
	private int shootcooldown;
	private int shootrange;
	double angle;
	
	public Pusher(int x, int y, int size, double vel){
		super(x,y,size,vel);
		
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setMaxhealth(200);
		setHealth(getMaxhealth());
		setShootrange(300);
		shootcooldown=120;
		canshoot=false;
		
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
				if(chargetime.getTimer()>30&&shotcooldown.time==shootcooldown){
					angle =p.s.findAngle(smidy-midy, smidx-midx);
					Projectile projectile= new Projectile(midx,midy,smidx+(int)(p.getSquare().getVelx()*15),smidy+(int)(p.getSquare().getVely()*15),7,(distance(midx,midy,smidx,smidy)/10)+10);
					projectile.setHurtsallies(false);
					projectile.setVel(10);
					projectile.setBomb(true); 
					projectile.setFriendlybomb(false);
					projectile.setDelay(30);
					p.getProjectiles().add(projectile);
					Point a= p.bm.getMapPos(getX(), getY());
					nextnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
					shotcooldown.time=0;
					canshoot=false;
				}
				if(distance(midx,midy,smidx,smidy)<shootrange-30){
					double a=findAngle(midy-smidy,midx-smidx);
					//move((Math.cos(Math.toRadians(a))*vel),(Math.sin(Math.toRadians(a))*vel));
					setVelx((Math.cos(Math.toRadians(a))*(getMaxspeed()/2)));
					setVely((Math.sin(Math.toRadians(a))*(getMaxspeed()/2)));
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

	public int getShootcooldown(){
		return shootcooldown;
	}
	public void setShootcooldown(int shootcooldown){
		this.shootcooldown=shootcooldown;
	}
	public int getShootrange() {
		return shootrange;
	}

	public void setShootrange(int shootrange) {
		this.shootrange = shootrange;
	}
}
