package ARPG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Play extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;//Ignore
	
    Square s = new Square();
    Camera cam;
    InputHandler h;
    BlockMap bm= new BlockMap(0,0,0,0,this);
    ArrayList<Block> blocks= new ArrayList<Block>();
    ArrayList<AI> AIs= new ArrayList<AI>();
    ArrayList<Hitbox> Projectiles = new ArrayList<Hitbox>();
    
    int fps;
    int totaltime=0;
    int totalkills=0;
    int camxoff=600;//camera offsets to "Center" the camera
    int camyoff=320;
    double ti=0;
    boolean debug=false;
    //for dashing
    Time t=new Time();
	int angle;
	//
	
	boolean dead=false;//temporary
    
    private Thread thread;
    private boolean running=false;
    
    final double UPDATE=1.0/60.0;
    
    public Play(){
    	//bm.map3();
    	Block f= new Block(-100,-0,100,1000);
    	Block l= new Block(-100,1000,100,1000);
    	Block g= new Block(0,-100,1000,100);
    	Block o= new Block(1000,-100,1000,100);
    	blocks.add(f);
    	blocks.add(l);
    	blocks.add(g);
    	blocks.add(o);
    	//bm.createMap(4,4);
    	bm.createArena(4, 4);
    	bm.addBlocks(blocks);
    	bm.printMap(bm.map);
    	s.setX((int)bm.getStartPos().getX()+bm.getScale()*5);
    	s.setY((int)bm.getStartPos().getY()+bm.getScale()*5);
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
    			if(s.getStunframes()<=0){
    			h.interpretInput(this);// reads inputs of the user in order to do things
    			}
    			//1.Collision Checks (Intersections)
    			checkCollisions();
    			//---------------------------------------------------------------------
    			
    			//2.Status checking
    			checkStatus();
    			//--------------------------------------------------------------------
    			
    			//--------------------------------------------------------------------
    			if(frameTime>=1.0){
    				//per second events
    				frameTime=0;
    				fps=frames;
    				frames=0;
    				if(!dead){
    				totaltime++;
    					if(totaltime%10==0||totaltime==3){
    						boolean foundspot=false;
    						//int num=0;
    						while(foundspot==false){
    							/**
    							int i= RN.nextInt(bm.map.length);
    							int j= RN.nextInt(bm.map[0].length);
    							if(bm.map[i][j]==0){
    								//num++;
    								//if(num==25){foundspot=true;}
    								foundspot=true;
    								spawnJumper((int)bm.getPixelPos(i,j).getY(),(int)bm.getPixelPos(i,j).getX(),30,5);
    							}
    							*/
    							Point p= randomPointnear(s.getX(),s.getY(),600);
    							//clusterSpawn((int)p.getX(),(int)p.getY(),4,30,6);
    							surroundSpawn(s.getMidx(),s.getMidy(),30,6,6,400);
    							foundspot=true;
    						}
    					}
    					
    					if(s.getHealth()<s.getMaxhealth()){//temporary life regen
    						s.setHealth(s.getHealth()+5);
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
    
    public void checkCollisions(){
    	for(int i=0;i<blocks.size();i++){//Intersection between blocks and player
			Block b= blocks.get(i);
			int smidx=(s.getX()+(int)s.getRect().getMaxX())/2;
			int smidy=(s.getY()+(int)s.getRect().getMaxY())/2;
			if(b.distance(smidx, smidy)<(blocks.get(i).getWidth()*2)){
				if(s.getRect().intersects(b.getRect())){
					//s.fixVelocity(b);
					s.fixPosition(blocks.get(i));
				}
			}
		}
		for(int i=0;i<AIs.size();i++){//Intersection between AIs and blocks and projectiles
			AI a = AIs.get(i);
			for(int j=0;j<blocks.size();j++){
				Block b= blocks.get(j);
				int smidx=(AIs.get(i).getX()+(int)AIs.get(i).getRect().getMaxX())/2;
				int smidy=(AIs.get(i).getY()+(int)AIs.get(i).getRect().getMaxY())/2;
				if(b.distance(smidx, smidy)<(blocks.get(j).getWidth()*2)+10){
					if(AIs.get(i).intersectsBlocks(blocks)[j]){
						(AIs.get(i)).fixPosition(blocks.get(j));
					}
				}
			}
			for(int j=0;j<Projectiles.size();j++){
				Hitbox h= Projectiles.get(j);
				if(AIs.get(i).getInvunerableframes()<=0){
					if(h.getClass()==Projectile.class){
						if(AIs.get(i).intersects(h.getRect())&&h.isIsfriendly()==true){
							AIs.get(i).setHealth(AIs.get(i).getHealth()-20);
							Projectiles.remove(j);
						}
					}
					else{
						if(AIs.get(i).intersects(h.getRect())&&h.isIsfriendly()==true){
							AIs.get(i).setHealth(AIs.get(i).getHealth()-50);
							AIs.get(i).setInvunerableframes(10);				
							double angle1= s.findAngle(s.getMidy()-(AIs.get(i).getY()+AIs.get(i).getSize()/2),s.getMidx()-(AIs.get(i).getX()+AIs.get(i).getSize()/2));
							AIs.get(i).move((Math.cos(Math.toRadians(angle1+180))*30),(Math.sin(Math.toRadians(angle1+180))*30));
							AIs.get(i).setStunframes(30);
						}
					}
				}
			}
			if(AIs.get(i).getClass()==Jumper.class){    			
    			if(s.isInvunerable()==false&&s.getInvunerableframes()==0){//any "damage" checks to player
    				if(AIs.get(i).getRect().intersects(s.getRect())&&((Jumper)AIs.get(i)).chargewaittime!=0){				
    					s.setHealth(s.getHealth()-(int)(s.getMaxhealth()*.7));//70% max health damage
    					s.setInvunerableframes(120);
    					s.knockback(s, AIs.get(i).getX()+AIs.get(i).getSize()/2, AIs.get(i).getY()+AIs.get(i).getSize()/2, 1);
					}
    				
    				if(s.getHealth()<=0){
						dead=true;//temp
					}
    			}
    			//((Jumper)AIs.get(i)).tick();
			}
			tickAi(AIs.get(i));
			if(AIs.get(i).getHealth()<=0){
				totalkills++;
				AIs.remove(i);
			}
		}
    }
    
    public void checkStatus(){
    	for(int i=0;i<Projectiles.size();i++){//Lifetime checking for player's projectiles
			if(Projectiles.get(i).getCurrentTime()>=Projectiles.get(i).getLifeTime()){
				Projectiles.remove(i);
			}
			else{
				Projectiles.get(i).tick();
			}    			
		}
    	//Square status
    	s.friction();
		if(s.isAttacking()==true){
	    	Hitbox h= new Hitbox(s.getX()-(s.getWidth()/2)-(int)(Math.cos(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),s.getY()-(s.getHeight()/2)-(int)(Math.sin(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),45,45,2);
			Projectiles.add(h);
			s.setAttackamount(s.getAttackamount()-30);    				
			if(s.getAttackamount()<=0){
				s.setAttacking(false);
			}
		}
		if(s.getInvunerableframes()>0){
			s.setInvunerableframes(s.getInvunerableframes()-1);
		}
		if(s.getStunframes()>0){
			s.setStunframes(s.getStunframes()-1);
		}
		if(s.getCurrdashcooldown()>0){
			s.setCurrdashcooldown(s.getCurrdashcooldown()-1);
		}
		//---------
		//AI status
		for(int i=0;i<AIs.size();i++){
			AI a= AIs.get(i);
			a.friction();
			if(a.getStunframes()>0){
				if(a.getClass()==Jumper.class){
					((Jumper)a).chargetime.time=0;
					((Jumper)a).jumping=false;
	    		} 			
				a.setStunframes(a.getStunframes()-1);
			}
			if(a.getInvunerableframes()>0){
				a.setInvunerableframes(AIs.get(i).getInvunerableframes()-1);
			}
		}
    }
    
    public void tickAi(AI a){
    	if(a.getStunframes()<=0){
    		if(a.getClass()==Jumper.class){
    			((Jumper)a).tick(this);
    		}
    		//add
    	}
    }
    
    public Point randomPointnear(int x, int y, int range){
    	Random RN= new Random();
    	Point p= new Point(x,y);
    	boolean looking=true;
    	while(looking){
    		int rx= RN.nextInt(range)+x-RN.nextInt(range);
    		int ry= RN.nextInt(range)+y-RN.nextInt(range);
    		Point m= bm.getMapPos(rx, ry);
    		if((m.getX()>0&&m.getX()<bm.map[0].length&&m.getY()>0&&m.getY()<bm.map.length)&&bm.map[(int)m.getX()][(int)m.getY()]==0&&s.distance(rx,ry,(int)s.getX(),(int)s.getY())>300){
    			p= new Point(rx,ry);
    			looking=false;
    		}
    	}
    	return p;
    }
    
    public void spawnJumper(int x, int y, int size, int vel){
		Jumper j= new Jumper(x,y,size,vel);	
		//Jumper k= new Jumper(s.getX(),s.getY(),30,2);	
		AIs.add(j);
    }
    
    public void clusterSpawn(int x, int y, int amount, int size, int vel){
    	Random RN= new Random();
    	boolean spawning=true;
    	while(spawning){
    		int rx= RN.nextInt(bm.getScale()*4)+x-RN.nextInt(bm.getScale()*4);
    		int ry= RN.nextInt(bm.getScale()*4)+y-RN.nextInt(bm.getScale()*4);
    		Point m= bm.getMapPos(rx, ry);
    		if((m.getX()>0&&m.getX()<bm.map[0].length&&m.getY()>0&&m.getY()<bm.map.length)&&bm.map[(int)bm.getMapPos(rx, ry).getX()][(int)bm.getMapPos(rx, ry).getY()]==0){
    			spawnJumper(rx,ry,size,vel);
    			amount--;
    			if(amount<=0){
    				spawning=false;
    			}
    		}
    	}
    }
    
    public void surroundSpawn(int x, int y, int size, int vel, int amount, int radius){
    	int increment= 360/amount;
    	for(int i=0;i<amount;i++){
    		int x1=(int)(x+(radius*(Math.cos(Math.toRadians(increment*i)))));
    		int y1=(int)(y+(radius*(Math.sin(Math.toRadians(increment*i)))));
    		Point m= bm.getMapPos((int)(x+(radius*(Math.cos(Math.toRadians(increment*i))))), (int)(y+(radius*(Math.sin(Math.toRadians(increment*i))))));
    		if((m.getX()>0&&m.getX()<bm.map[0].length&&m.getY()>0&&m.getY()<bm.map.length)&&(int)bm.map[(int)bm.getMapPos(x1, y1).getX()][(int)bm.getMapPos(x1, y1).getY()]==0){
    			spawnJumper(x1,y1,size,vel);
    		}   		
    	}
    }
    
    public void delayedAttack(double angle, int delay){
    	s.setAttacking(true);
    	s.setAttackamount(180);
    	s.setAttackangle(angle-(s.getAttackamount()/2));
    }
    
	/**
	 * Spawns a projectile from coord. (x1,y1) that shoots to (x2,y2)
	 * 
	 * @param x1 spawn x coordinate of projectile
	 * @param y1 spawn y coordinate of projectile
	 * @param x2 target x coordinate of projectile
	 * @param y2 target y coordinate of projectile
	 */
	public void spawnProjectile(int x1, int y1, int x2, int y2){
		Projectile p= new Projectile(x1,y1,x2,y2,40);
		//System.out.println("Spawned");
		p.setIsfriendly(true);
		Projectiles.add(p);
	}
	
	
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d= (Graphics2D)g; 
        g.setColor(Color.BLACK);
        try{
        	//Square
        	//g2d.rotate(40,camxoff,camyoff);
        	g2d.translate(-cam.getX(), -cam.getY());//begins cam
        	//Player
        	g2d.fillRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        	if(s.getHealth()>0){
        		g2d.drawRect(s.getX(), s.getY()-7, (s.getWidth()*s.getHealth()/s.getMaxhealth())-1, 5);
        	}
        	if(s.getCurrdashcooldown()>0){//Dash Cooldown bar
        		g2d.drawRect(s.getX(), s.getY()+s.getHeight()+2, (s.getWidth()*s.getCurrdashcooldown()/s.getDashcooldown()), 5);
        	}
        	//g2d.drawString(s.getCurrdashcooldown()+"",(int)s.getX(),(int)s.getY());
        	if(s.getDashing()==true){
        		g2d.setColor(Color.RED);
        		g2d.fillRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        		g2d.setColor(Color.BLACK);
        	}
        	if(s.getInvunerableframes()>0){//Animation for damage invulnerbility 
        		if(s.getInvunerableframes()%2==0){
        			g2d.setColor(Color.RED);
        		}
        		else{
        			g2d.setColor(Color.BLACK);
        		}
        		g2d.fillRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        	}
        	if(dead==true){
        		g2d.setColor(Color.RED);
        		g.drawLine(s.getX(), s.getY(), s.getX()+s.getWidth(), s.getY()+s.getHeight());
        		g.drawLine(s.getX(), s.getY()+s.getHeight(), s.getX()+s.getWidth(), s.getY());
        		//g.drawString("DEAD",(int)s.getX()+5,(int)s.getY()+20);
        		g2d.setColor(Color.BLACK);
        	}
        	//----------------------
        	if(debug){
        		for(int i=0;i<100;i++){//background grid. Non-interactable
            		for(int j=0;j<100;j++){
            			g.drawRect((i*60),(j*60),60,60);
            		}
            	}
        	}
            //Blocks
            int xoff=0;
            int yoff=-15;
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
            	if(AIs.get(i).getClass()==Jumper.class){
            		if(((Jumper)a).jumping==true){
            			g.setColor(Color.RED);
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Jumper)a).chargetime.time>((Jumper)a).chargewaittime-20){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				if(((Jumper)a).chargetime.time>((Jumper)a).chargewaittime-10){
                				g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
                			}
            			}
            			g.setColor(Color.BLACK);
            		}
            	}
            	
            	if(a.getStunframes()>0){
            		Color c= new Color(100,200,1);
            		g.setColor(c);
            		g.drawRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());
            		g.setColor(Color.BLACK);
            	}
            	if(a.getHealth()>0){//Enemy Hp bars
            		g2d.drawRect(a.getX(), a.getY()-7, (int)(a.getRect().getWidth()*a.getHealth()/s.getMaxhealth()), 5);
            	}
            	if(debug){          		
            		g.drawString(s.distance((int)a.getX(), (int)a.getY(),(int)s.getX(),(int)s.getY())+"",(int)a.getX(), (int)a.getY());  
            	}
            } 
            //Projectiles
            for(int i=0;i<Projectiles.size();i++){
            	Hitbox p=Projectiles.get(i);
            	g.drawRect((int)p.getRect().getX(),(int)p.getRect().getY(),(int)p.getRect().getWidth(),(int)p.getRect().getHeight());
            }
            if(debug){
            	for(int i=0;i<bm.map.length;i++){
            		for(int j=0;j<bm.map.length;j++){
            			g.drawString(i+","+j, bm.scale*i, bm.scale*j);
            		}
            	}
            }
        	g.drawString("FPS: "+fps,(int)cam.getX()+5,(int)cam.getY()+20);//FPS Counter (Top-Right)
        	g.drawString("Time: "+totaltime,(int)cam.getX()+50,(int)cam.getY()+20);
        	g.drawString("Kills: "+totalkills,(int)cam.getX()+100,(int)cam.getY()+20);
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
		//int j= (int) bm.getMapPos(s.getX(), s.getY()).getX();
		//int i= (int) bm.getMapPos(s.getX(), s.getY()).getY();
		//System.out.println(j+" , "+i);
		//System.out.println(bm.getPixelPos(i,j).getX()+" , "+bm.getPixelPos(i,j).getY());
		if(s.getCurrattackcooldown()>0){
			s.setCurrattackcooldown(s.getCurrattackcooldown()-1);
		}
		
		if(s.getDashing()==false){
			s.setInvunerable(false);
			t.time=0;
			s.move((double)s.getVelx(),(double)s.getVely());
			angle=(int) s.findAngle((int)s.getVely(), (int)s.getVelx());
		}
		else if(s.getDashing()==true){
			s.setInvunerable(true);
			t.tick();
			s.setX(s.getX()+(int)(Math.cos(Math.toRadians(angle))*8));
			s.setY(s.getY()+(int)(Math.sin(Math.toRadians(angle))*8));
			if(t.getTimer()>=20){
				s.setDashing(false);
				s.setCurrdashcooldown(s.getDashcooldown());
			}
		}

		
	}
	
	
	
	public static void main(String[] args){
        Play p= new Play(); //Jpanel Class
        Input i= new Input(p);//MouseListener
        p.addMouseListener(i);
        JFrame frame= new JFrame();
        //frame.setSize(500,500);
        frame.setSize(1300,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(i);
        frame.add(p);
        frame.setVisible(true);
        p.start();
    }
	
	public void reset(){
		for(int i=0;i<AIs.size();i++){
			AIs.remove(i);
			i--;
		}
		totaltime=0;
		totalkills=0;
		s.setHealth(s.getMaxhealth());
		dead=false;
	}
}

