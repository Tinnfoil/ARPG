package ARPG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

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
    int totaltime=0;
    int camxoff=600;//camera offsets to "Center" the camera
    int camyoff=400;
    //for dashing
    Time t=new Time();
	int angle;
	//
	
	boolean dead=false;//temporary
    
    private Thread thread;
    private boolean running=false;
    
    final double UPDATE=1.0/60.0;
    
    public Play(){
    	bm.map3();
    	bm.addBlocks(blocks);
    	Block f= new Block(-100,-0,100,1000);
    	Block l= new Block(-100,1000,100,1000);
    	Block g= new Block(0,-100,1000,100);
    	Block o= new Block(1000,-100,1000,100);
    	Block h= new Block(2000,1000,100,1000);
    	Block n= new Block(2000,0,100,1000);
    	Block k= new Block(1000,2000,1000,100);
    	Block v= new Block(0,2000,1000,100);
    	blocks.add(f);
    	blocks.add(l);
    	blocks.add(g);
    	blocks.add(o);
    	blocks.add(h);
    	blocks.add(n);
    	blocks.add(k);
    	blocks.add(v);
    	bm.CreateMap(10,10);
    	bm.printMap(bm.map);
    	//Jumper j= new Jumper(-300,-300,30,2);
    	//Jumper a= new Jumper(-200,-400,30,2);
    	//Jumper b= new Jumper(-400,-300,30,2);
    	//Jumper c= new Jumper(-500,-300,30,2);
    	//AIs.add(j);
    	//AIs.add(a);
    	//AIs.add(b);
    	//AIs.add(c);
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
     * Game Loop, runs the game by 60 computations/frames per second: Reduces needed processing
     * FPS is what we see
     */
    public void run(){
    	Random RN= new Random();
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
    			//GAME UPDATE-------------------------------------------
    			
    			updateSquare();//moves square according to velocities
    			cam.move(this);//moves the camera which follows the square/player
    			h.interpretInput(this);// reads inputs of the user in order to do things
    			
    			//1.Collision Checks (Intersections)
    			for(int i=0;i<blocks.size();i++){//Intersection between blocks and player
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
    			for(int i=0;i<AIs.size();i++){//Intersection between AIs and blocks and projectiles
    				if(AIs.get(i).getClass()==Jumper.class){
    					boolean isdead=false;
    	    			for(int j=0;j<blocks.size();j++){
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
    	    					isdead=true;
    	    					Projectiles.remove(j);
    	    				}
    	    			}	
	    				if(AIs.get(i).intersects(s.getRect())){
	    					dead=true;//temp
	    				}
	    				((Jumper)AIs.get(i)).tick(this);
	    			
	    				if(isdead){
	    					AIs.remove(i);
	    				}
	    			}
    			}
    			//---------------------------------------------------------------------
    			
    			//2.Status checking
    			for(int i=0;i<Projectiles.size();i++){//Lifetime checking for player's projectiles
    				if(Projectiles.get(i).currentTime>=Projectiles.get(i).lifeTime){
    					Projectiles.remove(i);
    				}
    				else{
    				Projectiles.get(i).tick();
    				}    			
    			}
    			//--------------------------------------------------------------------
    			
    			//--------------------------------------------------------------------
    			if(frameTime>=1.0){
    				//per second events
    				frameTime=0;
    				fps=frames;
    				frames=0;
    				if(!dead){
    				totaltime++;
    					if(totaltime%2==0){
    						Jumper j= new Jumper(RN.nextInt(2000),RN.nextInt(2000),30,2);	
    						AIs.add(j);
    					}
    				}
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
        	//g2d.rotate(40,camxoff,camyoff);
        	g2d.translate(-cam.getX(), -cam.getY());//begins cam
        	g.drawString("FPS: "+fps,(int)cam.getX()+5,(int)cam.getY()+20);//FPS Counter (Top-Right)
        	g.drawString("Time: "+totaltime,(int)cam.getX()+50,(int)cam.getY()+20);
        	//Player
        	g2d.fillRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        	if(s.getDashing()==true){
        		g2d.setColor(Color.RED);
        		g2d.fillRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        		g2d.setColor(Color.BLACK);
        	}
        	if(dead==true){
        		g2d.setColor(Color.RED);
        		g.drawString("DEAD",(int)s.getX()+5,(int)s.getY()+20);
        		g2d.setColor(Color.BLACK);
        	}
        	//----------------------

            for(int i=0;i<10;i++){//background grid. Non-interactable
            	for(int j=0;j<10;j++){
            		g.drawRect((i*200),(j*200),200,200);
            	}
            }
            //Blocks
            int xoff=10;
            int yoff=-70;
            for(int i=0;i<blocks.size();i++){
            	Block b= blocks.get(i);
            	g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            }
            for(int i=0;i<blocks.size();i++){
            	Block b= blocks.get(i);
            	g.drawLine(b.getX(), b.getY(), b.getX()+xoff, b.getY()+yoff);
            	g.drawLine(b.getX()+b.getWidth(), b.getY(), b.getX()+b.getWidth()+xoff, b.getY()+yoff);
            	g.drawLine(b.getX(), b.getY()+b.getHeight(), b.getX()+xoff, b.getY()+b.getHeight()+yoff);
            	g.drawLine(b.getX()+b.getWidth(), b.getY()+b.getHeight(), b.getX()+b.getWidth()+xoff, b.getY()+b.getHeight()+yoff);
            }
            for(int i=0;i<blocks.size();i++){
            	Block b= blocks.get(i);
            	g2d.setColor(Color.GREEN);
            	g.fillRect(b.getX()+xoff, b.getY()+yoff, b.getWidth(), b.getHeight());
            	g2d.setColor(Color.BLACK);
            	g.drawRect(b.getX()+xoff, b.getY()+yoff, b.getWidth(), b.getHeight());
            }
            
            //AIs
            for(int i=0;i<AIs.size();i++){
            	AI a= AIs.get(i);
            	g.drawRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());

            }
            
            //Projectiles
            for(int i=0;i<Projectiles.size();i++){
            	Projectile p=Projectiles.get(i);
            	g.drawRect((int)p.getRect().getX(),(int)p.getRect().getY(),(int)p.getRect().getWidth(),(int)p.getRect().getHeight());
            }
            g2d.translate(cam.getX(), cam.getY());//end of cam
            //g2d.rotate(-30,0,0);
        }
        catch(Exception e){
        }
        
    }

    /**
     * The movement "tick" of the player. Moves player according to the velocities
     */
	public void updateSquare(){
		if(s.getDashing()==false){
			t.time=0;
			s.move((double)s.getVelx(),(double)s.getVely());
			angle=(int) s.findAngle((int)s.getVely(), (int)s.getVelx());
		}
		else if(s.getDashing()==true){
			t.tick();
			s.setX(s.getX()+(int)(Math.cos(Math.toRadians(angle))*17));
			s.setY(s.getY()+(int)(Math.sin(Math.toRadians(angle))*17));
			if(t.getTimer()>=20){
				s.setDashing(false);
			}
		}
	}
	
	/**
	 * Spawns a projectile from coord. (x1,y1) that shoots to (x2,y2)
	 * 
	 * @param x1 spawn x coordinate of projectile
	 * @param y1 spawn y coordinate of projectile
	 * @param x2 target x coordinate of projectile
	 * @param y2 target y coordinate of projectile
	 */
	public boolean spawnProjectile(int x1, int y1, int x2, int y2){
		Projectile p= new Projectile(x1,y1,x2,y2);
		System.out.println("Spawned");
		Projectiles.add(p);
		return true;
	}
	
    
}

