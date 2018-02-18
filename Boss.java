package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

public class Boss extends AI{
	
	private int ticktime;
	private int currsprayduration;
	private int sprayduration;
	
	private boolean phase2;
	private boolean canspawnhealers;
	private int lasthealth;
	
	private boolean phase3;
	private boolean canspawnjumpers;
	
	public Boss(int x, int y, int size, double vel){
		super(x,y,size,vel);
		
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setMaxhealth(1000);
		setHealth(getMaxhealth());
		setRange(300);
		setShootcooldown(90);
		setSprayduration(120);
		setCurrsprayduration(getSprayduration());
		setCanshoot(false);
		setLasthealth(getMaxhealth());
	}
	
	public void tick(Play p){
		super.tick();
		setTicktime(getTicktime()+1);
		if(getTicktime()==360){
			setTicktime(0);
		}
		
		if(getHealth()<getMaxhealth()/2&&isPhase2()==false){
			setPhase2(true);
			setCanspawnhealers(true);
		}
		if(getHealth()<getMaxhealth()/8&&isPhase3()==false){//less than 13% hp
			setPhase3(true);
			setCanspawnjumpers(true);
		}
		if(isCanspawnhealers()==true){
			Healer h= new Healer(getMidx(),getMidy(),25,7);
			h.setMaxhealth(h.getMaxhealth());
			h.setHealth(h.getMaxhealth());
			p.AIs.add(h);
			setCanspawnhealers(false);
		}
		if(isCanspawnjumpers()==true){
			p.surroundSpawn(getMidx(), getMidy(), 30, 6, 4, 300);
			setCanspawnjumpers(false);
		}
		
		if(getHealth()<getLasthealth()){
			setInvunerableframes(120);
			Projectile projectile= new Projectile(getMidx(),getMidy(),getMidx(),getMidy(),7,1);
			projectile.setHurtsallies(false);
			projectile.setVel(0);
			projectile.setBomb(true); 
			projectile.setDamage(0);
			projectile.setColor("BLACK");
			projectile.setFriendlybomb(false);
			p.getProjectiles().add(projectile);
			setLasthealth(getHealth());
		}
		
		int smidx=(p.getSquare().getX()+(int)p.getSquare().getRect().getMaxX())/2;
		int smidy=(p.getSquare().getY()+(int)p.getSquare().getRect().getMaxY())/2;
		int midx=(int)(getX()+getX()+getRect().getWidth())/2;
		int midy=(int)(getY()+getY()+getRect().getHeight())/2;
		
		if(distance(midx,midy,smidx,smidy)<p.bm.scale*30){
			move((double)getVelx(),(double)getVely());
			if(canShoot()==true){//Shoot at player if they can
				getCooldown().tick();
				if(getCooldown().getTimer()>getShootcooldown()){
					if(getTicktime()%4==0&&isPhase2()==true){
						bombs(p);
					}
					explode(p);
					Point a= p.bm.getMapPos(getX(), getY());
					nextnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
					if(getCurrsprayduration()>0){
						setCurrsprayduration(getCurrsprayduration()-1);
					}
					else{
						setCurrsprayduration(getSprayduration());
						setCanshoot(false);
					}
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

	public void explode(Play p){
    	double interval=360/4;
    	Projectile[] ps= new Projectile[4];
    	for(int i=0;i<ps.length;i++){
    		Projectile po= new Projectile(getMidx(), getMidy(),getMidx(), getMidy(),7,400);
    		po.setDamage(5);
    		po.setVel(5);
    		po.setHurtsallies(true);
    		po.setSquareshot(true);
    		po.setColor("BLACK");
    		po.changeAngle((i*interval)+getTicktime());
    		ps[i]=po;
    	}
    	p.spawnMultipleProjectiles(ps);
	}
	
	public void bombs(Play p){
		double interval=360/4;
    	Projectile[] ps= new Projectile[4];
    	for(int i=0;i<ps.length;i++){
    		Projectile projectile= new Projectile(getMidx(), getMidy(),getMidx(), getMidy(),7,50);
			projectile.setHurtsallies(false);
			projectile.setVel(10);
			projectile.setDamage(30);
			projectile.setBomb(true); 
			projectile.setColor("RED");
			projectile.setFriendlybomb(false);
			projectile.setDelay(60);
			projectile.changeAngle((i*interval)+getTicktime()+10);
			ps[i]=projectile;
    	}
    	p.spawnMultipleProjectiles(ps);
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

	public int getTicktime() {
		return ticktime;
	}

	public void setTicktime(int ticktime) {
		this.ticktime = ticktime;
	}

	public boolean isPhase2() {
		return phase2;
	}

	public void setPhase2(boolean phase2) {
		this.phase2 = phase2;
	}

	public boolean isCanspawnhealers() {
		return canspawnhealers;
	}

	public void setCanspawnhealers(boolean canspawnhealers) {
		this.canspawnhealers = canspawnhealers;
	}

	public int getLasthealth() {
		return lasthealth;
	}

	public void setLasthealth(int lasthealth) {
		this.lasthealth = lasthealth;
	}

	public boolean isPhase3() {
		return phase3;
	}

	public void setPhase3(boolean phase3) {
		this.phase3 = phase3;
	}

	public boolean isCanspawnjumpers() {
		return canspawnjumpers;
	}

	public void setCanspawnjumpers(boolean canspawnjumpers) {
		this.canspawnjumpers = canspawnjumpers;
	}
}

