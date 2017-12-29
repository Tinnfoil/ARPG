package ARPG;

public class Projectile extends Object{

	int lifeTime=60;//frames life
	int currentTime=0;
	int x;
	int y;
	double angle;
	
	public Projectile(int x1, int y1, int x2,int y2){
		setRect(x1,y1,10,10);
		x=x2-x1;
		y=y2-y1;
		angle=findAngle(y,x);
		//System.out.println("Angle:"+angle);
		//System.out.println(Math.sin(Math.toRadians(angle)));
		//System.out.println(Math.cos(Math.toRadians(angle)));
	}
	
	public void tick(){
			currentTime++;
			setX(getX()+(int)(Math.cos(Math.toRadians(angle))*10));
			setY(getY()+(int)(Math.sin(Math.toRadians(angle))*10));
	}
	
	
	
}
