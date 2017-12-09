package ARPG;

public class Jumper extends AI{

	double vel;
	int jumprange;
	int jumpdistance;
	int currjumpdistance;
	double launchx; double launchy;
	boolean jumping;
	public Jumper(int x, int y, int size, double vel){
		super(x,y,size);
		setX(x);
		setY(y);
		this.vel=vel;
		jumprange=200;//pixels
		jumping =false;
		jumpdistance=300;
		currjumpdistance=0;
		setSize(size);
	}
	
	public void tick(Play p){//thinking method
		System.out.println("Shuhsudsh");
		int x=(getX()+getX()+getSize())/2;
		int y=(getY()+getY()+getSize())/2;
		int midx=((int)p.getSquare().getRect().getX()+(int)p.getSquare().getRect().getMaxX())/2;
		int midy=((int)p.getSquare().getRect().getY()+(int)p.getSquare().getRect().getMaxY())/2;
		if(Math.sqrt(Math.pow(x-midx, 2)+Math.pow(y-midy, 2))>=jumprange&&!jumping){
			launchx=1;
			launchy=1;
			followPoint(x,y,midx,midy);
		}
		else{
			jumping=true;
			launch((int)launchx*10,(int)launchy*10);
		}
	}
	
	public void followPoint(int x, int y, int midx, int midy){
		System.out.println("dap");
		if(Math.abs(x-midx)<vel){
			if((x>midx)){
				setX((int)(getX()-1));
			}
			else if((x<midx)){
				setX((int)(getX()+1));
			}
		}
		else if((x>midx)&&Math.abs(x-midx)>vel){
			setX((int)(getX()-vel));
		}
		else if((x<midx)&&Math.abs(x-midx)>vel){
			setX((int)(getX()+vel));
		}

		if(Math.abs(y-midy)<vel){
			if((y>midy)){
				setY((int)(getY()-1));
			}
			else if((y<midy)){
				setY((int)(getY()+1));
			}
		}
		else if(y>midy){
			setY((int)(getY()-vel));
		}
		else if(y<midy){
			setY((int)(getY()+vel));
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
