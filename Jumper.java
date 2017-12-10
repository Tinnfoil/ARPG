package ARPG;

import java.awt.Rectangle;

/**
 * Subclass of AI 
 * AI That follows the square and jumps at the square when within a distance from it. 
 */
public class Jumper extends AI{

	int jumprange;
	int jumpdistance;
	int currjumpdistance;
	double launchx; double launchy;
	boolean jumping;
	public Jumper(int x, int y, int size, double vel){
		super(x,y,size);
		setX(x);
		setY(y);
		setVelx(vel);
		setVely(vel);
		jumprange=200;//pixels
		jumping =false;
		jumpdistance=300;
		currjumpdistance=0;
		Rectangle Rect= new Rectangle(getX(),getY(),size,size);
		setRect(Rect);
		setSize(size);
	}
	
	/**
	 * thinking and action method of the AI 
	 * method is constantly called by Play class.
	 * AI Movement System(Based on Square/Player movement)
	 * 1.Moves object according to velocities
	 * 2.Sets new velocities according to AI or methods
	 * 3.Collision system checks if the "next position" will collide with anything and fix velocities accordingly
	 * 4.Repeat
	 * @param p
	 */
	public void tick(Play p){//thinking method
		move((double)getVelx(),(double)getVely());
		int smidx=(p.getSquare().getX()+(int)p.getSquare().getRect().getMaxX())/2;
		int smidy=(p.getSquare().getY()+(int)p.getSquare().getRect().getMaxY())/2;
		followPoint((int)(getX()+getX()+getRect().getWidth())/2,(int)(getY()+getY()+getRect().getHeight())/2,smidx,smidy);
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
			int smidx=(getX()+(int)getRect().getMaxX())/2;
			int smidy=(getY()+(int)getRect().getMaxY())/2;
			if(Math.sqrt(Math.pow(bmidx-smidx, 2)+Math.pow(bmidy-smidy, 2))<200){
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
			if(getVelx()>-3){
				setVelx(getVelx()-.1);
				}
		}
		else if((x<midx)&&Math.abs(x-midx)>getVelx()){
			if(getVelx()<3){
				setVelx(getVelx()+.1);
			}
		}

		if(Math.abs(y-midy)<=getVely()){
			setVely(0);
		}
		else if((y>midy)&&Math.abs(y-midy)>getVely()){
			if(getVely()>-3){
			setVely(getVely()-.1);
			}
		}
		else if((y<midy)&&Math.abs(y-midy)>getVely()){
			if(getVely()<3){
				setVely(getVely()+.1);
			}
		}
	}
	
	/**
	 * x and y is from -1 to 1 to represent direction
	 */
	public void launch(int x, int y){
		if(currjumpdistance<jumpdistance){
		setX(getX()+x);
		setY(getY()+y);
		currjumpdistance+=x;
		}
		else{
			currjumpdistance=0;
			jumping=false;
		}

	}

}
