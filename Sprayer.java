package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

public class Sprayer extends AI{
	
	private int currsprayduration;
	private int sprayduration;

	public Sprayer(int x, int y, int size, double vel){
		super(x,y,size,vel);
		
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setMaxhealth(150);
		setHealth(getMaxhealth());
		setRange(300);
		setShootcooldown(90);
		setSprayduration(120);
		setCurrsprayduration(getSprayduration());
		setCanshoot(false);
	}
	
	public void tick(Play p) throws Exception{
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
					setAngle(p.s.findAngle(smidy-midy, smidx-midx));
					Projectile projectile= new Projectile(midx,midy,smidx-(int)(p.getSquare().getVelx()*5),smidy-(int)(p.getSquare().getVelx()*5),7,50);
					projectile.setHurtsallies(true);
					projectile.setVel(7);
					projectile.setDamage(3);
					p.getProjectiles().add(projectile);
					Point a= p.bm.getMapPos(getX(), getY());
					nextnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
					if(getCurrsprayduration()>0){
						if(getCurrsprayduration()==getSprayduration()-1){
							//System.out.println("Spray");
							p.sound.playeffect("Sprayer");
						}
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
