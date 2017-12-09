package ARPG;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Square extends Object
{
	
    private int width;
    private int height;
    /**
     * Constructor for objects of class Square
     */
    public Square()
    {
    	width=50;
    	height=50;
        setX(51);
        setY(51);
        Rectangle rect= new Rectangle(getX(),getY(),50,50);
        setRect(rect);
    }
    
    public int getWidth(){
    	return width;
    }
    public int getHeight(){
    	return height;
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
    				System.out.println("bottom stop");
    			}
    			if(b.getX()<=(int) getRect().getMaxX()){
    				currvelx+=-getVelx();
    				//setVelx(0);
    			}
    			if((b.getMaxY())>=(int)getRect().getMinY()){
    				currvely+=getVely();
    				//setVely(0);
    				System.out.println("top stop");
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
        		System.out.println("1");
    		}
    		else if(getRect().getMaxY()-b.getY()>getRect().getX()-b.getMaxX()){
        		int diff =b.getMaxX()-(int)getRect().getMinX();
        		move(diff, 0);
        		System.out.println("2");
    		}
    	}
    	else if(b.getRect().contains(getRect().getMinX(), getRect().getMinY()))//top left
    	{
    		if(b.getMaxX()-getRect().getMinX()<b.getMaxY()-getRect().getMinY()){
        		int diff =b.getMaxX()-(int)getRect().getMinX();
        		move(diff, 0);
        		System.out.println("3");
    		}
    		else if(b.getMaxX()-getRect().getMinX()>b.getMaxY()-getRect().getMinY()){
    			int diff =b.getMaxY()-(int)getRect().getMinY();
        		move(0, diff);
        		System.out.println("4");
    		}
    	}
    	else if(b.getRect().contains(getRect().getMaxX(),getRect().getMinY()))//top right
    	{
    		if(b.getMaxY()-getRect().getMinY()<getRect().getMaxX()-b.getX()){
    			int diff =b.getMaxY()-(int)getRect().getMinY();
        		move(0, diff);
        		System.out.println("5");
    		}
    		else if(b.getMaxY()-getRect().getMinY()>getRect().getMaxX()-b.getX()){
    			int diff =(int)getRect().getMaxX()-b.getX();
        		move(-diff, 0);
        		System.out.println("6");
    		}
    	}
    	else if(b.getRect().contains(getRect().getMaxX(),getRect().getMaxY()))//bottom right
    	{
    		if(getRect().getMaxX()-b.getX()<getRect().getMaxY()-b.getY()){
    			int diff =(int)getRect().getMaxX()-b.getX();
        		move(-diff, 0);
        		System.out.println("7");
    		}
    		else if(getRect().getMaxX()-b.getX()>b.getY()-getRect().getMaxY()){
    			int diff =(int)getRect().getMaxY()-b.getY();
        		move(0, -diff);
        		System.out.println("8");
    		}
    	}

    }




    
}


