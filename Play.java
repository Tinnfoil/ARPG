package ARPG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Play extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;//Ignore
	
    Square s = new Square();
    Camera cam;
    InputHandler h;
    ArrayList<Block> blocks= new ArrayList<Block>();
    
    private Thread thread;
    private boolean running=false;
    
    final double UPDATE=1.0/30.0;
    
    public Play(){
        Block b= new Block(100,500,300,20);
        Block c= new Block(655,100,20,250);
        Block d= new Block(100,400,100,20);
        Block e= new Block(100,150,20,100);
        Block f= new Block(400,300,100,20);
    	blocks.add(b);
    	blocks.add(c);
    	blocks.add(d);
    	blocks.add(e);
    	blocks.add(f);
    }
    
    public Square getSquare(){
    	return s;
    }
    public InputHandler getHandler(){
    	return h;
    }
    public void start(){
    	cam=new Camera(0,0);
    	h=new InputHandler();
    	thread= new Thread(this);
    	thread.run();
    }
    
    public void stop(){
    	
    }
    /**
     * Game Loop, runs the game by 60 computations per second: Reduces needed processing
     * FPS is what we see
     */
    public void run(){
    	
    	boolean render=false;
    	running=true;
    	
    	double firstTime=0;
    	double lastTime=System.nanoTime()/1000000000.0;
    	double passedTime=0;
    	double unprocessedTime=0;
    	
    	double frameTime=0;
    	int frames=0;
    	int fps=0;
    	
    	while(running){
    		render=false;
    		
    		firstTime=System.nanoTime()/1000000000.0;
    		passedTime=firstTime-lastTime;
    		lastTime=firstTime;
    		
    		unprocessedTime +=passedTime;
    		frameTime+=passedTime;
    		
    		while(unprocessedTime >= UPDATE){  
    			unprocessedTime-=UPDATE;
    			render=true;
    			//update game
    			
    			updateSquare();//moves square according to velocities
    			cam.move(this);
    			h.interpretInput(this);
    			for(int i=0;i<blocks.size();i++){//Collision Checks
    				if(s.intersectsBlocks(blocks)[i]){
    					s.fixPosition(blocks.get(i));
    				}
    			}
    			//
    			if(frameTime>=1.0){
    				//per second events
    				frameTime=0;
    				fps=frames;
    				frames=0;
    				System.out.println("FPS:"+fps);
    			}
    		}
    		if(render)
    		{
    			repaint();
    			frames++;
    			//renders game
    		}
    		else
    		{
    			try
    			{
    				Thread.sleep(1);
    			}
    			catch(InterruptedException e)
    			{
    				e.printStackTrace();
    			}
    		}
    	}
    	dispose();
    }
    
    private void dispose(){
    	
    }
	
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d= (Graphics2D)g; 
        g.setColor(Color.BLACK);
        try{
        	g2d.translate(-cam.getX(), -cam.getY());//begins cam
        	
            g.drawRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
            
            for(int i=0;i<blocks.size();i++){
            	Block b= blocks.get(i);
            	g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            }
            
            g2d.translate(cam.getX(), cam.getY());//end of cam
            
        }
        catch(Exception e){
        }
        
    }
    
	public void updateSquare(){
		s.move((double)s.getVelx(),(double)s.getVely());
	}
	
    
}

