package ARPG;

public class Projectile extends Hitbox{
	
	int x;
	int y;
	private double angle;
	private double vel;
	
	private boolean heal;
	private int healamount;
	private boolean bomb;
	private boolean friendlybomb;
	private int delay;
	
	private boolean blood;
	private boolean squareshot;
	
	public Projectile(int x1, int y1, int x2,int y2,int size, int lifetime){
		super(x1,y1,size,size, lifetime);
		setRect(x1,y1,size,size);
		x=x2-x1;
		y=y2-y1;
		angle=findAngle(y,x);
		setVel(7);
		setDamage(0);
		squareshot=false;
		blood=false;
		//System.out.println("Angle:"+angle);
		//System.out.println(Math.sin(Math.toRadians(angle)));
		//System.out.println(Math.cos(Math.toRadians(angle)));
	}
	
	public void tick(){
			setCurrentTime(getCurrentTime()+1);
			setX(getX()+(int)(Math.cos(Math.toRadians(angle))*vel));
			setY(getY()+(int)(Math.sin(Math.toRadians(angle))*vel));
	}
	
	public double getAngle(){
		return angle;
	}
	
	public void changeAngle(int x1, int y1, int x2, int y2){
		angle=findAngle((y2-y1),(x2-x1));
	}
	public void changeAngle(double angle){
		this.angle=angle;
	}

	public boolean isBomb() {
		return bomb;
	}

	public void setBomb(boolean bomb) {
		this.bomb = bomb;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public double getVel() {
		return vel;
	}

	public void setVel(double vel) {
		this.vel = vel;
	}

	public boolean isFriendlybomb() {
		return friendlybomb;
	}

	public void setFriendlybomb(boolean friendlybomb) {
		this.friendlybomb = friendlybomb;
	}

	public boolean isSquareshot() {
		return squareshot;
	}

	public void setSquareshot(boolean squareshot) {
		this.squareshot = squareshot;
	}

	public boolean isHeal() {
		return heal;
	}

	public void setHeal(boolean heal) {
		this.heal = heal;
	}

	public int getHealamount() {
		return healamount;
	}

	public void setHealamount(int healamount) {
		this.healamount = healamount;
	}

	public boolean isBlood() {
		return blood;
	}

	public void setBlood(boolean blood) {
		this.blood = blood;
	}

	
}
