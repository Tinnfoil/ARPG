package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

public class Pusher extends AI{
	
	public Pusher(int x, int y, int size, double vel){
		super(x,y,size,vel);
		
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setMaxhealth(200);
		setHealth(getMaxhealth());
		setRange(300);
		setShootcooldown(120);
		//canshoot=false;
		
	}
	
	public void tick(Play p) throws Exception{
		super.tick(p);
		int smidx=(p.getSquare().getX()+(int)p.getSquare().getRect().getMaxX())/2;
		int smidy=(p.getSquare().getY()+(int)p.getSquare().getRect().getMaxY())/2;
		int midx=(int)(getX()+getX()+getRect().getWidth())/2;
		int midy=(int)(getY()+getY()+getRect().getHeight())/2;
		setAngle(p.s.findAngle(smidy-midy, smidx-midx));
		if(distance(midx,midy,smidx,smidy)<p.bm.scale*30){
			move((double)getVelx(),(double)getVely());
			if(canShoot()==true){//Shoot at player if they can
				getCooldown().tick();
				if(getCooldown().time==getShootcooldown()){
					Projectile projectile= new Projectile(midx,midy,smidx+(int)(p.getSquare().getVelx()*15),smidy+(int)(p.getSquare().getVely()*15),7,(distance(midx,midy,smidx,smidy)/10)+10);
					projectile.setHurtsallies(false);
					projectile.setVel(10);
					projectile.setDamage(30);
					projectile.setBomb(true); 
					projectile.setColor("RED");
					projectile.setFriendlybomb(false);
					projectile.setDelay(30);
					p.getProjectiles().add(projectile);
					Point a= p.bm.getMapPos(getX(), getY());
					nextnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
					setCanshoot(false);
				}
				if(distance(midx,midy,smidx,smidy)<getRange()-50){
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
			}
		}
		}

}
