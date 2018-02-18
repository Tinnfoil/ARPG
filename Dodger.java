package ARPG;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class Dodger extends AI{
	
	Random RN= new Random();
	int jumpdistance;
	Time jumptime=new Time();
	boolean jumping;
	private int lasthealth;
	
	public Dodger(int x, int y, int size, double vel){
		super(x,y,size,vel);
		
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setMaxhealth(140);
		setHealth(getMaxhealth());
		setLasthealth(getMaxhealth());
		setRange(300);
		setShootcooldown(80);
		//canshoot=false;
		jumping =false;
		jumpdistance=300;
		
	}
	
	public void tick(Play p){
		super.tick();
		int smidx=(p.getSquare().getX()+(int)p.getSquare().getRect().getMaxX())/2;
		int smidy=(p.getSquare().getY()+(int)p.getSquare().getRect().getMaxY())/2;
		int midx=(int)(getX()+getX()+getRect().getWidth())/2;
		int midy=(int)(getY()+getY()+getRect().getHeight())/2;
		int distance =distance(midx,midy,smidx,smidy);
		
		if(distance<p.bm.scale*30){
			if(getHealth()<getLasthealth()){
				Projectile projectile= new Projectile(midx,midy,midx,midy,7,1);
				projectile.setHurtsallies(false);
				projectile.setVel(0);
				projectile.setBomb(true); 
				projectile.setDamage(0);
				projectile.setColor("BLACK");
				projectile.setFriendlybomb(false);
				p.getProjectiles().add(projectile);
				setLasthealth(getHealth());
			}
			if(distance(midx,midy,smidx,smidy)<140&&p.getSquare().isAttacking()&&dodgeAttack(p)){
				double a=findAngle(midy-smidy,midx-smidx);
				//move((Math.cos(Math.toRadians(a))*vel),(Math.sin(Math.toRadians(a))*vel));
				setVelx((Math.cos(Math.toRadians(a))*(getMaxspeed()*2)));
				setVely((Math.sin(Math.toRadians(a))*(getMaxspeed()*2)));
			}
			if(squareShot(p)){
				double a=findAngle(getMidy()-p.getSquare().getMidy(),getMidx()-p.getSquare().getMidx())+(90*(-1)*RN.nextInt(2));
				setVelx((Math.cos(Math.toRadians(a))*(getMaxspeed()*2)));
				setVely((Math.sin(Math.toRadians(a))*(getMaxspeed()*2)));
			}
			
			move((double)getVelx(),(double)getVely());
			
			if(canShoot()==true&&distance>=getRange()){//Shoot at player if they can
				getCooldown().tick();
				if(getCooldown().time>=getShootcooldown()){
					setAngle(p.s.findAngle(smidy-midy, smidx-midx));
					Circle co= new Circle(getMidx(),getMidy(),30,false,false,30+1);
		        	co.setXoff(p.getSquare().getMidx()-getMidx()+(int)(p.getSquare().getVelx()*30));
		        	co.setYoff(p.getSquare().getMidy()-getMidy()+(int)(p.getSquare().getVely()*30));
		        	co.setCanteleport(true);
		        	p.spawnCircle(getMidx(), getMidy(), 30, false, false, 30,0);
		        	p.spawnDelayedcircle(co,30,0);
		        	
					Projectile projectile= new Projectile(smidx+(int)(p.getSquare().getVelx()*30),smidy+(int)(p.getSquare().getVely()*30),smidx+(int)(p.getSquare().getVelx()*30),smidy+(int)(p.getSquare().getVely()*30),7,1);
					projectile.setHurtsallies(false);
					projectile.setVel(0);
					projectile.setBomb(true); 
					projectile.setFriendlybomb(false);
					projectile.setDelay(30);
					p.getProjectiles().add(projectile);
					Point a= p.bm.getMapPos(getX(), getY());
					nextnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
					setCanshoot(false);
				}
			}
			else if(canShoot()==true&&distance<getRange()){
				if(distance(midx,midy,smidx,smidy)>100){
					followPoint(midx,midy,smidx,smidy);
				}
				else{
					double a=findAngle(midy-smidy,midx-smidx);
					//move((Math.cos(Math.toRadians(a))*vel),(Math.sin(Math.toRadians(a))*vel));
					setVelx((Math.cos(Math.toRadians(a))*(getMaxspeed()/2)));
					setVely((Math.sin(Math.toRadians(a))*(getMaxspeed()/2)));
				}
				getCooldown().tick();
				if(getCooldown().time>=getShootcooldown()){
					Projectile projectile= new Projectile(midx,midy,smidx+(int)(p.getSquare().getVelx()*5),smidy+(int)(p.getSquare().getVely()*5),7,50);
					projectile.setHurtsallies(true);
					projectile.setColor("PURPLE");
					projectile.setVel(15);
					projectile.setDamage(30);
					p.getProjectiles().add(projectile);
					getCooldown().time=0;
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
	
	public void launch(int x, int y, int midx, int midy){
		jumptime.tick();
		move((Math.cos(Math.toRadians(getAngle()))*10),(Math.sin(Math.toRadians(getAngle()))*10));
		if(jumptime.getTimer()>=30){
			jumping=false;
			jumptime.time=0;
		}
	}
	
	public boolean dodgeAttack(Play p){
		double angle=findAngle(getMidy()-p.getSquare().getMidy(),getMidx()-p.getSquare().getMidx());
		if(p.getSquare().isAttacking()&&(p.getSquare().getAttackangle()>angle-90&&p.getSquare().getAttackangle()<angle+90)){
			return true;
		}
		return false;
	}
	
	public boolean squareShot(Play p){
		double angle=findAngle(getMidy()-p.getSquare().getMidy(),getMidx()-p.getSquare().getMidx());
		//System.out.println(angle);
		for(int i=0;i<p.getProjectiles().size();i++){
			Hitbox h= p.getProjectiles().get(i);
			if(h.getClass()==Projectile.class){
				if(((Projectile)h).isSquareshot()&&(((Projectile)h).getAngle()>angle-10&&((Projectile)h).getAngle()<angle+10)){
					return true;
				}
			}
		}
		return false;
	}

	public int getLasthealth() {
		return lasthealth;
	}

	public void setLasthealth(int lasthealth) {
		this.lasthealth = lasthealth;
	}

}
