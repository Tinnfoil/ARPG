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
    BlockMap bm= new BlockMap(0,0,0,0);
    ArrayList<Block> blocks= new ArrayList<Block>();
    ArrayList<AI> AIs= new ArrayList<AI>();
    ArrayList<Projectile> Projectiles = new ArrayList<Projectile>();
    
    int fps;
    int camxoff=600;
    int camyoff=400;
    Time t=new Time();
	int angle;
    
    private Thread thread;
    private boolean running=false;
    
    final double UPDATE=1.0/60.0;
    
    public Play(){
    	bm.map1();
    	bm.addBlocks(blocks);
    	Block f= new Block(-1100,-1000,100,1000);
    	Block g= new Block(-1000,-1100,1000,100);
    	//Block h= new Block(-1000,0,1000,100);
    	//Block k= new Block(0,-1000,100,1000);
    	blocks.add(f);
    	blocks.add(g);
    	//blocks.add(h);
    	//blocks.add(k);
    	Jumper j= new Jumper(-300,-300,30,2);
    	Jumper a= new Jumper(-200,-400,30,2);
    	Jumper b= new Jumper(-400,-300,30,2);
    	Jumper c= new Jumper(-500,-300,30,2);
    	AIs.add(j);
    	AIs.add(a);
    	AIs.add(b);
    	AIs.add(c);
    }
    
    public Square getSquare(){
    	return s;
    }
    public InputHandler getHandler(){
    	return h;
    }
    public ArrayList<Block> getBlocks(){
    	return blocks;
    }
    public BlockMap getBlockMap(){
    	return bm;
    }
    public void start(){
    	cam=new Camera(0,0,camxoff,camyoff);
    	h=new InputHandler(this);
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
    	fps=0;
    	
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
    				Block b= blocks.get(i);
    				int smidx=(s.getX()+(int)s.getRect().getMaxX())/2;
    				int smidy=(s.getY()+(int)s.getRect().getMaxY())/2;
    				if(b.distance(smidx, smidy)<500){
    					if(s.getRect().intersects(b.getRect())){
    						s.fixVelocity(b);
    						s.fixPosition(blocks.get(i));
    					}
    				}
    			}
    			for(int i=0;i<AIs.size();i++){
    				if(AIs.get(i).getClass()==Jumper.class){
    						((Jumper)AIs.get(i)).tick(this);
    					
    					//((Jumper)AIs.get(i)).followSquare(s);
    	    			for(int j=0;j<blocks.size();j++){//Collision Checks
    	    				Block b= blocks.get(j);
    	    				int smidx=(AIs.get(i).getX()+(int)AIs.get(i).getRect().getMaxX())/2;
    	    				int smidy=(AIs.get(i).getY()+(int)AIs.get(i).getRect().getMaxY())/2;
    	    				if(b.distance(smidx, smidy)<500){
    	    					if(AIs.get(i).intersectsBlocks(blocks)[j]){
    	    						(AIs.get(i)).fixPosition(blocks.get(j));
    	    					}
    	    				}
    	    			}
    	    			for(int j=0;j<Projectiles.size();j++){
    	    			if(AIs.get(i).intersects(Projectiles.get(j).getRect())){
    	    				AIs.remove(i);
    	    				Projectiles.remove(j);
    	    				i--;
    	    			}
    	    			}

    				}
    			}
    			for(int i=0;i<Projectiles.size();i++){//Collision Checks
    				if(Projectiles.get(i).currentTime>=Projectiles.get(i).lifeTime){
    					Projectiles.remove(i);
    					i--;
    				}
    				else{
    				Projectiles.get(i).tick();
    				}    			
    			}
    			//
    			if(frameTime>=1.0){
    				//per second events
    				frameTime=0;
    				fps=frames;
    				frames=0;
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
        	g.drawString("FPS: "+fps,(int)cam.getX()+5,(int)cam.getY()+20);
        	
        	//g2d.rotate(Math.toRadians(45));
            //g2d.drawRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
            //g2d.rotate(-Math.toRadians(45));
        	g2d.drawRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        	if(s.getDashing()==true){
        		g2d.setColor(Color.RED);
        		g2d.drawRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        		g2d.setColor(Color.BLACK);
        	}
            g.drawArc(100, 100, 30, 30, 0, 360);
            g.drawArc(100, 100, 10, 10, 0, 140);
            for(int i=0;i<5;i++){
            	for(int j=0;j<5;j++){
            		g.drawRect(-1000+(i*200),-1000+(j*200),200,200);
            	}
            }
            for(int i=0;i<blocks.size();i++){
            	Block b= blocks.get(i);
            	g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            }
            for(int i=0;i<AIs.size();i++){
            	AI a= AIs.get(i);
            	g.drawRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());

            }
            for(int i=0;i<Projectiles.size();i++){
            	Projectile p=Projectiles.get(i);
            	g.drawRect((int)p.getRect().getX(),(int)p.getRect().getY(),(int)p.getRect().getWidth(),(int)p.getRect().getHeight());
            }
            
            g2d.translate(cam.getX(), cam.getY());//end of cam
            
        }
        catch(Exception e){
        }
        
    }

    
	public void updateSquare(){
		//int vx;
		//int vy;
		if(s.getDashing()==false){
			t.time=0;
			//vx= (int)s.getVelx();
			//vy= (int)s.getVely();
			s.move((double)s.getVelx(),(double)s.getVely());
			angle=(int) s.findAngle((int)s.getVely(), (int)s.getVelx());
		}
		else if(s.getDashing()==true){
			t.tick();
			//s.move((double)s.getVelx(),(double)s.getVely());
			s.setX(s.getX()+(int)(Math.cos(Math.toRadians(angle))*22));
			s.setY(s.getY()+(int)(Math.sin(Math.toRadians(angle))*22));
			if(t.getTimer()>=20){
				s.setDashing(false);
			}
		}
	}
	
	/**
	 * 
	 * @param x1 spawn x coordinate of projectile
	 * @param y1 spawn y coordinate of projectile
	 * @param x2 target x coordinate of projectile
	 * @param y2 target y coordinate of projectile
	 */
	public void spawnProjectile(int x1, int y1, int x2, int y2){
		Projectile p= new Projectile(x1,y1,x2,y2);
		Projectiles.add(p);
		//Projectile d= new Projectile(x2,y2,x2,y2);
		//Projectiles.add(d);

	}
	
    
}

