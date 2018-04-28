package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

public class Healer extends AI{
	
	private int currsprayduration;
	private int sprayduration;
	private AI targetAI;
	
	public Healer(int x, int y, int size, double vel){
		super(x,y,size,vel);
		
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setMaxhealth(70);
		setHealth(getMaxhealth());
		setRange(400);
		setShootcooldown(20);
		setSprayduration(10);
		setCurrsprayduration(getSprayduration());
		getCooldown().time=getShootcooldown();
		targetAI=this;
		//canshoot=false;
		
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
				if(getCooldown().getTimer()>getShootcooldown()){
					Projectile projectile= new Projectile(midx,midy,targetAI.getMidx()+(int)targetAI.getVelx()*10,targetAI.getMidy()+(int)targetAI.getVely()*10,7,(distance(midx,midy,targetAI.getMidx(),targetAI.getMidy())/30)+3);
					projectile.setVel(30);
					projectile.setHealamount(20);
					projectile.setColor("DARK_GREEN");
					projectile.setHeal(true);
					setAngle(projectile.getAngle());
					p.getProjectiles().add(projectile);
					if(getCurrsprayduration()>0){
						setCurrsprayduration(getCurrsprayduration()-1);
					}
					else{
						Point a= p.bm.getMapPos(getX(), getY());
						nextnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
						targetAI=findHealtarget(p);
						setCurrsprayduration(getSprayduration());
						setCanshoot(false);
					}
				}

				if(distance(midx,midy,smidx,smidy)<350){
					double a=findAngle(midy-smidy,midx-smidx);
					//move((Math.cos(Math.toRadians(a))*vel),(Math.sin(Math.toRadians(a))*vel));
					setVelx((Math.cos(Math.toRadians(a))*((getMaxspeed()*5)/7)));
					setVely((Math.sin(Math.toRadians(a))*((getMaxspeed()*5)/7)));
				}
				else{
					targetAI=findHealtarget(p);
					setVelx(0);
					setVely(0);
				}
			}
			else if(canShoot()==false&&distance(targetAI.getMidx(),targetAI.getMidy())<getRange()){//prepares to shoot!
				getCooldown().time=0;
				setCanshoot(true);
			}
			else if(path(targetAI.getMidx(),targetAI.getMidy(),getRange()+50,p)==true){// Path to player if player is further than the shootrange
				//Resets
				getCooldown().time=0;
				setCanshoot(false);
			}
			else if(canShoot()==false){//If player is between shoot and pathrange, simply follows player
				//System.out.println("TRIGER");
				followPoint(midx,midy,targetAI.getMidx(),targetAI.getMidy());
			}
		}
	}
	
	public AI findHealtarget(Play p){
		Point point= new Point(getMidx(),getMidy());
		int k=0;
		int min=(getHealth()*100)/getMaxhealth();//per
		for(int i=0;i<p.AIs.size();i++){
			AI a= p.AIs.get(i);
			if(percentHealth(a)<min){
				k=i;
				point.setLocation(a.getMidx(),a.getMidy());
			}
		}
		return p.AIs.get(k);
	}
	
	public int percentHealth(AI a){
		return (a.getHealth()*100)/a.getMaxhealth();
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
