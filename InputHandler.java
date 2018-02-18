package ARPG;

import java.util.ArrayList;

public class InputHandler {
	
	Play p;
	private ArrayList<String> multiKey;
	private int[] mousecoords= new int[4];
	boolean spawning=false;
	boolean attacking;
	int attackdelay=0;
	Time t= new Time();
	
	public InputHandler(Play p){
		multiKey = new ArrayList<String>();
		 attacking=false;
		this.p=p;
	}
	
	public ArrayList<String> getMultikey(){
		return multiKey;
	}
	
    public void readList(){
        for(int i=0; i <multiKey.size(); i++) //
        {
        	String a = multiKey.get(i);
        	System.out.println(a);
        }
    }
    public void addInput(String input){
   	 	if(!multiKey.contains(input)){
         multiKey.add(input);
    	 }
    }
    public void removeInput(String input){
    	if(multiKey.contains(input)){
    		multiKey.remove(input);
    	}
    }
    
    public int[] getMouseCoords(){
    	return mousecoords;
    }
    public void setMouseCoords(int x1, int y1, int x2, int y2){
    	mousecoords[0]=x1;
    	mousecoords[1]=y1;
    	mousecoords[2]=x2;
    	mousecoords[3]=y2;
    }
    
    /**
     * Interprets the current inputs mainly through the multikey arraylist. Certain strings in
     * the arraylist will prompt different outcomes.
     * Acceleration based movement. Pressing the key will increase speed.
     * @param p
     */
    public void interpretInput(Play p){
    	double xVel = p.getSquare().getVelx();
    	double yVel = p.getSquare().getVely();
    	
    	if(multiKey.contains("UP")){
    		if(yVel>-p.getSquare().getMaxspeed())
    			yVel += -p.getSquare().getAcceleration();
    		if(yVel<-p.getSquare().getMaxspeed())
    			yVel= -p.getSquare().getMaxspeed();
    		if(yVel>0)
    			yVel += -3;
    	}
    	if(multiKey.contains("DOWN")){
    		if(yVel<p.getSquare().getMaxspeed())
    			yVel += p.getSquare().getAcceleration();
    		if(yVel>p.getSquare().getMaxspeed())
    			yVel= p.getSquare().getMaxspeed();
    		if(yVel<0)
    			yVel += 3;
    	}
    	if(multiKey.contains("RIGHT")){
    		if(xVel<p.getSquare().getMaxspeed())
    			xVel += p.getSquare().getAcceleration();
    		if(xVel>p.getSquare().getMaxspeed())
    			xVel= p.getSquare().getMaxspeed();
    		if(xVel<0)
    			xVel += 3;
    	}	
    	if(multiKey.contains("LEFT")){
    		if(xVel>-p.getSquare().getMaxspeed())
    			xVel += -p.getSquare().getAcceleration();
    		if(xVel<-p.getSquare().getMaxspeed())
    			xVel= -p.getSquare().getMaxspeed();
    		if(xVel>0)
    			xVel += -3;
    	}
    	if(multiKey.contains("LEFT") && multiKey.contains("RIGHT")){
    		xVel = 0;
    	}
    	if(multiKey.contains("UP") && multiKey.contains("DOWN")){
    		yVel = 0;
    	}
    	if((multiKey.contains("UP") || multiKey.contains("DOWN")) && (!multiKey.contains("LEFT") && !multiKey.contains("RIGHT"))){//If player is only pressing horizontal movement, vertical movement is slowed down
    		if(xVel>0){
    			xVel-=.3;
    		}
			else if(xVel<0){
				xVel+=.3;
			}
    	}
    	if((multiKey.contains("LEFT") || multiKey.contains("RIGHT")) && (!multiKey.contains("UP") && !multiKey.contains("DOWN"))){//If player is only pressing vertical movement, horizontal movement is slowed down
    		if(yVel>0){
    			yVel-=.3;
    		}
			else if(yVel<0){
				yVel+=.3;
			}
    	}  		
    	if((!multiKey.contains("UP") && !multiKey.contains("DOWN")) && (!multiKey.contains("LEFT") && !multiKey.contains("RIGHT"))){//Stops Square if none of movement keys are pressed
    		//System.out.println(xVel);
    		if(xVel>0){
    			xVel-=1;
    		}
			else if(xVel<0){
				xVel+=1;
			}


    		if(yVel>0){
    			yVel-=1;
    		}
			else if(yVel<0){
				yVel+=1;
			}

    	}
    	
    	if(multiKey.contains("SPACE")){
    		p.getSquare().setDashing(true);
    	}
    	
    	p.getSquare().setVelx(xVel);
    	p.getSquare().setVely(yVel);

    	/**
    	if(multiKey.contains("CLICKED")){
    		if(spawning==true){
    			Projectile pro= new Projectile(mousecoords[0],mousecoords[1],mousecoords[2],mousecoords[3],10,60);
    		
    			pro.setVel(20);
    			pro.setHurtsenemy(true);
    			pro.setDamage(10);
    			pro.setSquareshot(true);
    			p.spawnProjectile(pro);
    			spawning=false;
    		}
    	}
    	*/
    	if(multiKey.contains("CLICKED")){
    		if(spawning==true){
    			double angle=p.getSquare().findAngle(mousecoords[3]-mousecoords[1], mousecoords[2]-mousecoords[0]);
    			double interval=(p.getSquare().getProjectileshots()*5)/p.getSquare().getProjectileshots();
        		Projectile[] ps= new Projectile[p.getSquare().getProjectileshots()];
        		for(int i=0;i<ps.length;i++){
        			Projectile po= new Projectile(mousecoords[0], mousecoords[1],mousecoords[2], mousecoords[3],10,60);
        			po.setDamage(20);
        			po.setVel(20);
        			po.setHurtsenemy(true);
        			po.setSquareshot(true);
        			po.changeAngle(angle-((p.getSquare().getProjectileshots()*5)/2)+(i*interval));
        			ps[i]=po;
        		}
        		p.spawnMultipleProjectiles(ps);
        		spawning=false;	
    		}
    	}
    	
    	if(multiKey.contains("ATTACK")){
    		if(attacking==true){
    			double angle=p.getSquare().findAngle(mousecoords[1]-mousecoords[3],mousecoords[0]-mousecoords[2]);
    			if(attackdelay>0){
    				//p.getSquare().setInvunerable(true);
    				p.getSquare().move((Math.cos(Math.toRadians(angle+180))*10),(Math.sin(Math.toRadians(angle+180))*10));
    			}
    			if(attackdelay<=0){
    				p.getSquare().setInvunerable(false);
    				p.delayedAttack(angle,4);
    				attacking =false;
    			}
    			else{
    				attackdelay--;	
    			}
    		}
    	}
    	
    }
    

}
