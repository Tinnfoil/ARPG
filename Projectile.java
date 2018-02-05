package ARPG;

public class Projectile extends Hitbox{
	
	int x;
	int y;
	double angle;
	
	public Projectile(int x1, int y1, int x2,int y2, int lifetime){
		super(x1,y1,7,7, lifetime);
		setRect(x1,y1,7,7);
		x=x2-x1;
		y=y2-y1;
		angle=findAngle(y,x);
		//System.out.println("Angle:"+angle);
		//System.out.println(Math.sin(Math.toRadians(angle)));
		//System.out.println(Math.cos(Math.toRadians(angle)));
	}
	
	public void tick(){
			setCurrentTime(getCurrentTime()+1);
			setX(getX()+(int)(Math.cos(Math.toRadians(angle))*7));
			setY(getY()+(int)(Math.sin(Math.toRadians(angle))*7));
	}
	
	
	
}
