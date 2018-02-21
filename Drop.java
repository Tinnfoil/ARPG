package ARPG;

public class Drop extends Object{
	
	private int lifetime;
	private int currenttime;
	
	private boolean healthdrop;
	private int healamount;
	
	private String color;
	
	public Drop(int x, int y, int size, int lifetime, String Color){
		super();
		super.setRect(x, y, size, size);
		this.lifetime=lifetime;		
		setCurrenttime(getLifetime());
		this.color=Color;
	}
	
	public void healthdrop(int healamount){
		setHealthdrop(true);
		setHealamount(healamount);
	}
	public boolean isHealthdrop() {
		return healthdrop;
	}
	public void setHealthdrop(boolean healthdrop) {
		this.healthdrop = healthdrop;
	}

	public int getHealamount() {
		return healamount;
	}

	public void setHealamount(int healamount) {
		this.healamount = healamount;
	}

	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	public int getCurrenttime() {
		return currenttime;
	}

	public void setCurrenttime(int currenttime) {
		this.currenttime = currenttime;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
