package ARPG;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Object {

    private int x;
    private int y;
    private double velx;
    private double vely;
    Rectangle rect;
    public Object(){
    	x=0;
    	y=0;
    	velx=0;
    	vely=0;
    	rect= new Rectangle(0,0,0,0);
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
    
    public int getY(){
    	return y;
    }
    public void setY(int y){
    	rect.translate(0,y-this.y);
    	this.y=y;
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
	
	   public void move(double x, double y){
	    	setX(getX()+(int)x);
	    	setY(getY()+(int)y);
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
	    			double currvelx=getVelx();
	    			double currvely=getVely();
	    			if((b.getY()<=(int) getRect().getMaxY())){
	    				//setVely(0);
	    				currvely+=-getVely();
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
	    		else{
	    			blist[i]=false;
	    		}
	    	}
	    	return blist;
	    }
	    
	    /**
	     * Adjusts the position of the square so that it doesn't go into the blocks
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
	    		else if(getRect().getMaxY()-b.getY()>getRect().getX()-b.getMaxX()){
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
}
