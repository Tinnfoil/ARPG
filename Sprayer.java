package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

public class Sprayer extends AI{
	
	Time shotcooldown = new Time();
	Time chargetime= new Time();
	private boolean canshoot;
	private int shootcooldown;
	private int shootrange;
	private int currsprayduration;
	private int sprayduration;
	double angle;
	public Sprayer(int x, int y, int size, double vel){
		super(x,y,size,vel);
		
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setMaxhealth(150);
		setHealth(getMaxhealth());
		setShootrange(300);
		setShootcooldown(90);
		setSprayduration(120);
		setCurrsprayduration(getSprayduration());
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
				if(chargetime.getTimer()>getShootcooldown()){
					angle =p.s.findAngle(smidy-midy, smidx-midx);
					Projectile projectile= new Projectile(midx,midy,smidx-(int)(p.getSquare().getVelx()*5),smidy-(int)(p.getSquare().getVelx()*5),7,50);
					projectile.setHurtsallies(true);
					projectile.setVel(7);
					projectile.setDamage(3);
					p.getProjectiles().add(projectile);
					Point a= p.bm.getMapPos(getX(), getY());
					nextnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
					if(getCurrsprayduration()>0){
						setCurrsprayduration(getCurrsprayduration()-1);
					}
					else{
						setCurrsprayduration(getSprayduration());
						canshoot=false;
						shotcooldown.time=0;
					}
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

	public int getCurrsprayduration() {
		return currsprayduration;
	}

	public void setCurrsprayduration(int currsprayduration) {
		this.currsprayduration = currsprayduration;
	}

	public int getSprayduration() {
		return sprayduration;
	}

	public void setSprayduration(int sprayduration) {
		this.sprayduration = sprayduration;
	}
}
