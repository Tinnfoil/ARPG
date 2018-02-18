package ARPG;

import java.awt.Point;
import java.awt.Rectangle;

public class AI extends Object{

	int size;
    private int health;
    private int maxhealth;
 
    private Time cooldown= new Time();//Current cooldown of the AI's ability
    private int shootcooldown;//The cooldown of Ai's Ability (Jump has a 1 second cooldown)
    private boolean canshoot;//Whether or not the Ai can use it's ability
    private int range;//Range in which the ai uses it's ability
    private double angle;//Optional angle 
    
	Node lastnode;
	Node nextnode;
	boolean pathing;
	
	public AI(int x, int y, int size, double vel){
		rect= new Rectangle(x,y,size,size);
		setX(x);
		setY(y);
		health=100;
		maxhealth=100;
		
		setShootcooldown(60);
		setCanshoot(false);
		setRange(200);
		
		setStunframes(0);
		setVelx(vel);
		setVely(vel);
		setRect(rect);
		setSize(size);
		setMaxspeed(vel);
	}
	
	public int getSize(){
		return size;
	}
	
	public void setSize(int size){
		this.size=size;
	}
	
	public void tick(){//override in subclass(Thinking method in the AIs)
    	if(getVelx()>getMaxspeed()){
    		setVelx(getVelx()-.4);
    	}
    	else if(getVelx()<-getMaxspeed()){
    		setVelx(getVelx()+.4);
    	}
    	if(getVely()>getMaxspeed()){
    		setVely(getVely()-.4);
    	}
    	else if(getVely()<-getMaxspeed()){
    		setVely(getVely()+.4);
    	}
	}
	
	public boolean intersects(Rectangle rect){
		 return getRect().intersects(rect);
	}
	
	/**
	 * copy of collision system of square
	 * @param p
	 */
	public void collides(Play p){
		for(int i=0;i<p.getBlocks().size();i++){//Collision Checks
			Block b= p.getBlocks().get(i);
			int bmidx=(b.getX()+b.getMaxX())/2;
			int bmidy=(b.getY()+b.getMaxY())/2;
			if(distance(bmidx,bmidy)<200){
				if(intersectsBlocks(p.getBlocks())[i]){
					fixPosition(p.getBlocks().get(i));
				}
			}
		}
	}
	
	/**
	 * changes objects' velocities(acceleration based) 
	 * so it can follow the specified point(Does not move object)
	 * @param x object's x coordinate
	 * @param y object's y coordinate
	 * @param midx point's x coordinate
	 * @param midy point's y coordinate
	 */
	public void followPoint(int x, int y, int midx, int midy){
		if(Math.abs(x-midx)<=getVelx()){
			setVelx(0);
		}
		else if((x>midx)&&Math.abs(x-midx)>getVelx()){
			if(getVelx()>-getMaxspeed()){
				setVelx(getVelx()-.3);
				}
		}
		else if((x<midx)&&Math.abs(x-midx)>getVelx()){
			if(getVelx()<getMaxspeed()){
				setVelx(getVelx()+.3);
			}
		}

		if(Math.abs(y-midy)<=getVely()){
			setVely(0);
		}
		else if((y>midy)&&Math.abs(y-midy)>getVely()){
			if(getVely()>-getMaxspeed()){
			setVely(getVely()-.3);
			}
		}
		else if((y<midy)&&Math.abs(y-midy)>getVely()){
			if(getVely()<getMaxspeed()){
				setVely(getVely()+.3);
			}
		}
	}
	
	public boolean path(int smidx, int smidy, int pathrange,Play p){
		if(distance(smidx, smidy)>pathrange&&pathing==false){
			Point a= p.bm.getMapPos(getX(), getY());
			lastnode=new Node((int)a.getX(),(int)a.getY(),(int)a.getX(),(int)a.getY());
			nextnode=nextNode(lastnode,lastnode,smidx,smidy,p.bm);
			pathing=true;
		}
		else if(distance(smidx, smidy)>pathrange&&pathing==true){
			followPoint(getX()+getSize()/2,getY()+getSize()/2,(int)p.bm.getPixelPos(nextnode.i, nextnode.j).getY(),(int)p.bm.getPixelPos(nextnode.i, nextnode.j).getX());
			if((p.bm.getMapPos(getX()+getSize()/2,getY()+getSize()/2).getX()==nextnode.i&&p.bm.getMapPos(getX()+getSize()/2,getY()+getSize()/2).getY()==nextnode.j)){
				Node n= nextnode;
				nextnode=nextNode(lastnode,nextnode,smidy,smidx,p.bm);
				lastnode=n;
			}
		}
		else{
			return false;
		}
		return true;
	}
	
	public Node nextNode(Node lastnode, Node currnode, int x, int y, BlockMap bm){
		int j2=(int)bm.getMapPos(x, y).getX();//Square pos
		int i2=(int)bm.getMapPos(x, y).getY();//square pos
		Node nextnode= new Node(currnode.i,currnode.j, i2,j2);
		int min=(bm.scale*bm.map.length)*(bm.scale*bm.map.length);
		int length=bm.map.length;
		int width=bm.map[0].length;
		int i= currnode.i;
		int j= currnode.j;
		int li=lastnode.i;
		int lj=lastnode.j;
		int num1=distance((int)bm.getPixelPos(i,j+1).getX(),(int)bm.getPixelPos(i,j+1).getY(),x,y);
		if(j+1<width&&bm.map[i][j+1]==0&&j+1!=lj&&num1<min){
			min=num1;
			nextnode.i=i;
			nextnode.j=j+1; //Designate the right node to the target node
		}
		int num2=distance((int)bm.getPixelPos(i,j-1).getX(),(int)bm.getPixelPos(i,j-1).getY(),x,y);
		if(j-1>0&&bm.map[i][j-1]==0&&lj!=j-1&&num2<min){
			min=num2;
			nextnode.i=i;
			nextnode.j=j-1;
		}
		int num3=distance((int)bm.getPixelPos(i+1,j).getX(),(int)bm.getPixelPos(i+1,j).getY(),x,y);
		if(i+1<length&&bm.map[i+1][j]==0&&i+1!=li&&num3<min){
			min=num3;
			nextnode.j=j;
			nextnode.i=i+1;
		}
		int num4=distance((int)bm.getPixelPos(i-1,j).getX(),(int)bm.getPixelPos(i-1,j).getY(),x,y);
		if(i-1>0&&bm.map[i-1][j]==0&&i-1!=li&&num4<min){
			min=num4;
			nextnode.j=j;
			nextnode.i=i-1;
		}
		return nextnode;
	}

	public int getMaxhealth() {
		return maxhealth;
	}

	public void setMaxhealth(int maxhealth) {
		this.maxhealth = maxhealth;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if(health>getMaxhealth()){
			health=maxhealth;
		}
		else{
			this.health = health;
		}
	}

	public Time getCooldown() {
		return cooldown;
	}

	public void setCooldown(Time cooldown) {
		this.cooldown = cooldown;
	}

	public boolean canShoot() {
		return canshoot;
	}

	public void setCanshoot(boolean canshoot) {
		this.canshoot = canshoot;
	}

	public int getShootcooldown() {
		return shootcooldown;
	}

	public void setShootcooldown(int shootcooldown) {
		if(shootcooldown<20){//Lowest lowering of cooldowns
			this.shootcooldown = 20;
		}
		else{
			this.shootcooldown = shootcooldown;
		}
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
		
	
}
