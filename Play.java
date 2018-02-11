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
    ArrayList<Circle> Circles= new ArrayList<Circle>();
    
    int fps;
    int totaltime=0;
    int totalkills=0;
    int wave=1;
    int camxoff=600;//camera offsets to "Center" the camera
    int camyoff=320;
    double ti=0;
    boolean debug=false;
    //for dashing
    Time t=new Time();
	int angle;
	//
	
	boolean dead=false;//temporary
	boolean inbreak=false;
	boolean page1=true;
    
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
    public Time getJumptime(){
    	return t;
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
    public ArrayList<Hitbox> getProjectiles(){
    	return Projectiles;
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
    	//Random RN= new Random();
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
    				//
    				if(!dead&&inbreak==false){
    					totaltime++;
    					spawnEnemies();
    					if(s.getHealth()<s.getMaxhealth()){//temporary life regen
    						s.setHealth(s.getHealth()+5);
    					}
    					if(totaltime%60==0){
    						wave++;
    						s.setSkillpoints(s.getSkillpoints()+1);
    						inbreak=true;
    					}
    				}
    				else if(inbreak==true){
    					
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
    	
		for(int j=0;j<Projectiles.size();j++){
			boolean isdead=false;
			Hitbox h= Projectiles.get(j);
			for(int i=0;i<AIs.size();i++){
				//System.out.println(AIs.get(i).getInvunerableframes());
			if(AIs.get(i).getInvunerableframes()<=0){
				if(h.getClass()==Projectile.class){
					if(AIs.get(i).getRect().intersects(h.getRect())&&h.hurtsEnemy()==true&&((Projectile)h).isSquareshot()){
						AIs.get(i).setHealth(AIs.get(i).getHealth()-((Projectile)h).getDamage());
						AIs.get(i).setStunframes(120);
						s.setHealth(s.getHealth()+10);
						s.boostSpeed(2, s.getSpeedboostduration());
						isdead=true;
						if(s.isFireupgrade()){
							AIs.get(i).setDotframes(60);
							AIs.get(i).setDotdamage(1);
						}
					}
					else if(AIs.get(i).intersects(h.getRect())&&h.hurtsEnemy()==true){
						AIs.get(i).setHealth(AIs.get(i).getHealth()-((Projectile)h).getDamage());
						AIs.get(i).setStunframes(90);
						isdead=true;
					}
				}
				else{
					if(AIs.get(i).intersects(h.getRect())&&h.hurtsEnemy()==true){
						AIs.get(i).setHealth(AIs.get(i).getHealth()-calculateAttackdamage(s.getAttackdamage(),AIs.get(i)));
						AIs.get(i).setInvunerableframes(10);				
						double angle1= s.findAngle(s.getMidy()-(AIs.get(i).getY()+AIs.get(i).getSize()/2),s.getMidx()-(AIs.get(i).getX()+AIs.get(i).getSize()/2));
						if(bm.openPoint(AIs.get(i).getX()+(int)(Math.cos(Math.toRadians(angle1+180))*30), AIs.get(i).getY()+(int)(Math.sin(Math.toRadians(angle1+180))*30))){
							AIs.get(i).move((Math.cos(Math.toRadians(angle1+180))*30),(Math.sin(Math.toRadians(angle1+180))*30));
						}
						AIs.get(i).setStunframes(AIs.get(i).getStunframes()+30);
						s.setHealth(s.getHealth()+s.getHealthonhit());
					}
				}
			}
			}
			if(h.getRect().intersects(s.getRect())){
				if(h.getClass()==Projectile.class){
					if(((Projectile)h).isBomb()&&h.getRect().intersects(s.getRect())&&s.getParryframes()>0){//Parry bomb projectiles
						((Projectile)h).setFriendlybomb(true);	
						((Projectile)h).changeAngle((int)h.getRect().getCenterX(), (int)h.getRect().getCenterY(),getHandler().getMouseCoords()[2], getHandler().getMouseCoords()[3]);
						((Projectile)h).setCurrentTime(0);
						h.setLifeTime((int)(h.distance((int)h.getRect().getCenterX(),(int)h.getRect().getCenterY(), getHandler().getMouseCoords()[2], getHandler().getMouseCoords()[3])/((Projectile)h).getVel()));
						s.freezeTime();
					}
					else if(h.getRect().intersects(s.getRect())&&h.hurtsAllies()==true&&s.getParryframes()>0){
						((Projectile)h).setHurtsallies(false);
						((Projectile)h).setHurtsenemy(true);
						((Projectile)h).setDamage(25);
						((Projectile)h).changeAngle((int)h.getRect().getCenterX(), (int)h.getRect().getCenterY(),getHandler().getMouseCoords()[2], getHandler().getMouseCoords()[3]);
						((Projectile)h).setCurrentTime(0);
						h.setLifeTime((int)(h.distance((int)h.getRect().getCenterX(),(int)h.getRect().getCenterY(), getHandler().getMouseCoords()[2], getHandler().getMouseCoords()[3])/((Projectile)h).getVel()));
						s.freezeTime();
					}
					
				}
				if(h.hurtsAllies()&&s.getInvunerableframes()<=0&&s.isInvunerable()==false){
					s.setHealth(s.getHealth()-h.getDamage());
					isdead=true;
				}
			}
			if(isdead){
				Projectiles.remove(j);
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
			for(int j=0;j<Circles.size();j++){
				Circle c=Circles.get(j);
				if(c.intersects(a)&&c.hurtsEnemy()){
					AIs.get(i).setHealth(AIs.get(i).getHealth()-100);
				}
			}
			if(AIs.get(i).intersects(s.getRect())){
				if(AIs.get(i).getDotframes()>0&&s.getDotframes()<=0){
					s.setDotframes(AIs.get(i).getDotframes()*3);
					s.setDotdamage(AIs.get(i).getDotdamage());
				}
				else if(s.getDotframes()>0&&AIs.get(i).getDotframes()<=0){
					AIs.get(i).setDotframes(s.getDotframes()*3);
					AIs.get(i).setDotdamage(s.getDotdamage());
				}
				if(AIs.get(i).getClass()==Jumper.class){    	
					if(s.getParryframes()>0){//Parrys result in delayed stuns. Must set both delay and stun duration in two lines always
						AIs.get(i).knockback(AIs.get(i), s.getMidx(), s.getMidy(), .5);
						AIs.get(i).setDelayedstunframes(15);
						AIs.get(i).setStunduration(60);
						s.freezeTime();
					}
					else if(s.isInvunerable()==false&&s.getInvunerableframes()==0){//any "damage" checks to player
						if(AIs.get(i).getRect().intersects(s.getRect())&&((Jumper)AIs.get(i)).getChargetime()>((Jumper)AIs.get(i)).getChargewaittime()){				
							s.setHealth(s.getHealth()-70);//70 health damage
							s.setInvunerableframes(30);
							s.knockback(s, AIs.get(i).getX()+AIs.get(i).getSize()/2, AIs.get(i).getY()+AIs.get(i).getSize()/2, 1);
						}
					}
				}
				if(a.getClass()==Teleporter.class){ 
					if(s.getParryframes()>0){//Parrys result in delayed stuns. Must set both delay and stun duration in two lines always
						AIs.get(i).knockback(AIs.get(i), s.getMidx(), s.getMidy(), 3);
						AIs.get(i).setDelayedstunframes(15);
						AIs.get(i).setStunduration(60);
						s.freezeTime();
					}
					else if(s.isInvunerable()==false&&s.getInvunerableframes()==0){//any "damage" checks to player
						if(AIs.get(i).getRect().intersects(s.getRect())){				
							s.setHealth(s.getHealth()-70);
							s.setInvunerableframes(30);
							s.knockback(s, AIs.get(i).getX()+AIs.get(i).getSize()/2, AIs.get(i).getY()+AIs.get(i).getSize()/2, 1);
						}
					}
				}
			}
			tickAi(AIs.get(i));
			if(AIs.get(i).getHealth()<=0){
				if(dead==false){//Cant get kills if your dead.
					totalkills++;
				}
				if(totalkills%2==0){
					s.setShotcharges(s.getShotcharges()+1);
				}
				if(AIs.get(i).getClass()==Sprayer.class && a.getStunframes()>30){
					projectileExplosion(a.getMidx(),a.getMidy(),true,false,50,20,40);
				}
				AIs.remove(i);
			}
			if(s.getHealth()<=0){
				dead=true;//temp
			}
		}
		for(int i=0;i<Circles.size();i++){
			Circle c= Circles.get(i);
			if(c.intersects(s)&&c.hurtsallies()&&s.getInvunerableframes()==0){
				s.knockback(s, c.getX(), c.getY(), 1.5);
				s.setHealth(s.getHealth()-30);
				s.setInvunerableframes(10);
				s.setStunframes(5);
			}
			if(c.getXoff()!=0||c.getYoff()!=0){
				circleTeleport(c);
			}
		}
    }
    
    /**
     * Checks the statues of the objects in game. Also ticking their cooldowns/ effects/ timed events
     */
    public void checkStatus(){
    	for(int i=0;i<Projectiles.size();i++){//Lifetime checking for player's projectiles
    		Hitbox h= Projectiles.get(i);
			if(h.getCurrentTime()>=h.getLifeTime()){
				if(h.getClass()==Projectile.class){
					if(((Projectile)h).isBomb()){
						spawnCircle((int)h.getRect().getX(),(int)h.getRect().getY(),20,false,false,((Projectile)h).getDelay());
						if(((Projectile)h).isFriendlybomb()==true){
							spawnDelayedcircle((int)h.getRect().getX(),(int)h.getRect().getY(),100,true,false,1+((Projectile)h).getDelay(),((Projectile)h).getDelay());
						}
						else if(((Projectile)h).isFriendlybomb()==false){
							spawnDelayedcircle((int)h.getRect().getX(),(int)h.getRect().getY(),100,false,true,1+((Projectile)h).getDelay(),((Projectile)h).getDelay());
						}
					}
				}
				Projectiles.remove(i);
			}
			else if(s.getFreezeframes()<=0){
				Projectiles.get(i).tick();
			}    			
		}
    	for(int i=0;i<Circles.size();i++){
    		Circle c= Circles.get(i);
    		if(c.getLifetime()>0){
    			c.setLifetime(c.getLifetime()-1);
    		}
    		else if(c.canteleport()==true){//What happends end of teleport
    			Circle cs= new Circle(c.getX()+c.getXoff(),c.getY()+c.getYoff(), c.getRadius(),false, false, 1);
    			circleStun(cs,30,10);
    			Circles.remove(i);
    		}
    		else{
    			Circles.remove(i);
    		}
    		if(c.getSpawndelay()>0){
				c.setSpawndelay(c.getSpawndelay()-1);
			}
    	}
    	//Square status
    	s.friction();
		if(s.isAttacking()==true){
	    	Hitbox h= new Hitbox(s.getX()-(s.getWidth()/2)-(int)(Math.cos(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),s.getY()-(s.getHeight()/2)-(int)(Math.sin(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),45,45,2);
			h.setHurtsenemy(true);
	    	Projectiles.add(h);
			s.setAttackamount(s.getAttackamount()-30);    				
			if(s.getAttackamount()<=0){
				s.setAttacking(false);
			}
		}
		if(s.getSpeedboostframes()>0){
			s.setSpeedboostframes(s.getSpeedboostframes()-1);
		}
		else{
			s.setSpeedboost(0);
		}
		if(s.getInvunerableframes()>0){
			s.setInvunerableframes(s.getInvunerableframes()-1);
		}
		if(s.getStunframes()>0){
			s.setStunframes(s.getStunframes()-1);
		}
		if(s.getDelayedstunframes()>0){
			s.setDelayedstunframes(s.getDelayedstunframes()-1);
		}
		if(s.getStunduration()>0&&s.getDelayedstunframes()==0){
			s.setStunframes(s.getStunduration());
			s.setStunduration(0);
		}
		if(s.getCurrdashcooldown()>0){
			s.setCurrdashcooldown(s.getCurrdashcooldown()-1);
		}
		if(s.getParryframes()>0){
			s.setParryframes(s.getParryframes()-1);
		}
		if(s.getCurrparrycooldown()>0&&s.getParryframes()==0){
			s.setCurrparrycooldown(s.getCurrparrycooldown()-1);
		}
		if(s.getDotframes()>0){
			if(s.getDotframes()%2==0){s.setHealth(s.getHealth()-s.getDotdamage());}
			s.setDotframes(s.getDotframes()-1);
		}
		else{
			s.setDotdamage(0);
		}
		if(s.getFreezeframes()>0){
			s.setFreezeframes(s.getFreezeframes()-1);
		}
		if(s.getCurrfreezecooldown()>0){
			s.setCurrfreezecooldown(s.getCurrfreezecooldown()-1);
		}
		if(s.getCurrphasewalkcooldown()>0){
			s.setCurrphasewalkcooldown(s.getCurrphasewalkcooldown()-1);
		}
		//---------
		//AI status
		for(int i=0;i<AIs.size();i++){
			AI a= AIs.get(i);
			a.friction();
			if(a.getSpeedboostframes()>0){
				a.setSpeedboostframes(a.getSpeedboostframes()-1);
			}
			else{
				a.setSpeedboost(0);
			}
			if(a.getStunframes()>0){
				if(a.getClass()==Jumper.class){
					((Jumper)a).chargetime.time=0;
					((Jumper)a).jumping=false;
	    		} 	
				if(a.getClass()==Sprayer.class){
					//((Teleporter)a).chargetime.time=0;
					((Sprayer)a).shotcooldown.time=0;
					((Sprayer)a).setCanshoot(false);
	    		} 
				if(a.getClass()==Teleporter.class){
					((Teleporter)a).chargetime.time=0;
					((Teleporter)a).shotcooldown.time=0;
					((Teleporter)a).setCanshoot(false);
	    		} 	
				a.setStunframes(a.getStunframes()-1);
			}
			if(a.getDelayedstunframes()>0){
				a.setDelayedstunframes(a.getDelayedstunframes()-1);
			}
			if(a.getStunduration()>0&&a.getDelayedstunframes()==0){
				a.setStunframes(a.getStunduration());
				a.setStunduration(0);
			}
			if(a.getInvunerableframes()>0){
				a.setInvunerableframes(a.getInvunerableframes()-1);
			}
			if(a.getDotframes()>0){
				if(a.getDotframes()%2==0){a.setHealth(a.getHealth()-a.getDotdamage());}		
				a.setDotframes(a.getDotframes()-1);
			}
			else{
				a.setDotdamage(0);
			}
		}
    }
    
    public void tickAi(AI a){
    	if(a.getStunframes()<=0&&s.getFreezeframes()<=0){
    		if(a.getClass()==Jumper.class){
    			((Jumper)a).tick(this);
    		}
    		if(a.getClass()==Pusher.class){
    			((Pusher)a).tick(this);
    		}
    		if(a.getClass()==Sprayer.class){
    			((Sprayer)a).tick(this);
    		}
    		if(a.getClass()==Teleporter.class){
    			((Teleporter)a).tick(this);
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
    		if((m.getX()>0&&m.getX()<bm.map[0].length&&m.getY()>0&&bm.openPoint(ry, rx)&&s.distance(rx,ry,(int)s.getX(),(int)s.getY())>300)){
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
    public void spawnPusher(int x, int y, int size, int vel){
    	Pusher p= new Pusher(x,y,size,vel);
    	AIs.add(p);
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
    
    public int calculateAttackdamage(int basedamage, AI a){
    	if(a.getStunframes()>0){
    		return basedamage*2;
    	}
    	return basedamage;
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
		Projectile p= new Projectile(x1,y1,x2,y2,7,40);
		//System.out.println("Spawned");
		p.setHurtsenemy(true);
		Projectiles.add(p);
	}
	public void spawnProjectile(Projectile p){
		Projectiles.add(p);
	}
	public void spawnMultipleProjectiles(int x, int y, int angle, int amount, int anglespread, int damage){
		double interval= anglespread/amount;
		for(int i=0;i<amount;i++){
			Projectile p= new Projectile(x,y,x,y,7,40);
			p.setHurtsenemy(true);
			p.setDamage(damage);
			p.changeAngle(angle-(anglespread/2)+(interval*i));
			Projectiles.add(p);
		}		
	}
	public void spawnMultipleProjectiles(Projectile[] p){
		for(int i=0;i<p.length;i++){
			Projectiles.add(p[i]);
		}		
	}
	public void projectileExplosion(int x, int y, boolean hurtsenemy, boolean hurtsallies, int damage, int amount, int lifetime){
		int interval= 360/amount;
		for(int i=0;i<amount;i++){
			Projectile p= new Projectile(x,y,x,y,7,lifetime);
			p.setHurtsenemy(hurtsenemy);
			p.setHurtsallies(hurtsallies);
			p.changeAngle(interval*i);
			p.setDamage(damage);
			Projectiles.add(p);
		}
	}
	
	public void spawnCircle(int x, int y, int radius, boolean hurtsenemy, boolean hurtsallies, int lifetime){
		Circle c= new Circle(x,y,radius,hurtsenemy,hurtsallies,lifetime);
		Circles.add(c);
	}
	public void spawnCircle(Circle c){
		Circles.add(c);
	}
	public void spawnDelayedcircle(Circle c, int delay){
		c.setSpawndelay(delay);
		Circles.add(c);
	}
	public void spawnDelayedcircle(int x, int y, int radius, boolean hurtsenemy, boolean hurtsallies, int lifetime, int delay){
		Circle c= new Circle(x,y,radius,hurtsenemy,hurtsallies,lifetime);
		c.setSpawndelay(delay);
		Circles.add(c);
	}
	
	public void circleTeleport(Circle c){
		for(int i=0;i<AIs.size();i++){
			if(c.intersects(AIs.get(i))&&AIs.get(i).getStunframes()<=0){
				AIs.get(i).setLocation(AIs.get(i).getX()+c.getXoff(), AIs.get(i).getY()+c.getYoff());
			}
		}
		if(c.intersects(s)&&s.getStunframes()<=0){
			s.setInvunerableframes(s.getInvunerableframes()+10);
			s.setLocation(s.getX()+c.getXoff(), s.getY()+c.getYoff());
		}
	}
	
	public void circleStun(Circle c, int duration, int damage){
		if(c.intersects(s)&&s.isInvunerable()==false&&s.getInvunerableframes()<=0){
			s.stop();
			s.setHealth(s.getHealth()-damage);
			s.setStunframes(duration);
		}
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
        	if(s.isCanfreeze()==true){
        		if(s.getCurrfreezecooldown()<=0){
        			Color c= new Color(1,1,100);
            		g.setColor(c); 
        			g.drawString("F", s.getX()-8, s.getY()+10);
        		}
        	}if(s.isPhasewalkupgrade()==true){
        		if(s.getCurrphasewalkcooldown()<=0){
        			g.setColor(Color.RED);
        			g.drawString("P", s.getX()-8, s.getY()+20);
        		}
        	}
        	g2d.setColor(Color.BLACK);
        	g2d.fillRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        	if(s.getHealth()>0){
        		g2d.drawRect(s.getX(), s.getY()-7, (s.getWidth()*(100*s.getHealth())/(100*s.getMaxhealth()))-1, 5);
        	}
        	if(s.getSpeedboostframes()>0){
        		g2d.drawRect(s.getX()+(int)s.getRect().getWidth(), s.getY(), 5,(s.getHeight()*(s.getSpeedboostframes())/(s.getSpeedboostduration()))-1);
        	}
        	if(s.getCurrdashcooldown()>0){//Dash Cooldown bar
        		g2d.drawRect(s.getX(), s.getY()+s.getHeight()+1, (s.getWidth()*s.getCurrdashcooldown()/s.getDashcooldown()), 5);
        	}
        	if(s.getParryframes()==0&&s.getCurrparrycooldown()>0){//Parry Cooldown bar
        		g2d.drawRect(s.getX(), s.getY()+s.getHeight()+6, (s.getWidth()*s.getCurrparrycooldown()/s.getParrycooldown()), 5);
        	}
        	//g2d.drawString(s.getCurrdashcooldown()+"",(int)s.getX(),(int)s.getY());
        	if(s.getSpeedboostframes()>0){
        		g2d.setColor(Color.DARK_GRAY);
        	}
        	if(s.getDashing()==true){
        		g2d.setColor(Color.RED);
        	}
        	if(s.getInvunerableframes()>0&&s.getFreezeframes()<=0){//Animation for damage invulnerbility 
        		if(s.getInvunerableframes()%2==0){
        			g2d.setColor(Color.RED);
        		}
        		else{
        			g2d.setColor(Color.BLACK);
        		}
        	}
        	if(s.getStunframes()>0){
        		Color c= new Color(100,200,1);
        		g2d.setColor(c);
        		if(s.getParryframes()<=0){
        			g.drawString("Stunned!",(int)s.getX(), (int)s.getY()+(int)s.getRect().getHeight()+10);
        		}
        	}
        	if(s.getDotframes()>0){
        		g2d.setColor(Color.ORANGE); 
        		g2d.drawString("Burning!", s.getX(), s.getY()-10);     		    		
        	}
        	if(s.getParryframes()>0){
        		g2d.setColor(Color.GREEN);     		
        	}
        	g2d.fillRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
    		g2d.setColor(Color.BLACK);
        	for(int i=0;i<s.getShotcharges();i++){
        		g.setColor(Color.RED);
        		g.drawString("l", s.getMidx()+(3*i)-5, s.getMidy()+4);
        		g.setColor(Color.BLACK);
        	}
        	if(inbreak==true&&AIs.size()<=0){
        		g.drawString("Press O to continue", s.getX()+s.getWidth(), s.getY());
        		g.drawString("Skillpoints: "+s.getSkillpoints(), s.getX()+s.getWidth(), s.getY()+10);
        		if(page1==true){
        			g.drawString("(1) +2 Projectiles per shot", s.getX()+s.getWidth(), s.getY()+20);
        			g.drawString("(2) +1 Max speed Curr["+s.getMaxspeed()+"]", s.getX()+s.getWidth(), s.getY()+30);
        			g.drawString("(3) +25 Max Health Curr["+s.getMaxhealth()+"]", s.getX()+s.getWidth(), s.getY()+40);
        			g.drawString("(4) +10 Attack Damage Curr["+s.getAttackdamage()+"]", s.getX()+s.getWidth(), s.getY()+50);
        			g.drawString("(5) -.2 Dash Cooldown Curr["+(double)s.getDashcooldown()/60+"]", s.getX()+s.getWidth(), s.getY()+60);
        			g.drawString("(6) +2 Health on Hit Curr["+s.getHealthonhit()+"]", s.getX()+s.getWidth(), s.getY()+70);
        			g.drawString("(0) Ascension Page", s.getX()+s.getWidth(), s.getY()+80);
        		}
        		else{
        			if(s.isFireupgrade()==false){g.drawString("(1) Projectiles inflict 30 burn damage[2 SP]", s.getX()+s.getWidth(), s.getY()+20);}
        			else{g.drawString("(X) Fire upgrade obtained", s.getX()+s.getWidth(), s.getY()+20);}
        			if(s.isCanfreeze()==false){g.drawString("(2) Freeze Time on Parry(2 seconds; 10 sec cooldown;F)[2 SP]", s.getX()+s.getWidth(), s.getY()+30);}
        			else{g.drawString("(X) Freeze upgrade obtained", s.getX()+s.getWidth(), s.getY()+30);}
        			if(s.isPhasewalkupgrade()==false){g.drawString("(3) Phasewalk after dash (1.5 seconds; 5 sec cooldown; SHIFT)[2 SP]", s.getX()+s.getWidth(), s.getY()+40);}
        			else{g.drawString("(X) Phasewalk obtained", s.getX()+s.getWidth(), s.getY()+40);}
        			g.drawString("(4) Work in Progress", s.getX()+s.getWidth(), s.getY()+50);
        			g.drawString("(5) Work in Progress", s.getX()+s.getWidth(), s.getY()+60);
        			g.drawString("(6) Work in Progress", s.getX()+s.getWidth(), s.getY()+70);
        			g.drawString("(0) Back", s.getX()+s.getWidth(), s.getY()+80);
        		}
        	}
        	if(dead==true){
        		g2d.setColor(Color.RED);
        		g.drawLine(s.getX(), s.getY(), s.getX()+s.getWidth(), s.getY()+s.getHeight());
        		g.drawLine(s.getX(), s.getY()+s.getHeight(), s.getX()+s.getWidth(), s.getY());
        		g.drawString("TIME:"+totaltime+"  KILLS:"+totalkills+"  WAVE:"+wave,(int)s.getX()-55,(int)s.getY()-20);
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
            	if(a.getClass()==Pusher.class){
            		g.setColor(Color.GRAY);
            	}
            	if(a.getClass()==Sprayer.class){
            		Color c= new Color(200,100,20);
            		g.setColor(c);
            	}
            	if(a.getClass()==Teleporter.class){
            		Color c= new Color(20,20,200);
            		g.setColor(c);
            	}
            	g.drawRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());
            	g.setColor(Color.BLACK);//Jumper default color is black
            	if(AIs.get(i).getClass()==Jumper.class){
            		if(((Jumper)a).jumping==true){
            			g.setColor(Color.RED);
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Jumper)a).chargetime.time>((Jumper)a).getChargewaittime()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				if(((Jumper)a).chargetime.time>(((Jumper)a).getChargewaittime()*2)/3){
                				g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
                			}
            			}
            			g.setColor(Color.BLACK);
            		}
            	}
            	if(a.getClass()==Pusher.class){
            		if(((Pusher)a).shotcooldown.time>0){
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Pusher)a).shotcooldown.time>((Pusher)a).getShootcooldown()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				if(((Pusher)a).shotcooldown.time>(((Pusher)a).getShootcooldown()*2)/3){
            					g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
            				}
            			}
            		}
            		//g.drawString(((Pusher)a).shotcooldown.time+"",a.getX(),a.getY());
            	}
            	else if(a.getClass()==Sprayer.class){
            		if(((Sprayer)a).shotcooldown.time>0){
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Sprayer)a).shotcooldown.time>((Sprayer)a).getShootcooldown()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				if(((Sprayer)a).shotcooldown.time>(((Sprayer)a).getShootcooldown()*2)/3){
            					g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
            				}
            			}
            		}
            	}
            	else if(a.getClass()==Teleporter.class){
            		if(((Teleporter)a).shotcooldown.time-((Teleporter)a).getCastduration()>0){
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Teleporter)a).shotcooldown.time-((Teleporter)a).getCastduration()>((Teleporter)a).getShootcooldown()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				if(((Teleporter)a).shotcooldown.time-((Teleporter)a).getCastduration()>(((Teleporter)a).getShootcooldown()*2)/3){
            					g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
            				}
            			}
            		}
            		//g.drawString(((Teleporter)a).shotcooldown.time+"",a.getX(),a.getY());
            	}
            	
            	if(a.getDotframes()>0){
            		g2d.setColor(Color.ORANGE); 
            		g2d.drawString("Burning!", a.getX(), a.getY()-10);   		
            	}
            	if(a.getStunframes()>0){
            		Color c= new Color(100,200,1);
            		g.setColor(c);
            		g.drawString("Stunned!",(int)a.getX(), (int)a.getY()+(int)a.getRect().getHeight()+10);  
            	}
            	if(s.getFreezeframes()>0){
            		Color c= new Color(1,1,100);
            		g.setColor(c);    
            		g.drawString("Frozen!",(int)a.getX(), (int)a.getY()-10);  
            	}
            	g.drawRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());
            	g.setColor(Color.BLACK);
            	if(a.getHealth()>0){//Enemy Hp bars
            		g2d.drawRect(a.getX(), a.getY()-7, (int)(a.getRect().getWidth()*a.getHealth()/a.getMaxhealth()), 5);
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
            //Circles
            for(int i=0;i<Circles.size();i++){
            	Circle c= Circles.get(i);
            	if(c.getXoff()!=0||c.getYoff()!=0){
            		if(c.getSpawndelay()<=0){
            			g.drawArc(c.getX()-c.getRadius(), c.getY()-c.getRadius(), c.getRadius()*2, c.getRadius()*2, 0, 360);
            		}
            		g.drawLine(c.getX(), c.getY(), c.getX()+c.getXoff(), c.getY()+c.getYoff());
            		Color co= new Color(20,20,100);
            		g.setColor(co);
            		g.drawArc(c.getX()-c.getRadius()+c.getXoff(), c.getY()-c.getRadius()+c.getYoff(), c.getRadius()*2, c.getRadius()*2, 0, 360-((360*c.getLifetime())/c.getTotallifetime()));
            		g.setColor(Color.BLACK);
            	}
            	else if(c.getSpawndelay()<=0){
            		if(c.hurtsallies()){
            			g.setColor(Color.RED);
            		}
            		g.drawArc(c.getX()-c.getRadius(), c.getY()-c.getRadius(), c.getRadius()*2, c.getRadius()*2, 0, 360);
            		g.setColor(Color.BLACK);
            	}
            }
            if(debug){
            	for(int i=0;i<bm.map.length;i++){
            		for(int j=0;j<bm.map.length;j++){
            			g.drawString(i+","+j, bm.scale*i, bm.scale*j);
            		}
            	}
            }
        
        	g.drawString("FPS: "+fps+"  Time:"+totaltime+"  Kills:"+totalkills,(int)cam.getX()+5,(int)cam.getY()+20);//FPS Counter (Top-Right)
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
			s.setX(s.getX()+(int)(Math.cos(Math.toRadians(angle))*15));
			s.setY(s.getY()+(int)(Math.sin(Math.toRadians(angle))*15));
			if(t.getTimer()>=10){
				h.removeInput("SPACE");
				s.setDashing(false);
				s.setCurrdashcooldown(s.getDashcooldown());
				if(s.isPhasewalkupgrade()==true&&s.getCurrphasewalkcooldown()<=0){//Phase walk upgrade
					s.boostSpeed(2, 90);
	        		s.setInvunerableframes(90);
	        		s.setCurrphasewalkcooldown(s.getPhasewalkcooldown());
				}
			}
		}

		
	}
	
	public void spawnEnemies(){
		if(totaltime%10==0||totaltime==3){
			boolean foundspot=false;
			//int num=0;
			while(foundspot==false){
				foundspot=true;
				//Point p= randomPointnear(s.getX(),s.getY(),600);
				//clusterSpawn((int)p.getX(),(int)p.getY(),4,30,6);
				surroundSpawn(s.getMidx(),s.getMidy(),30,6,3+((totaltime-1)/60),400);//Spawns on more Jumper every 20 seconds
				foundspot=true;
			}
			foundspot=false;
			while(foundspot==false&&totaltime%20==0){
				Point p= randomPointnear(s.getX(),s.getY(),600);
				spawnPusher((int)p.getX(), (int)p.getY(),30,6);
				foundspot=true;
			}
			foundspot=false;
			while(foundspot==false&&(totaltime-10)%20==0){
				Point p= randomPointnear(s.getX(),s.getY(),600);
				Sprayer sp= new Sprayer((int)p.getX(),(int)p.getY(),30,6);
				AIs.add(sp);
				foundspot=true;
			}
			foundspot=false;
			while(foundspot==false&&totaltime>=40&&totaltime%30==0){
				Point p= randomPointnear(s.getX(),s.getY(),600);
				Teleporter tp= new Teleporter((int)p.getX(),(int)p.getY(),35,6);
				AIs.add(tp);
				foundspot=true;
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
	
	public void clearBlocks(){
		for(int i=0;i<blocks.size();i++){
			blocks.remove(0);
		}
	}
	
	public void reset(){
		for(int i=0;i<AIs.size();i++){
			AIs.remove(i);
			i--;
		}
		clearBlocks();
		bm= new BlockMap(0,0,0,0,this);
		bm.createArena(4, 4);
    	bm.addBlocks(blocks);
		s=new Square();
		totaltime=0;
		totalkills=0;
		wave=1;
		s.setShotcharges(3);
		s.setHealth(s.getMaxhealth());
		dead=false;
		inbreak=false;
	}
}

