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
	Time t=new Time();
	int x;
	int y;
	double launchx; double launchy;
	boolean jumping;
	boolean charged;
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
		
		if(jumping==false&&Math.sqrt(Math.pow((getX()+getX()+getRect().getWidth())/2-smidx, 2)+Math.pow((getY()+getY()+getRect().getHeight())/2-smidy, 2))<jumprange){
			//System.out.println("SetTrue");
			if(Math.abs(getVelx())<2){
			setVely(getVelx()*2);
			}
			if(Math.abs(getVely())<2){
			setVely(getVely()*2);
			}
			jumping=true;
		}
		else if(jumping==false){
			followPoint((int)(getX()+getX()+getRect().getWidth())/2,(int)(getY()+getY()+getRect().getHeight())/2,smidx,smidy);
			x=smidx;
			y=smidy;
		}
		else if(jumping==true){
			launch(x,y,smidx,smidy);
		}

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
			if(getVelx()>-4){
				setVelx(getVelx()-.2);
				}
		}
		else if((x<midx)&&Math.abs(x-midx)>getVelx()){
			if(getVelx()<4){
				setVelx(getVelx()+.2);
			}
		}

		if(Math.abs(y-midy)<=getVely()){
			setVely(0);
		}
		else if((y>midy)&&Math.abs(y-midy)>getVely()){
			if(getVely()>-4){
			setVely(getVely()-.2);
			}
		}
		else if((y<midy)&&Math.abs(y-midy)>getVely()){
			if(getVely()<4){
				setVely(getVely()+.2);
			}
		}
	}
	
	/**
	 *
	 */
	public void launch(int x, int y, int midx, int midy){
		//System.out.println("Jumping");
		if(currjumpdistance<jumpdistance && distance(midx,midy)<jumprange){
			if(getVelx()<0){
				setVelx(getVelx()-.2);
			}
			else if(getVelx()>0){
				setVelx(getVelx()+.2);
			}
			if(getVely()<0){
				setVelx(getVelx()-.2);
			}
			else if(getVely()<0){
				setVely(getVely()+.2);
			}
		currjumpdistance+=40;
		}
		else{
			currjumpdistance=0;
			jumping=false;
		}

	}

}
