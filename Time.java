package ARPG;

public class Time {
	
	boolean timing;
	int time;
	
	public Time(){
		timing=false;
		time=0;
	}
	
	public void startTimer(){
		timing=true;
	}
	
	public void stopTimer(){
		timing=false;
	}
	
	public void tick(){

			time++;

	}
	
	public double getTimer(){
		//System.out.println(time);		
		return time;
	}
	
	public boolean getTiming(){
		return timing;
	}
	
	public void setTiming(Boolean b){
		time=0;
		timing=b;
	}
	
}
