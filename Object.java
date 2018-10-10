package ARPG;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
/**
 * The super class of all rectangular objects in the game
 * Contains velocities and many helper methods
 * Examples: Projectiles, AIs, and Player/Square
 * @author Kenny
 */
public class Object {

    private int x;
    private int y;
    private double velx;
    private double vely;
    private double maxspeed;
    private double acceleration;
    
    private int speedboost;
    private int speedboostframes;
    private int speedboostduration;
    private int stunframes;
    private int delayedstunframes;
    private int stunduration;
    private boolean invunerable;
    private int invunerableframes;
    private int dotframes;
    private int dotdamage;
    Rectangle rect;
    
    public Object(){
    	x=0;
    	y=0;
    	velx=0;
    	vely=0;
    	rect= new Rectangle(0,0,0,0);
    	setDotdamage(0);
    }
    
    public Rectangle getRect(){
    	return rect;
    }
    
    public void setRect(Rectangle rect){
    	this.rect=rect;
    }
    
    public void setRect(int x, int y, int width, int height){
    	rect.setBounds(x, y, width, height);
    }
    
    public int getX(){
    	return x;
    }
    
	public void setX(int x){
    	rect.translate(x-this.x,0);
    	this.x=x;
    }
	
	public int getMidx(){
		return x+(int)(getRect().getWidth()/2);
	}
    
    public int getY(){
    	return y;
    }
    public void setY(int y){
    	rect.translate(0,y-this.y);
    	this.y=y;
    }
    
	public int getMidy(){
		return y+(int)(getRect().getHeight()/2);
	}
	
	public void setLocation(int x, int y){
		setX(x);
		setY(y);
	}

	public double getVelx() {
		return velx;
	}

	public void setVelx(double velx){
		this.velx=velx;
	}

	public double getVely() {
		return vely;
	}

	public void setVely(double vely) {
		this.vely = vely;
	}
    public void stop(){
    	setVelx(0);
    	setVely(0);
    }
    public void friction(){
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
	
	/**
	 * returns the distance form this object's x and y to the specified x and y
	 * *Example of use: to determine when Jumper(AI) should "jump" at the player
	 * 					if(Jumper.distance(player.getx(),player.getY())<500) { *jump* }
	 * 
	 * @param x middle point of object
	 * @param y middle point of object
	 * @return
	 */
	public int distance(int x, int y){
		return (int)Math.sqrt(Math.pow((getX()+getX()+getRect().getWidth())/2-x, 2)+Math.pow((getY()+getY()+getRect().getHeight())/2-y, 2));
	}
	
	public int distance(int x1, int y1, int x2, int y2){
		return (int)Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
	}
	
	   public void move(double x, double y){
		   if(x!=0){
			   if(x>0){
				   x=x+getSpeedboost();
			   }
			   else if(x<0){
				   x=x-getSpeedboost();
			   }
		   }
		   if(y!=0){
			   if(y>0){
				   y=y+getSpeedboost();  
			   }
			   else if(y<0){
				   y=y-getSpeedboost();
			   }
		   }
	    	setX((getX()+(int)x));
	    	setY((getY()+(int)y));
	    }
	    
	    public boolean intersectsBlock(Block b){
	    	return b.getRect().intersects(getRect());
	    }
	    
	    public boolean[] intersectsBlocks(ArrayList<Block> list){
	    	boolean[] blist=new boolean[list.size()];
	    	for(int i=0;i<list.size();i++){  
	    		Block b=list.get(i);
	    		if(intersectsBlock(b)){
	    			blist[i]=true;
	    			fixVelocity(b);
	    		}
	    		else{
	    			blist[i]=false;
	    		}
	    	}
	    	return blist;
	    }
	    
	    /**
	     * Stops the object's velocities in a dismension depending whether or not it is 
	     * in the block.
	     * @param b The block
	     */
	    public void fixVelocity(Block b){
			double currvelx=getVelx();
			double currvely=getVely();
			if((b.getY()<=(int) getRect().getMaxY())){
				currvely+=-getVely();
				//setVely(0);
			}
			if(b.getX()<=(int) getRect().getMaxX()){
				currvelx+=-getVelx();
				//setVelx(0);
			}
			if((b.getMaxY())>=(int)getRect().getMinY()){
				currvely+=getVely();
				//setVely(0);
			}
			if((b.getMaxX())>=(int)getRect().getMinX()){
				currvelx+=getVelx();
				//setVelx(0);
			}
			setVelx(currvelx);
			setVely(currvely);
	    }
	    
	    /**
	     * Adjusts the position of the object(Rectangle) so that it doesn't go into the blocks
	     * Serves as the basic collision dectection and handling of the game
	     * Uses corner-based detection for deciding the direction the object is pushed
	     * example: Top-right corner is the block, if the corner is in the block it will compare
	     * 			the the distance of the corner to all other sides of the blocks. The
	     * 			shortest distance determines what side the block the object will be "Stuck" on 
	     * @param b
	     */
		public void fixPosition(Block b){
	    	if(getRect().contains(b.getX(),b.getY(), b.getWidth(), 1)){
	    		int diff =b.getY()-(int)getRect().getMaxY();
	    		move(0, diff);
	    	}
	    	else if(getRect().contains(b.getX(),b.getY(), 1, b.getHeight())){
	    		int diff =b.getX()-(int) getRect().getMaxX();
	    		move(diff, 0);
	    	}
	    	else if(getRect().contains(b.getX(),b.getY()+b.getHeight(), b.getWidth(), 1)){
	    		int diff = (b.getY()+b.getHeight())-(int) getRect().getMinY();
	    		move(0,diff);
	    	}
	    	else if(getRect().contains(b.getX()+b.getWidth(),b.getY(), 1, b.getHeight())){
	    		int diff = (int) ((b.getX()+b.getWidth())-(getRect().getMinX()));
	    		move(diff,0);
	    	}
	    	
	    	if(b.getRect().contains(getRect().getMinX(), getRect().getMaxY()))//bottom left
	    	{
	    		if(getRect().getMaxY()-b.getY()<b.getMaxX()-getRect().getX()){
	        		int diff =(int)getRect().getMaxY()-b.getY();
	        		move(0, -diff);
	    		}
	    		else if(getRect().getMaxY()-b.getY()>b.getMaxX()-getRect().getX()){
	    			int diff =b.getMaxX()-(int)getRect().getMinX();
	        		move(diff, 0);
	    		}
	    	}
	    	else if(b.getRect().contains(getRect().getMinX(), getRect().getMinY()))//top left
	    	{
	    		if(b.getMaxX()-getRect().getMinX()<b.getMaxY()-getRect().getMinY()){
	        		int diff =b.getMaxX()-(int)getRect().getMinX();
	        		move(diff, 0);
	    		}
	    		else if(b.getMaxX()-getRect().getMinX()>b.getMaxY()-getRect().getMinY()){
	    			int diff =b.getMaxY()-(int)getRect().getMinY();
	        		move(0, diff);
	    		}
	    	}
	    	else if(b.getRect().contains(getRect().getMaxX(),getRect().getMinY()))//top right
	    	{
	    		if(b.getMaxY()-getRect().getMinY()<getRect().getMaxX()-b.getX()){
	    			int diff =b.getMaxY()-(int)getRect().getMinY();
	        		move(0, diff);
	    		}
	    		else if(b.getMaxY()-getRect().getMinY()>getRect().getMaxX()-b.getX()){
	    			int diff =(int)getRect().getMaxX()-b.getX();
	        		move(-diff, 0);
	    		}
	    	}
	    	else if(b.getRect().contains(getRect().getMaxX(),getRect().getMaxY()))//bottom right
	    	{
	    		if(getRect().getMaxX()-b.getX()<getRect().getMaxY()-b.getY()){
	    			int diff =(int)getRect().getMaxX()-b.getX();
	        		move(-diff, 0);
	    		}
	    		else if(getRect().getMaxX()-b.getX()>b.getY()-getRect().getMaxY()){
	    			int diff =(int)getRect().getMaxY()-b.getY();
	        		move(0, -diff);
	    		}
	    	}
	    	
	    }
		
		/**
		 * 
		 * @param o1 Object to be "Knocked away" from the point
		 * @param x x coord of the point
		 * @param y y coord of the point
		 * @param power the "Strength of the knockback"
		 */
		public void knockback(Object o1, int x, int y, double power){
			double angle=o1.findAngle(o1.getY()+(int)o1.getRect().getHeight()/2-y, o1.getX()+(int)o1.getRect().getWidth()/2-x);
			o1.accelerate(angle,power);
		}
		
		public void accelerate(double angle, double a){
			double angle1= Math.toRadians(angle);
			setVelx(getVelx()+(Math.cos(angle1)*a*10));
			setVely(getVely()+(Math.sin(angle1)*a*10));
		}
		
		public void boostSpeed(int speed, int duration){
			setSpeedboost(speed);
			setSpeedboostframes(duration);
		}
		
		public double findAngle(int y, int x){
			double angle =0;
			angle=Math.toDegrees(Math.atan2(y,x));
			if(y<0&&x<0){
				angle+=360;
			}
			return angle;
		}
		
		public boolean intersects(Circle c){
			return c.intersects(this);
		}
		
		public int random(int num){
			Random RN= new Random();
			return RN.nextInt()+1;
		}

		public double getMaxspeed() {
			return maxspeed;
		}

		public void setMaxspeed(double maxspeed) {
			this.maxspeed = maxspeed;
		}

		public double getAcceleration() {
			return acceleration;
		}

		public void setAcceleration(double d) {
			this.acceleration = d;
		}

		public boolean isInvunerable() {
			return invunerable;
		}

		public void setInvunerable(boolean invunerable) {
			this.invunerable = invunerable;
		}

		public int getInvunerableframes() {
			return invunerableframes;
		}

		public void setInvunerableframes(int invunerableframes) {
			this.invunerableframes = invunerableframes;
		}

		public int getStunframes() {
			return stunframes;
		}

		public void setStunframes(int stunframes) {
			this.stunframes = stunframes;
		}

		public int getDelayedstunframes() {
			return delayedstunframes;
		}

		public void setDelayedstunframes(int delayedstunframes) {
			this.delayedstunframes = delayedstunframes;
		}

		public int getStunduration() {
			return stunduration;
		}

		public void setStunduration(int stunduration) {
			this.stunduration = stunduration;
		}

		public int getSpeedboost() {
			return speedboost;
		}

		public void setSpeedboost(int speedboost) {
			this.speedboost = speedboost;
		}

		public int getSpeedboostframes() {
			return speedboostframes;
		}

		public void setSpeedboostframes(int speedboostframes) {
			this.speedboostframes = speedboostframes;
		}

		public int getSpeedboostduration() {
			return speedboostduration;
		}

		public void setSpeedboostduration(int speedboostduration) {
			this.speedboostduration = speedboostduration;
		}

		public int getDotframes() {
			return dotframes;
		}

		public void setDotframes(int dotframes) {
			this.dotframes = dotframes;
		}

		public int getDotdamage() {
			return dotdamage;
		}

		public void setDotdamage(int dotdamage) {
			this.dotdamage = dotdamage;
		}


}
