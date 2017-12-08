package ARPG;


import java.awt.Rectangle;
import java.util.ArrayList;

public class Square extends Object
{
	
    private int width;
    private int height;
    Rectangle rect = new Rectangle();
    /**
     * Constructor for objects of class Square
     */
    public Square()
    {
    	width=50;
    	height=50;
        setX(300);
        setY(0);
        rect.setSize(width, height);
        rect.setBounds(getX(), getY(), width, height);
    }
    
    public int getWidth(){
    	return width;
    }
    public int getHeight(){
    	return height;
    }
    
    public void move(double x, double y){
    	rect.translate((int)x, (int)y);
    	setX(getX()+(int)x);
    	setY(getY()+(int)y);
    }
    
    public boolean intersectsBlock(Block b){
    	return b.getRect().intersects(rect);
    }
    
    public boolean[] intersectsBlocks(ArrayList<Block> list){
    	boolean[] blist=new boolean[list.size()];
    	for(int i=0;i<list.size();i++){  
    		Block b=list.get(i);
    		if(intersectsBlock(b)){
    			blist[i]=true;
    			double currvelx=getVelx();
    			double currvely=getVely();
    			if((b.getY()<=(int) rect.getMaxY())){
    				//setVely(0);
    				currvely+=-getVely();
    				System.out.println("bottom stop");
    			}
    			if(b.getX()<=(int) rect.getMaxX()){
    				currvelx+=-getVelx();
    				//setVelx(0);
    			}
    			if((b.getMaxY())>=(int)rect.getMinY()){
    				currvely+=getVely();
    				//setVely(0);
    				System.out.println("top stop");
    			}
    			if((b.getMaxX())>=(int)rect.getMinX()){
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
    	if(rect.contains(b.getX(),b.getY(), b.getWidth(), 1)){
    		int diff =b.getY()-(int)rect.getMaxY();
    		move(0, diff);
    	}
    	else if(rect.contains(b.getX(),b.getY(), 1, b.getHeight())){
    		int diff =b.getX()-(int) rect.getMaxX();
    		move(diff, 0);
    	}
    	else if(rect.contains(b.getX(),b.getY()+b.getHeight(), b.getWidth(), 1)){
    		int diff = (b.getY()+b.getHeight())-(int) rect.getMinY();
    		move(0,diff);
    	}
    	else if(rect.contains(b.getX()+b.getWidth(),b.getY(), 1, b.getHeight())){
    		int diff = (int) ((b.getX()+b.getWidth())-(rect.getMinX()));
    		move(diff,0);
    	}
    	if(b.getRect().contains(rect.getMinX(), rect.getMaxY()))//bottom left
    	{
    		if(rect.getMaxY()-b.getY()<b.getMaxX()-rect.getX()){
        		int diff =(int)rect.getMaxY()-b.getY();
        		move(0, -diff);
        		System.out.println("1");
    		}
    		else if(rect.getMaxY()-b.getY()>rect.getX()-b.getMaxX()){
        		int diff =b.getMaxX()-(int)rect.getMinX();
        		move(diff, 0);
        		System.out.println("2");
    		}
    	}
    	else if(b.getRect().contains(rect.getMinX(), rect.getMinY()))//top left
    	{
    		if(b.getMaxX()-rect.getMinX()<b.getMaxY()-rect.getMinY()){
        		int diff =b.getMaxX()-(int)rect.getMinX();
        		move(diff, 0);
        		System.out.println("3");
    		}
    		else if(b.getMaxX()-rect.getMinX()>b.getMaxY()-rect.getMinY()){
    			int diff =b.getMaxY()-(int)rect.getMinY();
        		move(0, diff);
        		System.out.println("4");
    		}
    	}
    	else if(b.getRect().contains(rect.getMaxX(),rect.getMinY()))//top right
    	{
    		if(b.getMaxY()-rect.getMinY()<rect.getMaxX()-b.getX()){
    			int diff =b.getMaxY()-(int)rect.getMinY();
        		move(0, diff);
        		System.out.println("5");
    		}
    		else if(b.getMaxY()-rect.getMinY()>rect.getMaxX()-b.getX()){
    			int diff =(int)rect.getMaxX()-b.getX();
        		move(-diff, 0);
        		System.out.println("6");
    		}
    	}
    	else if(b.getRect().contains(rect.getMaxX(),rect.getMaxY()))//bottom right
    	{
    		if(rect.getMaxX()-b.getX()<rect.getMaxY()-b.getY()){
    			int diff =(int)rect.getMaxX()-b.getX();
        		move(-diff, 0);
        		System.out.println("7");
    		}
    		else if(rect.getMaxX()-b.getX()>b.getY()-rect.getMaxY()){
    			int diff =(int)rect.getMaxY()-b.getY();
        		move(0, -diff);
        		System.out.println("8");
    		}
    	}

    }




    
}


