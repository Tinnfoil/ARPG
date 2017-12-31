package ARPG;

import java.util.ArrayList;

public class InputHandler {
	
	Play p;
	private ArrayList<String> multiKey;
	Time t= new Time();
	
	public InputHandler(Play p){
		multiKey = new ArrayList<String>();
		this.p=p;
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
    
    /**
     * Interprets the current inputs mainly through the multikey arraylist. Certain strings in
     * the arraylist will prompt different outcomes.
     * @param p
     */
    public void interpretInput(Play p){
    	double xVel = p.getSquare().getVelx();
    	double yVel = p.getSquare().getVely();
    	if(multiKey.contains("UP")){
    		if(yVel>-10)
    		yVel += -.7;
    		if(yVel>0)
    		yVel += -4;
    	}
    	if(multiKey.contains("DOWN")){
    		if(yVel<10)
    		yVel += .7;
    		if(yVel<0)
    		yVel += 4;
    	}
    	if(multiKey.contains("RIGHT")){
    		if(xVel<10)
    		xVel += .7;
    		if(xVel<0)
    		xVel += 4;
    	}	
    	if(multiKey.contains("LEFT")){
    		if(xVel>-10)
    		xVel += -.7;
    		if(xVel>0)
    		xVel += -4;
    	}
    	if(multiKey.contains("LEFT") && multiKey.contains("RIGHT")){
    		xVel = 0;
    		}
    	if(multiKey.contains("UP") && multiKey.contains("DOWN")){
    		yVel = 0;
    	}
    	if((multiKey.contains("UP") || multiKey.contains("DOWN")) && (!multiKey.contains("LEFT") && !multiKey.contains("RIGHT"))){
    		if(xVel>0){
    			xVel-=.3;
    		}
			else if(xVel<0){
				xVel+=.3;
			}
    	}
    	if((multiKey.contains("LEFT") || multiKey.contains("RIGHT")) && (!multiKey.contains("UP") && !multiKey.contains("DOWN"))){
    		if(yVel>0){
    			yVel-=.3;
    		}
			else if(yVel<0){
				yVel+=.3;
			}
    	}
    		
    	if((!multiKey.contains("UP") && !multiKey.contains("DOWN")) && (!multiKey.contains("LEFT") && !multiKey.contains("RIGHT"))){
    		if(xVel>0){
    			xVel--;
    		}
			else if(xVel<0){
				xVel++;
			}
    		if(yVel>0){
    			yVel--;
    		}
			else if(yVel<0){
				yVel++;
			}
    	}
    	
    	if(multiKey.contains("SPACE")){
    			p.getSquare().setDashing(true);
    	}
    	
    	p.getSquare().setVelx(xVel);
    	p.getSquare().setVely(yVel);

    	
    }
}
