package ARPG;


public class Circle {
	
	int x;
	int y;
	int radius;
	private int lifetime;
	private int totallifetime;
	boolean hurtsenemy;
	boolean hurtsallies;
	private int spawndelay;
	
	private boolean canteleport;
	private int xoff;//Teleport 
	private int yoff;
	
	public Circle(int x, int y, int radius, boolean hurtsenemy, boolean hurtsallies, int lifetime){
		this.x=x;
		this.y=y;
		this.radius=radius;
		this.hurtsenemy=hurtsenemy;
		this.hurtsallies=hurtsallies;
		this.lifetime=lifetime;
		totallifetime=lifetime;
		
		canteleport=false;
		setXoff(0);
		setYoff(0);
		
		spawndelay=0;
	}
	
    public int getX(){
    	return x;
    }  
	public void setX(int x){
    	this.x=x;
    }
    public int getY(){
    	return y;
    }
    public void setY(int y){
    	this.y=y;
    }
    public int getRadius(){
    	return radius;
    }
    public boolean hurtsEnemy(){
    	return hurtsenemy;
    }
    public void setHurtsenemy(Boolean hurtsenemy){
    	this.hurtsenemy=hurtsenemy;
    }
    public boolean hurtsallies(){
    	return hurtsallies;
    }
    public void setHurtsallies(Boolean hurtsallies){
    	this.hurtsallies=hurtsallies;
    }
    public int getLifetime(){
    	return lifetime;
    }
    public void setLifetime(int lifetime){
    	this.lifetime= lifetime;
    }
    
	public void move(int x, int y){
	    setX((getX()+x));
	    setY((getY()+y));
	}
	public boolean contains(int x2, int y2){
		if((int)Math.sqrt(Math.pow(x2-x, 2)+Math.pow(y2-y, 2))<radius){
			return true;
		}
		return false;
	}
	
	public boolean intersects(Object o){
		if(spawndelay==0){
			if(o.getRect().contains(x,y)||contains(o.getX(),o.getY())||contains(o.getX()+(int)o.getRect().getWidth(),o.getY())
				||contains(o.getX(),o.getY()+(int)o.getRect().getHeight())||contains(o.getX()+(int)o.getRect().getWidth(),o.getY()+(int)o.getRect().getHeight())
				||o.getRect().contains(x+radius,y)||o.getRect().contains(x,y+radius)||o.getRect().contains(x-radius,y)||o.getRect().contains(x,y-radius)){
				return true;
			}
		}
		return false;
	}

	public int getSpawndelay() {
		return spawndelay;
	}

	public void setSpawndelay(int spawndelay) {
		this.spawndelay = spawndelay;
	}

	//Teleport
	public boolean canteleport() {
		return canteleport;
	}

	public void setCanteleport(boolean canteleport) {
		this.canteleport = canteleport;
	}

	public int getXoff() {
		return xoff;
	}

	public void setXoff(int xoff) {
		this.xoff = xoff;
	}

	public int getYoff() {
		return yoff;
	}

	public void setYoff(int yoff) {
		this.yoff = yoff;
	}

	public int getTotallifetime() {
		return totallifetime;
	}

	public void setTotallifetime(int totallifetime) {
		this.totallifetime = totallifetime;
	}
}
