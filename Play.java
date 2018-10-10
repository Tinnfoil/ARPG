package ARPG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Play extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;//Ignore
	
	JFrame frame= new JFrame();
    Square s = new Square();
    Camera cam;
    Input i;
    InputHandler h;
    SoundEngine sound;
    BlockMap bm= new BlockMap(0,0,0,0,this);
    ArrayList<Block> blocks= new ArrayList<Block>();
    ArrayList<AI> AIs= new ArrayList<AI>();
    ArrayList<Hitbox> Projectiles = new ArrayList<Hitbox>();
    ArrayList<Circle> Circles= new ArrayList<Circle>();
    ArrayList<Drop>	Drops= new ArrayList<Drop>();
    ArrayList<Dialogue> Dialogues= new ArrayList<Dialogue>();
    ArrayList<Button> Buttons= new ArrayList<Button>();
    ArrayList<Image> Images= new ArrayList<Image>();
    
    int fps;
    double scale=1;
    int totaltime=0;
    int deathtimeframes=0;
    int deathtime=0;
    int endtime=7;//7
    int postgameframes=0; int postcreditspeed=1;
    int totalkills=0;
    int deaths=0;
    int wave=1;
    int camxoff;//camera offsets to "Center" the camera 620
    int camyoff;//330
    int borderframes;
    int mousex=0;
    int mousey=0;
    boolean opening;
    boolean cinematic;
    boolean deathbound=false;
    int deathboundkills=0;
    boolean character=false;
    double ti=0;
    boolean debug=false;
    String tip="";
    //for dashing
    Time t=new Time();
	int angle;
	//
	
    int xoff=0; int xmax=0;
    int yoff=0; int ymax=25;
	
	boolean startscreen=true; int startframes=0; int poststartframes=0;
	boolean win=false;
	boolean dead=false;//temporary
	boolean inbreak=false;
	boolean page1=true;
	
	//For easy,normal, hard mode. 1 is for normal mode
	double difficultyvarible=1;
	boolean easy=false;
	boolean hard=false;
     
    private Thread thread;
    private boolean running=false;
    
    double UPDATE=1.0/60;
    int c;
    
    public Play() throws Exception{
    	//bm.map3();
    	//bm.createMap(4,4);
    	sound=new SoundEngine("Intro","Intro",1,this);
    	sound.startUp();
    	displayDialogues();
    	
		bm= new BlockMap(0,0,0,0,this);
		bm.createArena(4, 4);
    	bm.addBlocks(blocks);
    	
    	addWalls();
    	borderframes=125;
    	opening=true;
    	
        i= new Input(this);//MouseListener
        addMouseListener(i);
        addMouseWheelListener(i);
        addMouseMotionListener(i);
        frame.setSize(1300,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(i);
        frame.add(this);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setUndecorated(true);
        frame.setVisible(true);
        camxoff=(int)frame.getBounds().getWidth()/2-getSquare().getWidth()*2;
        camyoff=(int)frame.getBounds().getHeight()/2-getSquare().getHeight();
        
    	addButtons();
    	//Jumper j= new Jumper(-300,-300,30,2);
    	//AIs.add(j);
    	changedifficulty();
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
    public void addWalls(){
    	Block leftwall= new Block(-99,0,100,bm.getScale()*bm.map.length);
    	Block rightwall= new Block(bm.startx+(bm.getScale()*bm.map.length)-1,0,100,bm.getScale()*bm.map.length);
    	Block topwall= new Block(0,-99,bm.getScale()*bm.map[0].length,100);
    	Block bottomwall= new Block(0,bm.starty+(bm.getScale()*bm.map.length)-1,bm.getScale()*bm.map.length,100);
    	blocks.add(leftwall);
    	blocks.add(rightwall);
    	blocks.add(topwall);
    	blocks.add(bottomwall);
    }
    public void start(){
    	cam=new Camera(0,0,camxoff,camyoff);
    	h=new InputHandler(this);
    	thread= new Thread(this);
    	thread.run();
    }
    public void addButtons(){
    	Button start= new Button(camxoff,camyoff,70,20,"Start Game");
    	//b.setButton((camxoff+(int)fm.stringWidth(b.getFunction())/2-(int)fm.stringWidth(b.getFunction())/4), camyoff, (int)fm.stringWidth(b.getFunction())+(int)fm.stringWidth(b.getFunction())/2, 20);
    	Buttons.add(start);
    }
    public void clearButtons(){
    	for(int i=0;i<Buttons.size();i++){
    		Buttons.remove(i);
    		i--;
    	}
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
    			
    			try {
					updateSquare();
				} catch (Exception e1) {
					e1.printStackTrace();
				}//moves square according to velocities
    			if(startscreen){
    				cam.strictmove(this);//moves the camera which follows the square/player
    			}
    			else{
        			cam.move(this);//moves the camera which follows the square/player
    			}
    			if(s.getStunframes()<=0){
    				h.interpretInput(this);// reads inputs of the user in order to do things
    			}
    			//1.Collision Checks (Intersections)
    			try {
					checkCollisions();
				} catch (Exception e) {
					e.printStackTrace();
				}
    			//---------------------------------------------------------------------
    			
    			//2.Status checking
    			try {
					checkStatus();
				} catch (Exception e) {					
					e.printStackTrace();
				}
    			//--------------------------------------------------------------------
    			
    			//--------------------------------------------------------------------
    			if(frameTime>=1.0){
    				//System.out.println(c);
    				c=0;
    				//per second events
    				frameTime=0;
    				fps=frames;
    				frames=0;
    				//
    				if(s.getHealth()<s.getMaxhealth()&&deathbound==false&&dead==false){//temporary life regen 					
						if(inbreak==true&&AIs.size()==0){
							s.setHealth(s.getHealth()+10);
						}
						else{
							if(easy){
								s.setHealth(s.getHealth()+4);
							}
							s.setHealth(s.getHealth()+1);
						}
					}
    				if(!dead&&inbreak==false){
    					if(startscreen==false){
    						totaltime++;
    					}
    					spawnEnemies();
    					if(totaltime%60==0&&win==false){
    						wave++;
    						if(wave==2){
    							s.setSkillpoints(s.getSkillpoints()+2);
    						}
    						else{
    							s.setSkillpoints(s.getSkillpoints()+1);
    						}
    						inbreak=true;
    					}
    				}
    				else if(inbreak==true){

    				}
    				if(deathbound==true){
    					s.boostSpeed(-2, 60);
    					deathtime++;
    				}
    				
    				if(inbreak==true&&totaltime==300&&AIs.size()==0){
    					win=true;
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
    
    public void checkCollisions() throws Exception{
    	Random RN= new Random();
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
						AIs.get(i).setHealth(AIs.get(i).getHealth()-calculateAttackdamage(((Projectile)h).getDamage(),AIs.get(i)));
						AIs.get(i).setStunframes(120);
						if(!deathbound){
							s.setHealth(s.getHealth()+10);
						}
						if(!deathbound){
							s.boostSpeed(2, s.getSpeedboostduration());
						}
						if(((Projectile)h).getDamage()>=99){
							//Circle c=new Circle(AIs.get(i).getMidx(),AIs.get(i).getMidy(),100,false,false,2);
							//circleStun(c,30,34,true,false);
							//Circles.add(c);
						}
						isdead=true;
						hitAnimation(s, AIs.get(i).getMidx(), AIs.get(i).getMidy(), RN.nextInt(4)+1, 90,5,"BLACK");
						if(s.isFireupgrade()){
							AIs.get(i).setDotframes(80);
							AIs.get(i).setDotdamage(1);
						}
					}
					else if(AIs.get(i).intersects(h.getRect())&&h.hurtsEnemy()==true){
						AIs.get(i).setHealth(AIs.get(i).getHealth()-calculateAttackdamage(((Projectile)h).getDamage(),AIs.get(i)));
						AIs.get(i).setStunframes(90);
						isdead=true;
					}
					else if(AIs.get(i).intersects(h.getRect())&&((Projectile)h).isHeal()==true){
						AIs.get(i).setHealth(AIs.get(i).getHealth()+((Projectile)h).getHealamount());
						AIs.get(i).setStunframes(0);
						isdead=true;
					}
				}
				else{
					if(AIs.get(i).intersects(h.getRect())&&h.hurtsEnemy()==true){
						AIs.get(i).setHealth(AIs.get(i).getHealth()-calculateAttackdamage(h.getDamage(),AIs.get(i)));
						AIs.get(i).setInvunerableframes(10);				
						double angle1= s.findAngle(s.getMidy()-(AIs.get(i).getY()+AIs.get(i).getSize()/2),s.getMidx()-(AIs.get(i).getX()+AIs.get(i).getSize()/2));
						Point p= new Point(AIs.get(i).getX()+(int)(Math.cos(Math.toRadians(angle1+180))*30), AIs.get(i).getY()+(int)(Math.sin(Math.toRadians(angle1+180))*30));
						if(bm.map[(int)bm.getMapPos((int)p.getX(), (int)p.getY()).getX()][(int)bm.getMapPos((int)p.getX(), (int)p.getY()).getY()]==0){
							AIs.get(i).move((Math.cos(Math.toRadians(angle1+180))*30),(Math.sin(Math.toRadians(angle1+180))*30));
						}
						AIs.get(i).setStunframes(AIs.get(i).getStunframes()+30);
						if(s.isCanlifesteal()){
							if(!deathbound){
								s.setHealth(s.getHealth()+s.getHealthonhit());
							}
							s.setCanlifesteal(false);
						}
						//hitAnimation(AIs.get(i),s.getX()+(s.getWidth()/2)-(int)(Math.cos(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),s.getY()+(s.getHeight()/2)-(int)(Math.sin(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),RN.nextInt(2)+1,60,10,"BLACK");
						hitAnimation(s, AIs.get(i).getMidx(), AIs.get(i).getMidy(), RN.nextInt(4)+1, 60,10,"BLACK");
					}
				}
			}
			}
			if(h.getRect().intersects(s.getRect())){
				if(h.getClass()==Projectile.class){
					if(((Projectile)h).isBomb()&&h.getRect().intersects(s.getRect())&&s.getParryframes()>0&&((Projectile)h).isFriendlybomb()==false){//Parry bomb projectiles
						((Projectile)h).setFriendlybomb(true);	
						((Projectile)h).changeAngle((int)h.getRect().getCenterX(), (int)h.getRect().getCenterY(),getHandler().getMouseCoords()[2], getHandler().getMouseCoords()[3]);
						((Projectile)h).setCurrentTime(0);
						((Projectile)h).setDamage(200);
						h.setLifeTime(2+(int)(h.distance((int)h.getRect().getCenterX(),(int)h.getRect().getCenterY(), getHandler().getMouseCoords()[2], getHandler().getMouseCoords()[3])/((Projectile)h).getVel()));
						hitAnimation2(s,(int)h.getRect().getX(),(int)h.getRect().getY(),5,60,13,"BLACK");
						//sound effect
						sound.playeffect("Parry"); 
						if(s.getFreezeframes()<=0){sound.playeffect("Crowdoohcheer");}
						s.freezeTime(this); if(s.isSpinupgrade()==true){s.spin();}
					}
					else if(h.getRect().intersects(s.getRect())&&h.hurtsAllies()==true&&s.getParryframes()>0){
						((Projectile)h).setHurtsallies(false);
						((Projectile)h).setHurtsenemy(true);
						((Projectile)h).setDamage(100);
						((Projectile)h).changeAngle((int)h.getRect().getCenterX(), (int)h.getRect().getCenterY(),getHandler().getMouseCoords()[2], getHandler().getMouseCoords()[3]);
						((Projectile)h).setCurrentTime(0);
						h.setLifeTime((int)(h.distance((int)h.getRect().getCenterX(),(int)h.getRect().getCenterY(), getHandler().getMouseCoords()[2], getHandler().getMouseCoords()[3])/((Projectile)h).getVel()));
						//hitAnimation(s,(int)h.getRect().getX(),(int)h.getRect().getY(),1,90,7,"BLACK");
						s.freezeTime(this); s.spin();
					}
					else if(h.getRect().intersects(s.getRect())&&((Projectile)h).isHeal()){
						s.setHealth(s.getHealth()+(((Projectile)h).getHealamount()/4));
						isdead=true;
					}
					
				}
				if(h.hurtsAllies()&&s.getInvunerableframes()<=0&&s.isInvunerable()==false){
					s.knockback(s, h.getMidx(), h.getMidy(), .2);//TINY KNOWCKBACK
					s.setHealth(s.getHealth()-takeDamage(h.getDamage()));
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
				if(c.intersects(a)&&c.hurtsEnemy()&&AIs.get(i).getInvunerableframes()<=0){
					int damage=c.getDamage();
					if(AIs.get(i).getClass()==Boss.class){
						damage=damage/2;
					}
					AIs.get(i).setHealth(AIs.get(i).getHealth()-damage);
				}
			}
			if(AIs.get(i).intersects(s.getRect())){//AIS to square
				if(AIs.get(i).getDotframes()>0&&s.getDotframes()<=0){
					s.setDotframes(AIs.get(i).getDotframes()*3);
					s.setDotdamage(AIs.get(i).getDotdamage());
				}
				else if(s.getDotframes()>0&&AIs.get(i).getDotframes()<=0){
					AIs.get(i).setDotframes(s.getDotframes()*3);
					AIs.get(i).setDotdamage(s.getDotdamage());
				}
				if(AIs.get(i).getClass()==Jumper.class){    	
					if(s.getParryframes()>0&&(AIs.get(i).getStunframes()<=0&&AIs.get(i).getDelayedstunframes()<=0)){//Parrys result in delayed stuns. Must set both delay and stun duration in two lines always
						AIs.get(i).knockback(AIs.get(i), s.getMidx(), s.getMidy(), 2);
						AIs.get(i).setDelayedstunframes(15);
						AIs.get(i).setStunduration(60);
						hitAnimation2(s,(int)AIs.get(i).getRect().getX(),(int)AIs.get(i).getRect().getY(),5,60,13,"BLACK");
						s.freezeTime(this); s.spin();
						if(s.getFreezeframes()<=0){sound.playeffect("Parry"); sound.playeffect("Cheer1");}
					}
					else if(s.isInvunerable()==false&&s.getInvunerableframes()==0&&AIs.get(i).getDelayedstunframes()<=0){//any "damage" checks to player
						if(AIs.get(i).getRect().intersects(s.getRect())&&((Jumper)AIs.get(i)).getCooldown().time>((Jumper)AIs.get(i)).getShootcooldown()){				
							s.setHealth(s.getHealth()-takeDamage(35+(int)(35*difficultyvarible)));//70 health damage
							s.setInvunerableframes(30);
							s.knockback(s, AIs.get(i).getX()+AIs.get(i).getSize()/2, AIs.get(i).getY()+AIs.get(i).getSize()/2, 1);
						}
					}
				}
				if(a.getClass()==Teleporter.class){ 
					if(s.getParryframes()>0&&(AIs.get(i).getStunframes()<=0&&AIs.get(i).getDelayedstunframes()<=0)){//Parrys result in delayed stuns. Must set both delay and stun duration in two lines always
						AIs.get(i).knockback(AIs.get(i), s.getMidx(), s.getMidy(), 3);
						AIs.get(i).setDelayedstunframes(15);
						AIs.get(i).setStunduration(60);
						hitAnimation2(s,(int)AIs.get(i).getRect().getX(),(int)AIs.get(i).getRect().getY(),5,60,13,"BLACK");
						s.freezeTime(this); s.spin();
						if(s.getFreezeframes()<=0){sound.playeffect("Parry");sound.playeffect("Cheer1");}
					}
					else if(s.isInvunerable()==false&&s.getInvunerableframes()==0&&AIs.get(i).getDelayedstunframes()<=0){//any "damage" checks to player
						if(AIs.get(i).getRect().intersects(s.getRect())&&AIs.get(i).getStunframes()<=0){				
							s.setHealth(s.getHealth()-takeDamage(25+(int)(24*difficultyvarible)));
							s.setInvunerableframes(30);
							s.knockback(s, AIs.get(i).getX()+AIs.get(i).getSize()/2, AIs.get(i).getY()+AIs.get(i).getSize()/2, 2);
						}
					}
				}
			}
			if(AIs.get(i).distance(s.getMidx(), s.getMidy())<(s.getWidth()*3)/2&&s.isStundashupgrade()&&s.getDashing()){
				//AIs.get(i).setStunframes(30);
				Circle c=new Circle(AIs.get(i).getMidx(),AIs.get(i).getMidy(),100,false,false,2);
				circleStun(c,30,34,true,false);
				Circles.add(c);
				s.setDashstunned(true);
			}
			tickAi(AIs.get(i));
			if(AIs.get(i).getHealth()<=0||AIs.get(i).isDead()==true){
				if(inbreak==true&&AIs.size()==1){
					if(!dead){
						sound.playeffect("Waveend2");
					}
					sound.changeTrack("Intro", "Intro", 1);
				}
				if(AIs.size()==1&&dead==false&&deathbound==false){
					sound.playeffect("Cheer"+(RN.nextInt(2)+1)+"");
				}
				if(dead==false){//Cant get kills if your dead.
					totalkills++;
					if(deathbound){
						deathboundkills++;
					}
					if(s.isPhasewalking()){
						if(s.getInvunerableframes()<90){s.setInvunerableframes(s.getInvunerableframes()+10);}
						if(s.getSpeedboostframes()<90){s.setSpeedboostframes(s.getSpeedboostframes()+10);}
					}
				}
				if(totalkills%2==0){
					s.setShotcharges(s.getShotcharges()+1);
				}
				if(AIs.get(i).getClass()==Sprayer.class && a.getStunframes()>30){
					projectileExplosion(a.getMidx(),a.getMidy(),true,false,0,20,RN.nextInt(20)+20);
				}
				if(AIs.get(i).getDotframes()>0){
					Circle c= new Circle(AIs.get(i).getMidx(),AIs.get(i).getMidy(),150,false,false,10);
					sound.playeffect("Explosion");
					circleBurn(c,120);
					Circles.add(c);
				}
				Drop d= new Drop(AIs.get(i).getMidx()-6,AIs.get(i).getMidy()-6,12,300,"GREEN");
				d.healthdrop(10);
				Drops.add(d);
				AIs.remove(i);
			}
			if(s.getHealth()<=0&deathbound==false&&dead==false){
				sound.changeVolume(-10);
				sound.playeffect("Deathbound");
				deathbound=true;
				s.setInvunerableframes(40);
				s.setHealth(1);
			}
			if(deathbound==true&&s.getHealth()<=0){
				//System.out.println("ded");
				s.setChargingattack(false);
    			int s= Dialogues.size();
    			for(int j=0;j<s;j++){
    				Dialogues.remove(0);
				}
				dead=true;
			}
		}
		for(int i=0;i<Circles.size();i++){
			Circle c= Circles.get(i);
			if(c.intersects(s)&&c.hurtsallies()&&(s.getInvunerableframes()<=0&&s.isInvunerable()==false)){
				s.knockback(s, c.getX(), c.getY(), 2);
				int damage=takeDamage((int)(c.getDamage()*difficultyvarible));
				if(easy&&deathbound&&s.getHealth()<damage){
					damage=s.getHealth()-1;
				}
				s.setHealth(s.getHealth()-damage);
				s.setInvunerableframes(10);
				s.setStunframes(5);	
			}
			if(c.getLifetime()==0&&(c.hurtsallies==true||c.hurtsenemy==true)){
				if(c.getDamage()>0){
					explodeAnimation(c.getX(),c.getY(),10,15);
				}
				else{
					explodeAnimation2(c.getX(),c.getY(),10,15);
				}
			}
			if(c.getXoff()!=0||c.getYoff()!=0){
				circleTeleport(c);
			}
		}
		for(int i=0;i<Drops.size();i++){
			Drop d=Drops.get(i);
			boolean pickedup=false;
			if(s.getRect().intersects(d.getRect())){
				if(d.isHealthdrop()&&s.getHealth()<s.getMaxhealth()){
					s.setHealth(s.getHealth()+d.getHealamount());
					pickedup=true;
				}
			}
			if(pickedup==true){
				Drops.remove(i);
			}
		}
    }
    
    /**
     * Checks the statues of the objects in game. Also ticking their cooldowns/ effects/ timed events
     * @throws Exception 
     */
    public void checkStatus() throws Exception{
    	Random RN= new Random();
    	sound.checkDone();
    	s.displaceVel();
    	s.displaceCam();
    	s.shakeCam();
    	if(deathbound){
    		if(deathtimeframes<(endtime*60)){
    			deathtimeframes++;
			}
    	}
    	else{
			if(deathtimeframes>0){
				deathtimeframes--;
			}
    	}
    	if(dead==true&&deathtime>endtime){
    		postgameframes++;
    		if(postgameframes==30){
    			sound.shutdown();
    		}
    		if(postgameframes==300){
    			postCredits();
    			sound.changeTrack("Intro", "Intro", 1);
    		}
			scale=1;
    	}
    	if(win&&!dead){
    		deathbound=true;
    	}
    	for(int i=0;i<Projectiles.size();i++){//Lifetime checking for player's projectiles
    		Hitbox h= Projectiles.get(i);
			if(h.getCurrentTime()>=h.getLifeTime()){
				if(h.getClass()==Projectile.class){
					if(((Projectile)h).isBomb()){
						spawnCircle((int)h.getRect().getX(),(int)h.getRect().getY(),20,false,false,((Projectile)h).getDelay(),0);
						if(((Projectile)h).isFriendlybomb()==true){
							Circle c= new Circle((int)h.getRect().getX(),(int)h.getRect().getY(),100,true,false,1+((Projectile)h).getDelay());
							c.setColor(h.getColor());
							//c.setDamage(300);
							spawnDelayedcircle(c,((Projectile)h).getDelay(),((Projectile)h).getDamage());
						}
						else if(((Projectile)h).isFriendlybomb()==false){
							Circle c= new Circle((int)h.getRect().getX(),(int)h.getRect().getY(),100,false,true,1+((Projectile)h).getDelay());
							c.setColor(h.getColor());
							spawnDelayedcircle(c,((Projectile)h).getDelay(),((Projectile)h).getDamage());
						}
					}
				}
				if(h.getClass()==Projectile.class){
					if(((Projectile)h).isBlood()){
						//Image image= new Image((int)h.getRect().getX(),(int)h.getRect().getY(),"Blood"+(RN.nextInt(5)+1),100);
						Image image= new Image((int)h.getRect().getX(),(int)h.getRect().getY(),"Blood0",45);
						//image.setVelx((int)(Math.cos(Math.toRadians(p.getAngle()))*p.getVel()));
						//image.setVely((int)(Math.sin(Math.toRadians(p.getAngle()))*p.getVel()));
						Images.add(image);
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
    			circleStun(cs,30,5-5+(int)(5*difficultyvarible),false,true);
    			Circles.remove(i);
    		}
    		else{
    			Circles.remove(i);
    		}
    		if(c.getSpawndelay()>0){
				c.setSpawndelay(c.getSpawndelay()-1);
			}
    	}
    	for(int i=0;i<Drops.size();i++){
    		Drop d= Drops.get(i);
    		if(d.getCurrenttime()>0){
    			d.setCurrenttime(d.getCurrenttime()-1);
    		}
    		else if(d.getCurrenttime()<=0){
    			Drops.remove(i);
    		}
    	}
    	for(int i=0;i<Dialogues.size();i++){
    		Dialogue d= Dialogues.get(i);
    		boolean remove=false;
    		if(d.getCurrintialwait()>=d.getIntialwait()){
    		if(d.getCurrdelay()<d.getDelay()){
    			d.setCurrdelay(d.getCurrdelay()+1);
    		}
    		else if(d.getCurrdelay()>=d.getDelay()){
    			if(d.getCurrlength()<d.getLength()){
    				d.setCurrlength(d.getCurrlength()+1);
    				d.setCurrdelay(0);
    			}
    			else if(d.getCurrwait()<d.getWait()){
    				d.setCurrwait(d.getCurrwait()+1);
    				if(d.getCurrwait()>=d.getWait()){
    					remove=true;
    				}
    			}
    		}
    		}
    		else{
    			d.setCurrintialwait(d.getCurrintialwait()+1);
    		}
    		if(remove==true){
    			Dialogues.remove(i);
    		}
    	}
    	//Square status
    	s.friction();
    	if(dead){
    		s.setVelx(0);
    		s.setVely(0);
    	}
		if(s.isChargingattack()){
			if(s.getAttackchargeframes()<s.getMaxattackchargeframes()){
				s.setAttackchargeframes(s.getAttackchargeframes()+1);
			}
			else if(s.getTotalattackchargeframes()%3==0){
				hitAnimation2(s,s.getMidx()+(int)(50/4*Math.cos(Math.toRadians(s.getAngle()-180))),s.getMidy()+(int)(50/4*Math.sin(Math.toRadians(s.getAngle()-180))),1,1,5,"RED");
				hitAnimation2(s,s.getMidx(),s.getMidy(),1,1,5,"RED");
			}
			s.boostSpeed(-2, 1);
			s.setTotalattackchargeframes(s.getTotalattackchargeframes()+1);
		}
		if(s.isAttacking()==true){
			Hitbox h;
			if(s.getAttackcombo()==3){
				h= new Hitbox(s.getX()-(s.getWidth()/2)-(int)(Math.cos(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*(30*s.getComboattacks())),s.getY()-(s.getHeight()/2)-(int)(Math.sin(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*(30*s.getComboattacks())),45,45,2);
			}
			else{
				h= new Hitbox(s.getX()-(s.getWidth()/2)-(int)(Math.cos(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),s.getY()-(s.getHeight()/2)-(int)(Math.sin(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),45,45,2);
			}
	    	h.setHurtsenemy(true);
	    	Projectiles.add(h);
	    	//System.out.println(s.getAttackchargeframes()+","+(s.getAttackdamage()+(((s.getAttackdamage()-40)*2*s.getAttackchargeframes())/s.getMaxattackchargeframes())));
	    	int strength=0;
	    	if(s.getAttackchargeframes()>=10){
	    		strength=s.getAttackchargeframes();
	    	}
	    	int damage=s.getAttackdamage()+(((s.getAttackdamage()-45)*2*strength)/s.getMaxattackchargeframes());
	    	//System.out.println(damage);
	    	if(s.getAttackcombo()==1){
	    		h.setDamage(damage);
	    		s.setAttackamount(s.getAttackamount()-s.getAttackinterval());    		
	    	}
	    	else if(s.getAttackcombo()==2){
	    		h.setDamage(damage);
	    		s.setAttackamount(s.getAttackamount()+s.getAttackinterval()); 
	    	}
	    	else if(s.getAttackcombo()==3){
	    		//System.out.println((h.getDamage()+damage));
	    		h.setDamage(h.getDamage()+damage);//Disgusting damage
	    		s.setAttackamount(0);
	    		
	    	}
	    	if(Math.abs(s.getAttackamount())<s.getAttackinterval()&&s.getAttackcombo()<3){
	    		getHandler().attacking=false;
				s.setAttacking(false);
				if(s.isComboupgrade()==true){
					s.setAttackcombo(s.getAttackcombo()+1);
				}
				s.setComboattacks(s.getComboattacks()+1);
	    	}
	    	else if(Math.abs(s.getAttackamount())<s.getAttackinterval()&&s.getComboattacks()==4){
				hitAnimation2(s,s.getX()+(s.getWidth()/2)-(int)(Math.cos(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),s.getY()+(s.getHeight()/2)-(int)(Math.sin(Math.toRadians(s.getAttackangle()+s.getAttackamount()))*60),RN.nextInt(2)+1,60,10,"BLACK");
				getHandler().attacking=false;
				s.setAttacking(false);
				if(s.isComboupgrade()==true){
					s.setAttackcombo(s.getAttackcombo()+1);
				}
				if(s.getAttackcombo()>=4){
					s.setAttackcombo(1);
				}
			}
	    	else if(s.getComboattacks()<4){
				s.setComboattacks(s.getComboattacks()+1);
			}

		}
		else if(s.isAttacking()==false&&s.isChargingattack()==false){
			s.setAttackchargeframes(0);
			s.setTotalattackchargeframes(0);
		}
		if(opening){
			if(borderframes<150){
				borderframes+=4;
			}
		}
		else{
			if(borderframes>0){
				borderframes-=4;
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
			if(s.getInvunerableframes()<=0&&s.isPhasewalking()==true){
				if(!cinematic){opening=true;}
				sound.warp();
				sound.playeffect("Cheer3");
				s.setPhasewalking(false);
				UPDATE=((double)1/(double)60);
				s.setCurrphasewalkcooldown(s.getPhasewalkcooldown());
			}
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
			if(!s.isPhasewalking()){
				UPDATE=((double)1/(double)30);
			}
			if(s.getParryframes()==0&&!s.isPhasewalking()){
				//sound.warp();
			}
		}
		else{
			if(!s.isPhasewalking()){
				UPDATE=((double)1/(double)60);
			}
		}
		if(s.getCurrparrycooldown()>0&&s.getParryframes()==0){
			s.setCurrparrycooldown(s.getCurrparrycooldown()-1);
		}
		if(s.getDotframes()>0){
			if(s.getDotframes()%4==0&&s.getHealth()>s.getDotdamage()){s.setHealth(s.getHealth()-takeDamage(s.getDotdamage()));}
			s.setDotframes(s.getDotframes()-1);
		}
		else{
			s.setDotdamage(0);
		}
		if(s.getFreezeframes()>0){
			s.setFreezeframes(s.getFreezeframes()-1);
			if(s.getFreezeframes()==0){
				sound.playeffect("Cheer3");
				sound.unfreeze();
			}
		}
		if(s.getCurrfreezecooldown()>0){
			s.setCurrfreezecooldown(s.getCurrfreezecooldown()-1);
		}
		if(s.getCurrphasewalkcooldown()>0){
			s.setCurrphasewalkcooldown(s.getCurrphasewalkcooldown()-1);
		}
		if(s.getCurrspincooldown()>0){
			if(s.getCurrspincooldown()%4==0){
				sound.playeffect("Whoosh");
			}
			if(s.getDizzylimit()<s.getDizzymaxlimit()){
				s.setDizzylimit(s.getDizzylimit()+1);
			}
			if(s.getCurrspincooldown()>0){
				if(s.getAttackcombo()==3){s.setAttackcombo(2);}
				s.setAttacking(true);
			}
			s.setAttacklimit(-1);
			s.setCurrspincooldown(s.getCurrspincooldown()-1);
			if(s.getCurrspincooldown()<=0){
				s.setAttacking(false);
			}
		}
		else{
			//System.out.println(s.getDizzylimit());
			if(s.getDizzylimit()>0){
				s.setDizzylimit(s.getDizzylimit()-1);
			}
			s.setAttacklimit(0);
		}
		if(s.getShakelimit()>0){
			if(s.getShakelimit()>30){
				s.setShakelimit(s.getShakelimit()-5);
			}
			else{
				s.setShakelimit(s.getShakelimit()-1);
			}
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
					((Jumper)a).getCooldown().time=0;
					((Jumper)a).jumping=false;
	    		} 	
				if(a.getClass()==Sprayer.class){
					//((Teleporter)a).chargetime.time=0;
					//((Sprayer)a).shotcooldown.time=0;
					((Sprayer)a).setCanshoot(false);
	    		} 
				if(a.getClass()==Teleporter.class){
					((Teleporter)a).getCooldown().time=0;
					((Teleporter)a).shotcooldown.time=0;
					((Teleporter)a).setCanshoot(false);
	    		} 	
				if(a.getClass()==Dodger.class){
					a.setStunframes(1);
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
		for(int i=0;i<Images.size();i++){
			if(Images.get(i).getLifetime()>0){			
				Images.get(i).setLifetime(Images.get(i).getLifetime()-1);
				Images.get(i).setX(Images.get(i).getX()+(int)Images.get(i).getVelx());
				Images.get(i).setY(Images.get(i).getY()+(int)Images.get(i).getVely());
			}
		}
		if(startscreen){
			startframes++;
		}
		else{
			poststartframes++;
		}			
    }
    
    public void tickAi(AI a) throws Exception{
    	if(a.getStunframes()<=0&&s.getFreezeframes()<=0){
    		if(a.getClass()==Jumper.class){
    			((Jumper)a).tick(this);
    		}
    		else if(a.getClass()==Pusher.class){
    			((Pusher)a).tick(this);
    		}
    		else if(a.getClass()==Sprayer.class){
    			((Sprayer)a).tick(this);
    		}
    		else if(a.getClass()==Teleporter.class){
    			((Teleporter)a).tick(this);
    		}
    		else if(a.getClass()==Healer.class){
    			((Healer)a).tick(this);
    		}
    		else if(a.getClass()==Dodger.class){
    			((Dodger)a).tick(this);
    		}
    		else if(a.getClass()==Boss.class){
    			((Boss)a).tick(this);
    		}
    		//add
    	}
    	else if(a.getStunframes()>0){
    		if(a.getClass()==Jumper.class){
    			((Jumper)a).getCooldown().setTimer(0);
    		}
    		if(a.getClass()==Boss.class){
    			((Boss)a).tick(this);
    		}
    		a.setVelx(0);
    		a.setVely(0);
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
		j.setMaxhealth(j.getMaxhealth()-40+(40*(int)difficultyvarible));
		j.setHealth(j.getMaxhealth());
		AIs.add(j);
    }
    public void spawnPusher(int x, int y, int size, int vel){
    	Pusher p= new Pusher(x,y,size,vel);
    	AIs.add(p);
    }
    
    /**
     * randomly spawing in a cluster with a targeted point
     * @param x
     * @param y
     * @param amount
     * @param size
     * @param vel
     */
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
    
    /**
     * Spawns jumpers evenly surrounding the target point, if there is a obstacle in their spawn location, no spawn will take place 
     * @param size size of jumpers
     * @param vel speed of jumpers
     * @param amount how many jumpers
     * @param radius the radius of the spawning circle
     */
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
    
    /**
     * Setting up the attacks for player. If player has combos, then the combos change after every attack
     * Attackamount: the starting angle and attack arc of the attack
     * @param angle
     * @param delay
     */
    public void delayedAttack(double angle, int delay){
    	s.setAttacking(true);
		s.setChargingattack(false);
		int attackamount=(s.getMaxattackamount()*s.getAttackchargeframes()/s.getMaxattackchargeframes());	
		if(attackamount<s.getAttackinterval()*4){
			attackamount=s.getAttackinterval()*4;
		}
    	if(s.getAttackcombo()==1){
    		s.setAttackamount(attackamount);
    	}
    	else if(s.getAttackcombo()==2){
    		s.setAttackamount(-attackamount);
    	}
    	else if(s.getAttackcombo()==3){
    		s.setComboattacks(0);
    		s.setAttackamount(0);
    	}
    	s.setAttackangle(angle-(s.getAttackamount()/2));
    }
    
    /**
     * Method that calculates how much damage a AI shoudl take.
     * Ex. Stunned AI take double damage
     * @param basedamage
     * @param a
     * @return
     * @throws Exception 
     */
    public int calculateAttackdamage(int basedamage, AI a) throws Exception{
    	Random RN= new Random();
    	int damage= basedamage;
    	if(a.getStunframes()>0){
    		damage=damage*2;
    	}
    	if(s.isPhasewalkupgrade()&&s.isPhasewalking()){
    		damage=damage*2;
    	}
    	if(s.getAttackcombo()==3){
    		damage=damage*2;
    	}
    	if(a.getInvunerableframes()>0||dead==true){
    		damage=0;
    	}
    	if(damage>0){
        	sound.playeffect("Hit"+(RN.nextInt(2)+1)+"");
			hitAnimation(s, a.getMidx(), a.getMidy(), RN.nextInt(4)+1, 60,10,"BLACK");
    	}
    	return damage;
    }
    
    /**
     * Method that calulates the damage that the player should take based on their status
     * @param basedamage
     * @return
     * @throws Exception 
     */
    public int takeDamage(int basedamage) throws Exception{
    	Random RN= new Random();
    	int damage=basedamage;
    	if(!dead){
    		if(RN.nextInt(100)+1<s.getDodge()){
    			if(damage>5){
    			sound.playeffect("Block");
    			sound.playeffect("Crowdoohcheer");
    			addString("Dodged!",0,0,35);
    			}
    			damage=0;
    		}
    		else if(damage>5){
    			sound.playeffect("Damagetaken");
    		}
    		else if(damage>=70){
    			sound.playeffect("Crowdooh1");
    			s.setShakelimit(60);
    		}
    		else if(damage>=30){
    			s.setShakelimit(30);
    		}
    		else if(damage>5){
    			s.setShakelimit(5);
    		}
    	}
    	return damage;
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
	public void projectileExplosion(int x, int y, boolean hurtsenemy, boolean hurtsallies, int damage, int amount, int lifetime) throws Exception{
		int interval= 360/amount;
		if(!containsBoss()&&damage>1){
			sound.playeffect("Explosion");
		}
		for(int i=0;i<amount;i++){
			Projectile p= new Projectile(x,y,x,y,7,lifetime);
			p.setHurtsenemy(hurtsenemy);
			p.setHurtsallies(hurtsallies);
			p.changeAngle(interval*i);
			p.setDamage(damage);
			Projectiles.add(p);
		}
	}
	
	public void projectileExplosion(Projectile p, int amount){
		int interval= 360/amount;
		for(int i=0;i<amount;i++){
			Projectile po= p;
			po.changeAngle(interval*i);
			Projectiles.add(po);
		}
	}
	
	public void hitAnimation(Object o, int x, int y, int amount, int spread, int lifetime, String Color){
		Random RN= new Random();
		for(int i=0;i<amount;i++){
			int randdegree=(int)RN.nextInt(spread)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
			int randpointx=(int)RN.nextInt(5)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
			int randpointy=(int)RN.nextInt(5)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
			Projectile p= new Projectile(x+randpointx,y+randpointy,o.getMidx(),o.getMidy(),5,lifetime);
			p.setHurtsallies(false);
			p.setHurtsenemy(false);
			p.changeAngle(p.getAngle()+randdegree+180);
			p.setColor(Color);
			p.setBlood(true);
			Projectiles.add(p);
		}
	}
	public void hitAnimation2(Object o, int x, int y, int amount, int spread, int lifetime, String Color){
		Random RN= new Random();
		for(int i=0;i<amount;i++){
			int randdegree=(int)RN.nextInt(spread)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
			int randpointx=(int)RN.nextInt(5)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
			int randpointy=(int)RN.nextInt(5)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
			Projectile p= new Projectile(x+randpointx,y+randpointy,o.getMidx(),o.getMidy(),5,lifetime);
			p.setHurtsallies(false);
			p.setHurtsenemy(false);
			p.changeAngle(p.getAngle()+randdegree+180);
			p.setColor(Color);
			Projectiles.add(p);
		}
	}
	
	public void explodeAnimation(int x, int y, int amount, int lifetime) throws Exception{
		Hitbox h= new Hitbox(x,y,1,1,1);
		if(!containsBoss()){
			sound.playeffect("Explosion");
		}
		hitAnimation2(h,x,y,amount,360,lifetime,"RED_ORANGE");
	}
	public void explodeAnimation2(int x, int y, int amount, int lifetime) throws Exception{
		Hitbox h= new Hitbox(x,y,1,1,1);
		hitAnimation2(h,x,y,amount,360,lifetime,"RED_ORANGE");
	}
	
	public void burnAnimation(Object o){
		Random RN= new Random();
		int randpointx=(int)RN.nextInt((int)o.getRect().getWidth()/2)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
		int randpointy=(int)RN.nextInt((int)o.getRect().getHeight()/2)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
		Projectile p= new Projectile(o.getMidx()+randpointx,o.getMidy()+randpointy,o.getMidx()+randpointx,o.getMidy()+randpointy-10,5,5);
		p.setHurtsallies(false);
		p.setHurtsenemy(false);
		p.setColor("RED_ORANGE");
		Projectiles.add(p);
	}
	
	public void stunDashanimation(){
		Random RN= new Random();
		int randpointx=(int)RN.nextInt((int)s.getRect().getWidth()/2)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
		int randpointy=(int)RN.nextInt((int)s.getRect().getHeight()/2)*(int)(Math.pow(-1, (RN.nextInt(2)+1)));
		Projectile p= new Projectile(s.getMidx()+randpointx,s.getMidy()+randpointy,s.getMidx()+randpointx+(int)(Math.cos(Math.toRadians(angle+180))),s.getMidy()+randpointy+(int)(Math.sin(Math.toRadians(180+angle))),5,5);
		p.setHurtsallies(false);
		p.setHurtsenemy(false);
		if(RN.nextInt(3)==1){
			Projectiles.add(p);
		}
	}
	
	public void spawnCircle(int x, int y, int radius, boolean hurtsenemy, boolean hurtsallies, int lifetime,int damage){
		Circle c= new Circle(x,y,radius,hurtsenemy,hurtsallies,lifetime);
		c.setDamage(damage);
		Circles.add(c);
	}
	public void spawnCircle(Circle c){
		Circles.add(c);
	}
	public void spawnDelayedcircle(Circle c, int delay,int damage){
		c.setSpawndelay(delay);
		c.setDamage(damage);
		Circles.add(c);
	}
	public void spawnDelayedcircle(int x, int y, int radius, boolean hurtsenemy, boolean hurtsallies, int lifetime, int delay,int damage){
		Circle c= new Circle(x,y,radius,hurtsenemy,hurtsallies,lifetime);
		c.setSpawndelay(delay);
		c.setDamage(damage);
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
	
	public void circleStun(Circle c, int duration, int damage, boolean hurtsenemies, boolean hurtsallies) throws Exception{
		if(hurtsallies){
		if(c.intersects(s)&&s.isInvunerable()==false&&s.getInvunerableframes()<=0){
			s.stop();
			s.setHealth(s.getHealth()-takeDamage(damage));
			s.setStunframes(duration);
		}
		}
		if(hurtsenemies){
		for(int i=0;i<AIs.size();i++){
			if(c.intersects(AIs.get(i))){
				AIs.get(i).setHealth(AIs.get(i).getHealth()-calculateAttackdamage(damage, AIs.get(i)));
				AIs.get(i).setStunframes(duration);
				AIs.get(i).setInvunerableframes(5);
			}
		}
		}
	}
	
	public void circleBurn(Circle c, int frames) throws Exception{
		for(int i=0;i<AIs.size();i++){
			if(c.intersects(AIs.get(i))){
				AIs.get(i).setDotframes(frames);
				AIs.get(i).setDotdamage(1);
			}
		}
		//Hitbox h= new Hitbox(c.getX(),c.getY(),1,1,1);
		//hitAnimation(h,c.getX(),c.getY(),30,360,20);
		explodeAnimation(c.getX(),c.getY(),15,20);	
	}
	
	public boolean containsBoss(){
		for(int i=0;i<AIs.size();i++){
			if(AIs.get(i).getClass()==Boss.class){
				if(((Boss)(AIs.get(i))).isPhase2()){
					return true;
				}
			}
		}
		return false;
	}
	
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d= (Graphics2D)g; 
        g.setColor(Color.BLACK);
        try{
        	//Square
        	//g2d.rotate(40,camxoff,camyoff);
        	//g2d.translate(-cam.getX()*scale+s.getCamdisplacex()*scale+s.getShakex(), -cam.getY()*scale+s.getCamdisplacey()*scale+s.getShakey());//begins cam
        	g2d.translate((int)frame.getBounds().getWidth()/2-((cam.getX()+camxoff+60)*scale)+s.getCamdisplacex()*scale+s.getShakex(), (int)frame.getBounds().getHeight()/2-((cam.getY()+camyoff+30)*scale)+s.getCamdisplacey()*scale+s.getShakey());//begins cam
        	g2d.scale(scale, scale);
        	//Player
        	if(s.isCanfreeze()==true){
        		if(s.getCurrfreezecooldown()<=0){
        			Color c= new Color(1,1,100);
            		g.setColor(c); 
        			g.drawString("F", s.getX()-8, s.getY()+10);
        		}
        	}
        	if(s.isPhasewalkupgrade()==true){
        		if(s.getCurrphasewalkcooldown()<=0){
        			g.setColor(Color.RED);
        			g.drawString("P", s.getX()-8, s.getY()+20);
        		}
        	}
        	g2d.setColor(Color.BLACK);
        	if(s.isComboupgrade()==true){
        		g2d.drawString(s.getAttackcombo()+"", s.getX()-8, s.getY()+30);
        	}     	
        	if(s.getDashing()&&s.isStundashupgrade()==true){
        		g2d.drawArc(s.getX()-s.getWidth()/4, s.getY()-s.getHeight()/4, (s.getWidth()*3)/2, (s.getWidth()*3)/2, 30, 30);
        		g2d.drawArc(s.getX()-s.getWidth()/4, s.getY()-s.getHeight()/4, (s.getWidth()*3)/2, (s.getWidth()*3)/2, 120, 30);
        		g2d.drawArc(s.getX()-s.getWidth()/4, s.getY()-s.getHeight()/4, (s.getWidth()*3)/2, (s.getWidth()*3)/2, 210, 30);
        		g2d.drawArc(s.getX()-s.getWidth()/4, s.getY()-s.getHeight()/4, (s.getWidth()*3)/2, (s.getWidth()*3)/2, 300, 30); 	
        	}
        	if(!character){
        		g2d.fillRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        	}
        	if(s.getHealth()>0){
            	if(s.getDisplayhealth()-s.getHealth()>5){
            		s.setDisplayhealth(s.getDisplayhealth()-2);
            	}
            	else if(s.getDisplayhealth()-s.getHealth()<-5){
            		s.setDisplayhealth(s.getDisplayhealth()+2);
            	}
            	else{
            		s.setDisplayhealth(s.getHealth());
            	}
        		int length=(s.getWidth()*(100*s.getDisplayhealth())/(100*s.getMaxhealth()))-1;
        		if(length<3){length = 3;}
        		g2d.drawRect(s.getX(), s.getY()-7, length, 5);
        	}
        	if(s.getAttackchargeframes()>0&&s.isChargingattack()){
        		g2d.drawRect(s.getX()-3, s.getY()+s.getHeight()-(s.getHeight()*s.getAttackchargeframes()/s.getMaxattackchargeframes()), 3, (s.getHeight()*s.getAttackchargeframes()/s.getMaxattackchargeframes())-2);
        	}
        	if(s.getSpeedboostframes()>0){
        		g2d.drawRect(s.getX()+(int)s.getRect().getWidth(), s.getY(), 5,(s.getHeight()*(s.getSpeedboostframes())/(s.getSpeedboostduration()))-1);
        	}
        	if(s.getCurrdashcooldown()>0){//Dash Cooldown bar
        		g2d.drawRect(s.getX(), s.getY()+s.getHeight()+1, (s.getWidth()*s.getCurrdashcooldown()/s.getDashcooldown()), 5);
        	}
        	if(s.getParryframes()>0){//Parry Cooldown bar
        		g2d.drawRect(s.getX()+(int)s.getRect().getWidth()+5, s.getY(), 5,(s.getHeight()*(s.getParryframes())/(s.getMaxparry()))-1);
        	}
        	if(s.getParryframes()==0&&s.getCurrparrycooldown()>0){//Parry Cooldown bar
        		g2d.drawRect(s.getX(), s.getY()+s.getHeight()+6, (s.getWidth()*s.getCurrparrycooldown()/s.getParrycooldown()), 5);
        	}
        	//g2d.drawString(s.getCurrdashcooldown()+"",(int)s.getX(),(int)s.getY());
        	if(s.getSpeedboostframes()>0){
        		g2d.setColor(Color.DARK_GRAY);
        	}
        	if(s.getDashing()==true){      		
        		if(s.isStundashupgrade()){
        			stunDashanimation();
        		}
        		g2d.setColor(new Color(125,7,0));
        	}
        	if(s.isChargingattack()&&s.getTotalattackchargeframes()>10){
        		if(s.getAttackchargeframes()>=s.getMaxattackchargeframes()){
        			g.setColor(Color.RED);
        		}
        		int angle=(int)s.getAngle();
        		int r=130+(40*s.getAttackchargeframes()/s.getMaxattackchargeframes());
        		int offset=(s.getMaxattackamount()/2*s.getAttackchargeframes()/s.getMaxattackchargeframes());
        		if(angle<0){
        			angle+=360;
        		}
        		if(s.getAttackcombo()<3){
        			//g.drawLine(s.getMidx(), s.getMidy(), s.getMidx()+(int)(r/2*Math.cos(Math.toRadians(angle-offset+5))), s.getMidy()+(int)((r/2*Math.sin(Math.toRadians(angle-offset+5)))));
        			//g.drawLine(s.getMidx(), s.getMidy(), s.getMidx()+(int)(r/2*Math.cos(Math.toRadians(angle+offset-5))), s.getMidy()+(int)((r/2*Math.sin(Math.toRadians(angle+offset-5)))));
        			angle-=360;
        			if(angle>-360&&angle<-180){
        				angle+=360;
        			}
        			if(angle>=-180){
        				angle*=-1;
        			}
        			//g.drawString(angle+"", s.getX(), s.getY()-20);
        			int startangle=angle-offset;
        			int arc=(s.getMaxattackamount()*s.getAttackchargeframes()/s.getMaxattackchargeframes());
        			//g.drawArc(s.getMidx()-r/2,s.getMidy()-r/2,r,r,startangle,arc);//Startangle on the right side
        			//g.drawArc(s.getMidx()-r/4,s.getMidy()-r/4,r/2,r/2,startangle,arc);//Startangle on the right side
        			s.setAttackarc(arc);
        			s.setLastattackangle(startangle);
        			s.setLastattackradius(r);
        		}
        		else if(s.getAttackcombo()>=3){
        			g.drawLine(s.getMidx()+(int)(r/6*Math.cos(Math.toRadians(angle-40))), s.getMidy()+(int)(r/6*Math.sin(Math.toRadians(angle-40))), s.getMidx()+(int)((r-25)*Math.cos(Math.toRadians(angle-7))), s.getMidy()+(int)(((r-25)*Math.sin(Math.toRadians(angle-7)))));
        			g.drawLine(s.getMidx()+(int)(r/6*Math.cos(Math.toRadians(angle+40))), s.getMidy()+(int)(r/6*Math.sin(Math.toRadians(angle+40))), s.getMidx()+(int)((r-25)*Math.cos(Math.toRadians(angle+7))), s.getMidy()+(int)(((r-25)*Math.sin(Math.toRadians(angle+7)))));
        		}
        		g.setColor(Color.BLACK);
        	}
        	if(s.isAttacking()){
        		//int arc=s.getAttackarc()-(s.getAttackarc()*s.getAttackamount()/s.getMaxattackamount());
        		int angle=(int)s.getAngle();
        		//g.drawArc(s.getMidx()-s.getLastattackradius()/2,s.getMidy()-s.getLastattackradius()/2,s.getLastattackradius(),s.getLastattackradius(),s.getLastattackangle(),arc);//Startangle on the right side
        		if(angle<0){
        			angle+=360;
        		}
    			//System.out.println(angle);
        		//g.drawLine(s.getMidx(), s.getMidy(), s.getMidx()+(int)(s.getLastattackradius()/2*Math.cos(Math.toRadians(angle+s.getAttackarc()/2-arc))), s.getMidy()+(int)((s.getLastattackradius()/2*Math.sin(Math.toRadians(angle+s.getAttackarc()/2-arc)))));
    			//g.drawLine(s.getMidx(), s.getMidy(), s.getMidx()+(int)(s.getLastattackradius()/2*Math.cos(Math.toRadians(angle+s.getAttackarc()/2))), s.getMidy()+(int)((s.getLastattackradius()/2*Math.sin(Math.toRadians(angle+s.getAttackarc()/2)))));
        	}
        	if(s.getInvunerableframes()>0&&s.getFreezeframes()<=0){//Animation for damage invulnerbility 
        		if(s.isPhasewalkupgrade()){
        			g2d.setColor(new Color(125,7,0));
        		}
        		else{
        			if(s.getInvunerableframes()%2==0){
        				g2d.setColor(Color.RED);
        			}
        			else{
        				g2d.setColor(Color.BLACK);
        			}
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
        		burnAnimation(s);
        		g2d.drawString("Burning!", s.getX(), s.getY()-10);     		    		
        	}
        	if(deathbound==true&&dead==false){
        		g2d.setColor(Color.BLACK); 
        		g2d.drawString("Deathbound", s.getX()-15, s.getY()+s.getHeight()+10);     		    		
        	}
        	if(s.getParryframes()>0){
        		g.drawArc(getHandler().getMouseCoords()[2]-10, getHandler().getMouseCoords()[3]-10, 10*2, 10*2, 0, 360);
        		getHandler().clickangle=(int)getSquare().findAngle(getHandler().getMouseCoords()[3]-s.getMidy(),getHandler().getMouseCoords()[2]-s.getMidx());
        		int angle=getHandler().clickangle;
        		g.drawLine(s.getMidx()+(int)(30*Math.cos(Math.toRadians(angle-10))), s.getMidy()+(int)(30*Math.sin(Math.toRadians(angle-10))), s.getMidx()+(int)(55*Math.cos(Math.toRadians(angle))), s.getMidy()+(int)(55*Math.sin(Math.toRadians(angle))));
				g.drawLine(s.getMidx()+(int)(30*Math.cos(Math.toRadians(angle+10))), s.getMidy()+(int)(30*Math.sin(Math.toRadians(angle+10))), s.getMidx()+(int)(55*Math.cos(Math.toRadians(angle))), s.getMidy()+(int)(55*Math.sin(Math.toRadians(angle))));
				g.drawLine(s.getMidx()+(int)(30*Math.cos(Math.toRadians(angle+10))), s.getMidy()+(int)(30*Math.sin(Math.toRadians(angle+10))), s.getMidx()+(int)(30*Math.cos(Math.toRadians(angle-10))), s.getMidy()+(int)(30*Math.sin(Math.toRadians(angle-10))));
        		g2d.setColor(new Color(60,94,7));     		
        	}
        	if(!character){
        		g2d.fillRect(s.getX(),s.getY(),s.getWidth(),s.getHeight());
        	}
        	else{
        		BufferedImage image1 = null;
        		try{
        			String image="CharacterDown";
        			if(s.getVely()>=0&&Math.abs(s.getVelx())<3){
        				image="CharacterDown";
        			}
        			else if(s.getVely()<0&&Math.abs(s.getVelx())<3){
        				image="CharacterUp";
        			}
        			else if(s.getVelx()>=0){
        				image="CharacterRight";
        			}else{
        				image="CharacterLeft";
        			}
        			image1 = ImageIO.read(new File("C:\\Users\\Kenny\\stuff\\GameCol\\Image\\"+image+".png"));
        			image1.createGraphics();
        		}
        		catch(Exception e){}
        		g2d.drawImage(image1,s.getX(),s.getY(),null);
        	}
    		g2d.setColor(Color.BLACK);
        	for(int i=0;i<s.getShotcharges();i++){
        		c++;
        		g.setColor(Color.RED);
        		g.drawString("l", s.getMidx()+(3*i)-4, s.getMidy()+4);
        		//g2d.drawRect(s.getX()+i*(s.getWidth()/3), s.getY()+s.getHeight(), -1+s.getWidth()/3, 5);
        		g.setColor(Color.BLACK);
        	}
        	if(inbreak==true&&AIs.size()<=0&&win==false){
        		g.drawString("Press O to continue", s.getX()+s.getWidth(), s.getY());
        		g.drawString("Skillpoints: "+s.getSkillpoints(), s.getX()+s.getWidth(), s.getY()+10);
        		if(page1==true&&win==false){
        			g.drawString("(1) +2 Projectiles per shot Curr["+s.getProjectileshots()+"]", s.getX()+s.getWidth(), s.getY()+20);
        			g.drawString("(2) +1 Speed & 20% dodge Curr["+s.getMaxspeed()+"]", s.getX()+s.getWidth(), s.getY()+30);
        			g.drawString("(3) +50 Max Health Curr["+s.getMaxhealth()+"]", s.getX()+s.getWidth(), s.getY()+40);
        			g.drawString("(4) +10-30 Attack Damage Curr["+s.getAttackdamage()+"]", s.getX()+s.getWidth(), s.getY()+50);
        			g.drawString("(5) -.2 Dash Cooldown Curr["+(double)s.getDashcooldown()/60+"]", s.getX()+s.getWidth(), s.getY()+60);
        			g.drawString("(6) +4 Health on Hit Curr["+s.getHealthonhit()+"]", s.getX()+s.getWidth(), s.getY()+70);
        			g.drawString("(0) Ascension Page", s.getX()+s.getWidth(), s.getY()+80);
        		}
        		else{
        			if(s.isFireupgrade()==false){g.drawString("(1) Projectiles inflict 40 burn damage[2 SP]", s.getX()+s.getWidth(), s.getY()+20);}
        			else{g.drawString("(X) Fire upgrade obtained", s.getX()+s.getWidth(), s.getY()+20);}
        			if(s.isCanfreeze()==false){g.drawString("(2) Freeze Time on Parry(3 seconds; 13 sec cooldown)[2 SP]", s.getX()+s.getWidth(), s.getY()+30);}
        			else{g.drawString("(X) Freeze upgrade obtained", s.getX()+s.getWidth(), s.getY()+30);}
        			if(s.isPhasewalkupgrade()==false){g.drawString("(3) Phasewalk after dash;x2 damage during Phase (3 seconds; 10 sec cooldown)[2 SP]", s.getX()+s.getWidth(), s.getY()+40);}
        			else{g.drawString("(X) Phasewalk obtained", s.getX()+s.getWidth(), s.getY()+40);}
        			if(s.isComboupgrade()==false){g.drawString("(4) Weapon Arts; x2 damage on third attack[2 SP]", s.getX()+s.getWidth(), s.getY()+50);}
        			else{g.drawString("(X) Weapon Art obtained", s.getX()+s.getWidth(), s.getY()+50);}
        			if(s.isStundashupgrade()==false){g.drawString("(5) Flicker; Stuns and damages enemies when hit by dash; 1/4th dash CD on hit[2 SP]", s.getX()+s.getWidth(), s.getY()+60);}
        			else{g.drawString("(X) Flicker obtained", s.getX()+s.getWidth(), s.getY()+60);}
        			if(s.isSpinupgrade()==false){g.drawString("(6) Spin on parry (2 second cooldown) [2 SP]", s.getX()+s.getWidth(), s.getY()+70);}
        			else{g.drawString("(X) Spin obtained", s.getX()+s.getWidth(), s.getY()+70);}
        			g.drawString("(0) Back", s.getX()+s.getWidth(), s.getY()+80);
        		}
        	}
        	if(dead==true){
        		g2d.setColor(new Color(125,7,0));
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
            for(int i=0;i<blocks.size();i++){
            	c++;
            	Block b= blocks.get(i);
            	calculateBlockoffsets(b);
            	g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
            	
            	g.drawLine(b.getX(), b.getY(), b.getX()+xoff, b.getY()+yoff);//top-left
            	g.drawLine(b.getX()+b.getWidth(), b.getY(), b.getX()+b.getWidth()+xoff, b.getY()+yoff);//top-right
            	g.drawLine(b.getX(), b.getY()+b.getHeight(), b.getX()+xoff, b.getY()+b.getHeight()+yoff);//bottom-left
            	g.drawLine(b.getX()+b.getWidth(), b.getY()+b.getHeight(), b.getX()+b.getWidth()+xoff, b.getY()+b.getHeight()+yoff);//bottom-right
            	
            	g.drawRect(b.getX()+xoff, b.getY()+yoff, b.getWidth(), b.getHeight());
            }
            for(int i=0;i<blocks.size();i++){
            	c++;
            	Block b= blocks.get(i);
            	calculateBlockoffsets(b);
            	if(s.getFreezeframes()>0){
            		Color c= new Color(8,47,84);
            		g.setColor(c);
            	}
            	else{
            		Color c;
            		if(s.getHealth()>0){
            			c= new Color(50,(50*s.getHealth()/s.getMaxhealth())+16,0);
            		}
            		else if(deathtime<endtime){
            			c= new Color(50,36-(36*deathtime/endtime),0);
            		}
            		else{
            			c= new Color(0,0,0);
            		}
            		g.setColor(c);
            	}
            	g.fillRect(b.getX()+xoff, b.getY()+yoff, b.getWidth(), b.getHeight());
            	g.drawLine(b.getX()+xoff, b.getY()+yoff, b.getX()+xoff+b.getWidth(), b.getY()+yoff);//top
            	g.drawLine(b.getX()+xoff, b.getY()+yoff+1, b.getX()+xoff+b.getWidth(), b.getY()+yoff+1);//top
            	g.drawLine(b.getX()+xoff, b.getY()+yoff-1, b.getX()+xoff+b.getWidth(), b.getY()+yoff-1);//top
            	g.drawLine(b.getX()+xoff+b.getWidth(), b.getY()+yoff, b.getX()+xoff+b.getWidth(), b.getY()+yoff+b.getHeight());//right
            	g.drawLine(b.getX()+xoff+b.getWidth()+1, b.getY()+yoff, b.getX()+xoff+b.getWidth()+1, b.getY()+yoff+b.getHeight());//right
            	g.drawLine(b.getX()+xoff+b.getWidth()-1, b.getY()+yoff, b.getX()+xoff+b.getWidth()-1, b.getY()+yoff+b.getHeight());//right
            	g.drawLine(b.getX()+xoff+b.getWidth(), b.getY()+yoff+b.getHeight(), b.getX()+xoff, b.getY()+yoff+b.getHeight());//bottom
            	g.drawLine(b.getX()+xoff+b.getWidth(), b.getY()+yoff+b.getHeight()+1, b.getX()+xoff, b.getY()+yoff+b.getHeight()+1);//bottom
            	g.drawLine(b.getX()+xoff+b.getWidth(), b.getY()+yoff+b.getHeight()-1, b.getX()+xoff, b.getY()+yoff+b.getHeight()-1);//bottom
            	g.drawLine(b.getX()+xoff, b.getY()+yoff, b.getX()+xoff, b.getY()+yoff+b.getHeight());//left
            	g.drawLine(b.getX()+xoff+1, b.getY()+yoff, b.getX()+xoff+1, b.getY()+yoff+b.getHeight());//left
            	g.drawLine(b.getX()+xoff-1, b.getY()+yoff, b.getX()+xoff-1, b.getY()+yoff+b.getHeight());//left
            	g2d.setColor(Color.BLACK);
            	g.drawLine(b.getX()+xoff, b.getY()+yoff-1, b.getX()+xoff+b.getWidth(), b.getY()+yoff-1);//top
            	g.drawLine(b.getX()+xoff+b.getWidth()+1, b.getY()+yoff, b.getX()+xoff+b.getWidth()+1, b.getY()+yoff+b.getHeight());//right
            	g.drawLine(b.getX()+xoff+b.getWidth(), b.getY()+yoff+b.getHeight()+1, b.getX()+xoff, b.getY()+yoff+b.getHeight()+1);//bottom
              	g.drawLine(b.getX()+xoff-1, b.getY()+yoff, b.getX()+xoff-1, b.getY()+yoff+b.getHeight());//left
            }
            
            //AIs
            for(int i=0;i<AIs.size();i++){
            	c++;
            	AI a= AIs.get(i);
            	if(a.getClass()==Jumper.class){
            		g.setColor(Color.GRAY);
            	}
            	if(a.getClass()==Pusher.class){
            		g.setColor(new Color(143,15,7));
            	}
            	if(a.getClass()==Sprayer.class){
            		Color c= new Color(200,100,20);
            		g.setColor(c);
            	}
            	if(a.getClass()==Teleporter.class){
            		Color c= new Color(20,20,200);
            		g.setColor(c);
            	}
            	if(a.getClass()==Healer.class){
            		Color c= new Color(1,100,1);
            		g.setColor(c);
            	}
            	if(a.getClass()==Dodger.class){
            		Color c= new Color(102,0,102);
            		g.setColor(c);
            	}
            	if(a.getInvunerableframes()>0&&s.getFreezeframes()<=0){//Animation for damage invulnerbility 
            		if(a.getInvunerableframes()%2==0){
            			g2d.setColor(new Color(125,7,0));
            		}
            		else{
            			g.setColor(Color.BLACK);
            		}

            	}
            	g.fillRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());
            	g.setColor(Color.BLACK);//Jumper default color is black
            	if(AIs.get(i).getClass()==Jumper.class){
            		if(((Jumper)a).jumping==true){
            			g.setColor(Color.WHITE);
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Jumper)a).getCooldown().time>((Jumper)a).getShootcooldown()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				g.setColor(Color.RED);
            				g.drawLine(AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()-10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()-10))), AIs.get(i).getMidx()+(int)(55*Math.cos(Math.toRadians(AIs.get(i).getAngle()))), AIs.get(i).getMidy()+(int)(55*Math.sin(Math.toRadians(AIs.get(i).getAngle()))));
            				g.drawLine(AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidx()+(int)(55*Math.cos(Math.toRadians(AIs.get(i).getAngle()))), AIs.get(i).getMidy()+(int)(55*Math.sin(Math.toRadians(AIs.get(i).getAngle()))));
            				g.drawLine(AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()-10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()-10))));
            				if(((Jumper)AIs.get(i)).getCooldown().getTimer()>((Jumper)AIs.get(i)).getShootcooldown()){         					
                				g.setColor(Color.WHITE);
                				if(((Jumper)a).getCooldown().time>(((Jumper)a).getShootcooldown()*2)/3){
                    				g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));// Longest equation I ever made. Lord Almightly 				
                    				//g.drawLine(a.getMidx(), a.getMidy(), a.getMidx()+(int)(Math.cos(Math.toRadians(((Jumper)a).getAngle()))*300)-(int)((Math.cos(Math.toRadians(((Jumper)a).getAngle()))*300)*((Jumper)a).jumptime.time/30), a.getMidy()+(int)(Math.sin(Math.toRadians(((Jumper)a).getAngle()))*300)-(int)((Math.sin(Math.toRadians(((Jumper)a).getAngle()))*300)*((Jumper)a).jumptime.time/30));
                    			}
            				}
            			}
            			g.setColor(Color.BLACK);
            		}
            	}
            	g.setColor(Color.WHITE);
            	if(a.getClass()==Pusher.class){
            		if(((Pusher)a).getCooldown().time>0){
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
        				if(((Pusher)a).getCooldown().time>((Pusher)a).getShootcooldown()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				if(((Pusher)a).getCooldown().time>(((Pusher)a).getShootcooldown()*2)/3){
            					g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
                    			g.setColor(Color.BLACK);
                    			g.drawLine(AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()-10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()-10))), AIs.get(i).getMidx()+(int)(55*Math.cos(Math.toRadians(AIs.get(i).getAngle()))), AIs.get(i).getMidy()+(int)(55*Math.sin(Math.toRadians(AIs.get(i).getAngle()))));
                				g.drawLine(AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidx()+(int)(55*Math.cos(Math.toRadians(AIs.get(i).getAngle()))), AIs.get(i).getMidy()+(int)(55*Math.sin(Math.toRadians(AIs.get(i).getAngle()))));
                				g.drawLine(AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()-10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()-10))));
                				g.setColor(Color.WHITE);
            				}
            			}
            		}
            		//g.drawString(((Pusher)a).getCooldown().time+"",a.getX(),a.getY());
            	}
            	else if(a.getClass()==Sprayer.class){
            		if(((Sprayer)a).getCooldown().time>0){
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Sprayer)a).getCooldown().time>((Sprayer)a).getShootcooldown()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				if(((Sprayer)a).getCooldown().time>(((Sprayer)a).getShootcooldown()*2)/3){
            					g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
            				}
            			}
            		}
            	}
            	else if(a.getClass()==Teleporter.class){
            		if(((Teleporter)a).getCooldown().time-((Teleporter)a).getCastduration()>0){
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Teleporter)a).getCooldown().time-((Teleporter)a).getCastduration()>((Teleporter)a).getShootcooldown()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				if(((Teleporter)a).getCooldown().time-((Teleporter)a).getCastduration()>(((Teleporter)a).getShootcooldown()*2)/3){
            					g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
            				}
            			}
            		}
            	}
            	else if(a.getClass()==Healer.class){
            		if(((Healer)a).getCooldown().time>((Healer)a).getShootcooldown()/2){
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-1, (int)a.getY()+(a.getSize()/2)+2);
            			g.setColor(Color.BLACK);
        				g.drawLine(AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()-10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()-10))), AIs.get(i).getMidx()+(int)(55*Math.cos(Math.toRadians(AIs.get(i).getAngle()))), AIs.get(i).getMidy()+(int)(55*Math.sin(Math.toRadians(AIs.get(i).getAngle()))));
        				g.drawLine(AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidx()+(int)(55*Math.cos(Math.toRadians(AIs.get(i).getAngle()))), AIs.get(i).getMidy()+(int)(55*Math.sin(Math.toRadians(AIs.get(i).getAngle()))));
        				g.drawLine(AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()+10))), AIs.get(i).getMidx()+(int)(30*Math.cos(Math.toRadians(AIs.get(i).getAngle()-10))), AIs.get(i).getMidy()+(int)(30*Math.sin(Math.toRadians(AIs.get(i).getAngle()-10))));
            		}
            	}
            	else if(a.getClass()==Dodger.class){
            		if(((Dodger)a).getCooldown().time>0){
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Dodger)a).getCooldown().time>((Dodger)a).getShootcooldown()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				if(((Dodger)a).getCooldown().time>(((Dodger)a).getShootcooldown()*2)/3){
            					g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
            				}
            			}
            		}
            	}
            	else if(a.getClass()==Boss.class){
            		AI b= AIs.get(i);
            		if(((Boss)a).getCooldown().time>0){
            			g.drawString("!",(int)a.getX()+(a.getSize()/2)-2, (int)a.getY()+(a.getSize()/2));
            			if(((Boss)a).getCooldown().time>((Boss)a).getShootcooldown()/3){
            				g.drawString("!",(int)a.getX()+(a.getSize()/2)+2, (int)a.getY()+(a.getSize()/2));
            				g.drawLine(b.getMidx(), b.getY(), b.getX()+(int)b.getRect().getWidth(),b.getMidy());
            				g.drawLine(b.getX()+(int)b.getRect().getWidth(),b.getMidy(), b.getMidx(), b.getY()+(int)b.getRect().getHeight());
            				g.drawLine(b.getMidx(), b.getY()+(int)b.getRect().getHeight(), b.getX(), b.getMidy());
            				g.drawLine(b.getX(), b.getMidy(), b.getMidx(), b.getY());
            				if(((Boss)a).getCooldown().time>(((Boss)a).getShootcooldown()*2)/3){
            					g.drawString("!",(int)a.getX()+(a.getSize()/2)+6, (int)a.getY()+(a.getSize()/2));
            					g.drawRect(b.getX()+13, b.getY()+13, 24, 24);
            				}
            			}
            		}
            	}
            	g.setColor(Color.BLACK);
            	if(a.getDotframes()>0){
            		Color c = new Color(255,69,0); g2d.setColor(c);
            		if(a.getDotframes()%2==0){burnAnimation(AIs.get(i));}
            		g2d.drawString("Burning!", a.getX(), a.getY()-10);  
            		g.fillRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());
            	}
            	if(a.getStunframes()>0){ 
            		int dis=40;
            		int length=57;
            		int angle=a.getStunframes()*2;
            		int startangle=0;
            		g.setColor(Color.RED);
            		g.drawLine(a.getMidx()+(int)(dis*Math.cos(Math.toRadians(startangle))), a.getMidy()+(int)(dis*Math.sin(Math.toRadians(startangle))), a.getMidx()+(int)(dis*Math.cos(Math.toRadians(startangle)))+(int)(calculateDis(length,angle,90)*Math.cos(Math.toRadians(135))), a.getMidy()+(int)(dis*Math.sin(Math.toRadians(startangle)))+(int)(calculateDis(length,angle,90)*Math.sin(Math.toRadians(135))));
            		g.drawLine(a.getMidx()+(int)(dis*Math.cos(Math.toRadians(startangle+90))), a.getMidy()+(int)(dis*Math.sin(Math.toRadians(startangle+90))), a.getMidx()+(int)(dis*Math.cos(Math.toRadians(startangle+90)))+(int)(calculateDis(length,angle,180)*Math.cos(Math.toRadians(225))), a.getMidy()+(int)(dis*Math.sin(Math.toRadians(startangle+90)))+(int)(calculateDis(length,angle,180)*Math.sin(Math.toRadians(225))));
            		g.drawLine(a.getMidx()+(int)(dis*Math.cos(Math.toRadians(startangle+180))), a.getMidy()+(int)(dis*Math.sin(Math.toRadians(startangle+180))), a.getMidx()+(int)(dis*Math.cos(Math.toRadians(startangle+180)))+(int)(calculateDis(length,angle,270)*Math.cos(Math.toRadians(315))), a.getMidy()+(int)(dis*Math.sin(Math.toRadians(startangle+180)))+(int)(calculateDis(length,angle,270)*Math.sin(Math.toRadians(315))));
            		g.drawLine(a.getMidx()+(int)(dis*Math.cos(Math.toRadians(startangle+270))), a.getMidy()+(int)(dis*Math.sin(Math.toRadians(startangle+270))), a.getMidx()+(int)(dis*Math.cos(Math.toRadians(startangle+270)))+(int)(calculateDis(length,angle,360)*Math.cos(Math.toRadians(45))), a.getMidy()+(int)(dis*Math.sin(Math.toRadians(startangle+270)))+(int)(calculateDis(length,angle,360)*Math.sin(Math.toRadians(45))));
            		length=40;
            		angle=angle+70;
            		g.drawLine(a.getMidx()+(int)(calculateDis(length,angle,90)*Math.cos(Math.toRadians(startangle))), a.getMidy()+(int)(calculateDis(length,angle,90)*Math.sin(Math.toRadians(startangle))), a.getMidx(), a.getMidy());
            		g.drawLine(a.getMidx()+(int)(calculateDis(length,angle,180)*Math.cos(Math.toRadians(startangle+90))), a.getMidy()+(int)(calculateDis(length,angle,180)*Math.sin(Math.toRadians(startangle+90))), a.getMidx(), a.getMidy());
            		g.drawLine(a.getMidx()+(int)(calculateDis(length,angle,270)*Math.cos(Math.toRadians(startangle+180))), a.getMidy()+(int)(calculateDis(length,angle,270)*Math.sin(Math.toRadians(startangle+180))), a.getMidx(), a.getMidy());
            		g.drawLine(a.getMidx()+(int)(calculateDis(length,angle,360)*Math.cos(Math.toRadians(startangle+270))), a.getMidy()+(int)(calculateDis(length,angle,360)*Math.sin(Math.toRadians(startangle+270))), a.getMidx(), a.getMidy());
            		Color c= new Color(100,200,1);
            		g.setColor(c);
            		//g.drawString("Stunned!",(int)a.getX()-4, (int)a.getY()+(int)a.getRect().getHeight()+10);  
            		g.fillRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());
            	}
            	g.setColor(Color.BLACK);//must be above freeze because freeze makes everything blue!
            	if(s.getFreezeframes()>0){
            		Color c= new Color(1,1,100);
            		g.setColor(c);    
            		g.drawString("Frozen!",(int)a.getX(), (int)a.getY()-10);
            		g.fillRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());
            	}
            	//g.drawRect((int)a.getX(), (int)a.getY(), (int)a.getSize(), (int)a.getSize());
            	//g.setColor(Color.BLACK);
            	if(a.getHealth()>0){//Enemy Hp bars
            		g2d.drawRect(a.getX(), a.getY()-7, (int)(a.getRect().getWidth()*a.getHealth()/a.getMaxhealth()), 5);
            	}
            	if(debug){          		
            		g.drawString(s.distance((int)a.getX(), (int)a.getY(),(int)s.getX(),(int)s.getY())+"",(int)a.getX(), (int)a.getY());  
            	}
            } 
            //Projectiles
            for(int i=0;i<Projectiles.size();i++){
            	c++;
            	Hitbox p=Projectiles.get(i);
            	if(p.getColor().equals("BLACK")){
            		g2d.setColor(Color.BLACK);
            	}
            	else if(p.getColor().equals("RED")){g2d.setColor(Color.RED);}
            	else if(p.getColor().equals("GREEN")){g2d.setColor(Color.GREEN);}
            	else if(p.getColor().equals("RED_ORANGE")){Color c = new Color(255,69,0); g2d.setColor(c);}
            	else if(p.getColor().equals("ORANGE")){g2d.setColor(Color.ORANGE);}
            	else if(p.getColor().equals("BLUE")){g2d.setColor(Color.BLUE);}
            	else if(p.getColor().equals("DARK_GREEN")){Color c = new Color(1,100,1); g2d.setColor(c);}
            	else if(p.getColor().equals("PURPLE")){Color c = new Color(102,0,102); g2d.setColor(c);}
            	else if(p.getColor().equals("WHITE")){g2d.setColor(Color.WHITE);}
            	//g.drawRect((int)p.getRect().getX(),(int)p.getRect().getY(),(int)p.getRect().getWidth(),(int)p.getRect().getHeight());
            	if(p.getClass()==Projectile.class&&((Projectile)p).hurtsEnemy()){
            		int dis=5;
            		int offset=100;
            		g.drawLine(p.getMidx()+(int)(dis*Math.cos(Math.toRadians(((Projectile)p).getAngle()-offset))), p.getMidy()+(int)(dis*Math.sin(Math.toRadians(((Projectile)p).getAngle()-offset))), p.getMidx()+(int)(25*Math.cos(Math.toRadians(((Projectile)p).getAngle()))), p.getMidy()+(int)(25*Math.sin(Math.toRadians(((Projectile)p).getAngle()))));
            		g.drawLine(p.getMidx()+(int)(dis*Math.cos(Math.toRadians(((Projectile)p).getAngle()+offset))), p.getMidy()+(int)(dis*Math.sin(Math.toRadians(((Projectile)p).getAngle()+offset))), p.getMidx()+(int)(25*Math.cos(Math.toRadians(((Projectile)p).getAngle()))), p.getMidy()+(int)(25*Math.sin(Math.toRadians(((Projectile)p).getAngle()))));
            		g.drawLine(p.getMidx()+(int)(dis*Math.cos(Math.toRadians(((Projectile)p).getAngle()+offset))), p.getMidy()+(int)(dis*Math.sin(Math.toRadians(((Projectile)p).getAngle()+offset))), p.getMidx()+(int)(dis*Math.cos(Math.toRadians(((Projectile)p).getAngle()-offset))), p.getMidy()+(int)(dis*Math.sin(Math.toRadians(((Projectile)p).getAngle()-offset))));
            	}
            	else{
            		g.drawRect((int)p.getRect().getX(),(int)p.getRect().getY(),(int)p.getRect().getWidth(),(int)p.getRect().getHeight());
            	}
            }
            g2d.setColor(Color.BLACK);
            //Circles
            for(int i=0;i<Circles.size();i++){
            	Circle c= Circles.get(i);
            	if(c.getColor().equals("RED")){g2d.setColor(Color.RED);}
            	else if(c.getColor().equals("GREEN")){g2d.setColor(Color.GREEN);}
            	else if(c.getColor().equals("RED_ORANGE")){Color co = new Color(255,69,0); g2d.setColor(co);}
            	else if(c.getColor().equals("ORANGE")){g2d.setColor(Color.ORANGE);}
            	else if(c.getColor().equals("BLUE")){g2d.setColor(Color.BLUE);}
            	else if(c.getColor().equals("DARK_GREEN")){Color co = new Color(1,100,1); g2d.setColor(co);}
            	else if(c.getColor().equals("PURPLE")){Color co = new Color(102,0,102); g2d.setColor(co);}
            	else if(c.getColor().equals("WHITE")){g2d.setColor(Color.WHITE);}
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
            		g.drawArc(c.getX()-c.getRadius(), c.getY()-c.getRadius(), c.getRadius()*2, c.getRadius()*2, 0, 360);
            		g.setColor(Color.BLACK);
            	}
            }
            for(int i=0;i<Drops.size();i++){
            	c++;
            	Drop d= Drops.get(i);
            	if(d.getColor().equals("RED")){g2d.setColor(Color.RED);}
            	else if(d.getColor().equals("GREEN")){g2d.setColor(Color.GREEN);}
            	else if(d.getColor().equals("RED_ORANGE")){Color co = new Color(255,69,0); g2d.setColor(co);}
            	else if(d.getColor().equals("ORANGE")){g2d.setColor(Color.ORANGE);}
            	else if(d.getColor().equals("BLUE")){g2d.setColor(Color.BLUE);}
            	else if(d.getColor().equals("DARK_GREEN")){Color co = new Color(1,100,1); g2d.setColor(co);}
            	else if(d.getColor().equals("PURPLE")){Color co = new Color(102,0,102); g2d.setColor(co);}
            	else if(d.getColor().equals("WHITE")){g2d.setColor(Color.WHITE);}
            	g2d.fillRect((int)d.getRect().getX(),(int)d.getRect().getY(),(int)d.getRect().getWidth(),(int)d.getRect().getHeight());
            	g.setColor(Color.BLACK);
            	g2d.drawRect((int)d.getRect().getX(),(int)d.getRect().getY(),(int)d.getRect().getWidth(),(int)d.getRect().getHeight());
            }
            if(startscreen){
            	g.fillRect(s.getX()-10000, s.getY()-10000, 20000, 20000);
            	BufferedImage image = null;
                try{
                    //Kenny//image= ImageIO.read(new File("Images\\"+name+".png"));
                    image = ImageIO.read(new File("C:\\Users\\Kenny\\stuff\\GameCol\\Image\\Deathbound.png"));
                    image.createGraphics();
                    //image.setRGB(100, 100, 100);
                    //``image.TRANSLUCENT=(double).5;
                }
                catch(Exception e){}
                g2d.drawImage(image,s.getX()+80-image.getWidth()/2,s.getY()-200,null);
            	g.fillRect(s.getX()-325, s.getY()-100+startframes/4, 100, 400);
            	g.fillRect(s.getX()-170, s.getY()-100+startframes/4, 100, 400);
            	g.fillRect(s.getX()+50, s.getY()-100+startframes/4, 100, 400);
            	g.fillRect(s.getX()+200, s.getY()-100+startframes/4, 100, 400);
            	g.fillRect(s.getX()+300, s.getY()-100+startframes/4, 100, 400);
            }
            for(int i=0;i<Images.size();i++){
            	c++;
            	if(Images.get(i).getLifetime()>0){
            		g2d.drawImage(Images.get(i).getImage(), Images.get(i).getX(), Images.get(i).getY(), null);
            	}
            }
            g.setColor(Color.RED);
            for(int i=0;i<Buttons.size();i++){
            	c++;
            	Button b= Buttons.get(i);
            	g.drawRect((int)b.getRect().getX()+s.getX()-camxoff, (int)b.getRect().getY()+s.getY()-camyoff, (int)b.getRect().getWidth(), (int)b.getRect().getHeight());
            	g.drawString(b.getFunction()+"",(int)b.getRect().getX()+s.getX()-camxoff+b.getStringwidth()/4, (int)b.getRect().getY()+s.getY()-camyoff+15);
            }
            g.setColor(Color.BLACK);
            if(debug){
            	for(int i=0;i<bm.map.length;i++){
            		for(int j=0;j<bm.map.length;j++){
            			g.drawString(i+","+j, bm.scale*i, bm.scale*j);
            		}
            	}
            }
            
            //System.out.println(deathtime);
        	if(deathtime<endtime){
        		setBackgroundEffects();
        	}
        	else if(deathtime>endtime&&dead==true){
        		if(deathbound==true){
       				deathbound=false;
        			deathboundkills=0;
        			for(int j=0;j<AIs.size();j++){
    					AIs.get(j).setDead(true);
    				}     
        			sound.muteEffects();
        			sound.shutdown();
        			sound.playeffect("Death");
        			Dialogue d= new Dialogue("");
        			Dialogues.add(0, d);
        			tip=Dialogues.get(0).getRandomtip();
        		}
        		this.setBackground(new Color(244,244,244,255));
        		g.setColor(Color.BLACK);
        		g.fillRect(s.getX()-10000, s.getY()-10000, 20000, 20000);
        		g.setColor(Color.RED);
        		if(postgameframes<1517){
                	BufferedImage image = null;
        			try{
                		//Kenny//image= ImageIO.read(new File("Images\\"+name+".png"));
                		image = ImageIO.read(new File("C:\\Users\\Kenny\\stuff\\GameCol\\Image\\Deathbound.png"));
                		//int[] a= new int[50];
                		//image.setRGB(image.getMinX(), image.getMinY(), image.getWidth(), image.getHeight(), a, 0,0);
                		image.createGraphics();
                    	//``image.TRANSLUCENT=(double).5;
                	}
                	catch(Exception e){}
        			int width = g.getFontMetrics().stringWidth(tip);
        			g.drawString(tip, s.getMidx()-(width/2), s.getMidy()-110);
              		g2d.drawImage(image,s.getX()+80-image.getWidth()/2,s.getY()-80,null);
        		}
        		else if(postgameframes>=1517){
        			BufferedImage image = null;
        			try{
                		image = ImageIO.read(new File("C:\\Users\\Kenny\\stuff\\GameCol\\Image\\Deathbound.png"));       
                		image.createGraphics();
                	}
                	catch(Exception e){}
              		g2d.drawImage(image,s.getX()+80-image.getWidth()/2,s.getY()-80-((postgameframes-1515)/3),null);
        		}
        		if(postgameframes>4900){
        			BufferedImage image = null;
        			try{
                		image = ImageIO.read(new File("C:\\Users\\Kenny\\stuff\\GameCol\\Image\\Deathbound.png"));
                    	image.createGraphics();
                	}
                	catch(Exception e){}
              		g2d.drawImage(image,s.getX()+80-image.getWidth()/2,s.getY()-80,null);
              		g.drawString("Thank You For Playing", s.getMidx()-40, s.getMidy()+100);
        		}
        	}
        	else if(deathtime>endtime&&win==true&&deathbound==true){
        		dead=true;
        	}
        	else if(deathtime>endtime&&dead==false&&deathbound==true){
        		deathtime=0;       		
        		sound.changeVolume(-10);
        		//sound.deleteEffects();
        		deathbound=false;
        	}
        	
        	if(!deathbound&&!dead){
        		for(int i=0;i<Dialogues.size();i++){
        			Dialogue d= Dialogues.get(i);
        			g.setColor(Color.BLACK);
        			int width = g.getFontMetrics().stringWidth(d.getCurrstring());
        			g.drawString(d.getCurrstring(), s.getMidx()-(width/2), s.getY()-10-((Dialogues.size()-1-i)*15));
        		}
        	}
        	if(dead){
        		for(int i=0;i<Dialogues.size();i++){
        			Dialogue d= Dialogues.get(i);
        			g.setColor(Color.RED);
        			int width = g.getFontMetrics().stringWidth(d.getCurrstring());
        			g.drawString(d.getCurrstring(), s.getMidx()-(width/2)+14, s.getY()-((Dialogues.size()-1-i)*20)+(Dialogues.size()*20)+(768/2)+d.getYoff());
        			if(postgameframes%3==0){
        				d.setYoff(d.getYoff()-postcreditspeed);
        			}
        		}
        		if(postgameframes==1515){
        			Dialogue d= new Dialogue("");
        			d.setDelay(0);
        			//d.set
        			d.setYoff((-768/2)-20);
        			Dialogues.set(1, d);
        		}
        		//g.drawString(postgameframes+"", s.getMidx()-100, s.getY());
        	}
        	//System.out.println(g2d.getTransform());
        	//AffineTransform at= new AffineTransform(1,0,g2d.getTransform().getShearX(),0,1,g2d.getTransform().getShearY());
        	//at.inverseTransform(ptSrc, ptDst)
            if(s.isShooting()){
            	//g2d.drawLine(s.getMidx(), s.getMidy(), mousex, mousey);      
                g2d.drawArc(mousex-2, mousey-2, 5, 5, 0, 360);
            	int distance=90;
            	if(s.getChargeframes()<s.getMaxchargeframes()){
            		s.setChargeframes(s.getChargeframes()+1);
            	}
            	s.setTotalchargeframes(s.getTotalchargeframes()+1);
        		if(s.getChargeframes()>=s.getMaxchargeframes()){
        			g2d.setColor(Color.RED);
        			//System.out.println(s.getAngle());
        			Object arrow= new Object();
        			arrow.setLocation(s.getMidx()+(int)(distance/3*Math.cos(Math.toRadians(s.getAngle()))),s.getMidy()+(int)(distance/3*Math.sin(Math.toRadians(s.getAngle()))));
        			//System.out.println(s.getX()+","+s.getY()+":"+arrow.getX()+","+arrow.getY());
        			if(s.getTotalchargeframes()%(4-(3*s.getProjectileshots()/5))==0){
        				hitAnimation2(arrow,s.getMidx()+(int)(distance/4*Math.cos(Math.toRadians(s.getAngle()))),s.getMidy()+(int)(distance/4*Math.sin(Math.toRadians(s.getAngle()))),1,1,10+(2*s.getProjectileshots()),"RED");
        			}
        			//hitAnimation2(arrow,s.getMidx()+(int)(30*Math.cos(Math.toRadians(s.getAngle()-180))),s.getMidy()+(int)(30*Math.sin(Math.toRadians(s.getAngle()-180))),1,1,10,"RED");
        		}
            	g.drawLine(s.getMidx()+(int)((distance-(60*s.getChargeframes()/s.getMaxchargeframes()))*Math.cos(Math.toRadians(s.getAngle()-7))), s.getMidy()+(int)((distance-(60*s.getChargeframes()/s.getMaxchargeframes()))*Math.sin(Math.toRadians(s.getAngle()-7))), s.getMidx()+(int)(distance*Math.cos(Math.toRadians(s.getAngle()))), s.getMidy()+(int)(distance*Math.sin(Math.toRadians(s.getAngle()))));
				g.drawLine(s.getMidx()+(int)((distance-(60*s.getChargeframes()/s.getMaxchargeframes()))*Math.cos(Math.toRadians(s.getAngle()+7))), s.getMidy()+(int)((distance-(60*s.getChargeframes()/s.getMaxchargeframes()))*Math.sin(Math.toRadians(s.getAngle()+7))), s.getMidx()+(int)(distance*Math.cos(Math.toRadians(s.getAngle()))), s.getMidy()+(int)(distance*Math.sin(Math.toRadians(s.getAngle()))));
				//g.drawLine(s.getMidx()+(int)(distance*Math.cos(Math.toRadians(s.getAngle()))), s.getMidy()+(int)(distance*Math.sin(Math.toRadians(s.getAngle()))),s.getMidx()+(int)(s.distance(mousex,mousey)*Math.cos(Math.toRadians(s.getAngle()))), s.getMidy()+(int)(s.distance(mousex,mousey)*Math.sin(Math.toRadians(s.getAngle()))));
				s.boostSpeed(-2, 1);
				g.setColor(Color.BLACK);
            }
            else{
            	s.setChargeframes(0);
            	s.setTotalchargeframes(0);
            }
        	AffineTransform tran = g2d.getTransform();
        	tran.invert();
        	g2d.transform(tran);
        	g2d.translate((int)frame.getBounds().getWidth()/2-((cam.getX()+camxoff+60))+s.getCamdisplacex()+s.getShakex(), (int)frame.getBounds().getHeight()/2-((cam.getY()+camyoff+30))+s.getCamdisplacey()+s.getShakey());//begins cam
        	g.drawString("FPS: "+fps+"  Time:"+totaltime+"  Kills:"+totalkills+"  Deaths:"+deaths,(int)cam.getX()+5-(int)s.getCamdisplacex(),(int)cam.getY()+20-(int)s.getCamdisplacey());//FPS Counter (Top-Right)
        	if(easy){
        		g.drawString("Easy",(int)cam.getX()+5-(int)s.getCamdisplacex()+240,(int)cam.getY()+20-(int)s.getCamdisplacey());
        	}
        	if(hard){
        		g.drawString("Hard",(int)cam.getX()+5-(int)s.getCamdisplacex()+240,(int)cam.getY()+20-(int)s.getCamdisplacey());
        	}
        	if(!easy&&!hard){
        		g.drawString("Normal",(int)cam.getX()+5-(int)s.getCamdisplacex()+240,(int)cam.getY()+20-(int)s.getCamdisplacey());
        	}
        	//g2d.drawRect((int)(cam.getX())+(int)frame.getBounds().getWidth()-950, (int)(cam.getY())+(int)frame.getBounds().getHeight()-50, 500, 40);
        	//g2d.setColor(Color.RED);
        	//g2d.fillRect((int)(cam.getX())+(int)frame.getBounds().getWidth()-1150+(450-450*s.getDisplayhealth()/s.getMaxhealth()), (int)(cam.getY())+(int)frame.getBounds().getHeight()-40, (900*s.getDisplayhealth()/s.getMaxhealth()), 10);
        	g2d.setColor(Color.BLACK);
        	g2d.fillRect((int)(cam.getX())-5-(int)s.getCamdisplacex(),(int)(cam.getY())-(camyoff)-(int)s.getCamdisplacey(),1600,500-borderframes);
        	g2d.fillRect((int)(cam.getX())-5-(int)s.getCamdisplacex(),(int)(cam.getY())+(camyoff*2)+borderframes-(int)s.getCamdisplacey()-80,1600,200);
        	g2d.fillArc((int)cam.getX(), (int)cam.getY(), 5, 5, 0, 360);
        	g2d.translate(-((int)frame.getBounds().getWidth()/2-((cam.getX()+camxoff+60))+s.getCamdisplacex()+s.getShakex()),-((int)frame.getBounds().getHeight()/2-((cam.getY()+camyoff+30))+s.getCamdisplacey()+s.getShakey()));//begins cam
        	g2d.transform(g2d.getTransform());

        	g.drawString("Deathbound by Kenny Doan",bm.startx+5,bm.starty);
        	//g2d.translate(cam.getX()-s.getCamdisplacex()-s.getShakex(), cam.getY()-s.getCamdisplacey()-s.getShakey());//end of cam
            g2d.translate(-((int)frame.getBounds().getWidth()/2-((cam.getX()+camxoff+60)*scale)+s.getCamdisplacex()*scale+s.getShakex()), -((int)frame.getBounds().getHeight()/2-((cam.getY()+camyoff+30)*scale)+s.getCamdisplacey()*scale+s.getShakey()));//begins cam
            //g2d.rotate(-30,0,0);
        }
        catch(Exception e){
        }
        
    }
    
    /**
     * Returns the length depending on the part of the lines
     * @param 
     * @param tier wither be 60,120,180, or 240
     * @return
     */
    public int calculateDis(int length, int amount, int tier){
    	int result=amount;
    	if(amount>=tier){
    		result=tier;
    	}
    	else if(amount>(tier-90)){
    		result=amount;
    		while(result>90){
    			result-=90;
    			tier-=90;
    		}
    	}
    	else{
    		result=0;
    	}
    	//System.out.println("Length:"+((length*result)/tier)+" Tier:"+tier+" Amount:"+amount+"");
    	return ((length*result)/tier);
    }
    
    public void calculateBlockoffsets(Block b){
    	ymax=25;
    	xmax=0;
    	if(Math.abs(b.getMidx()-s.getMidx())<1000&&b.getMidx()-s.getMidx()>0){
    		xoff=(int)(xmax*((double)Math.abs(b.getMidx()-s.getMidx())/(double)1000));
    	}
    	else if(Math.abs(b.getMidx()-s.getMidx())>=1000&&b.getMidx()-s.getMidx()>0){
    		xoff=xmax;
    	}
    	else if(Math.abs(b.getMidx()-s.getMidx())<1000&&b.getMidx()-s.getMidx()<0){
    		xoff=(int)(-xmax*((double)Math.abs(b.getMidx()-s.getMidx())/(double)1000));
    	}
    	else if(Math.abs(b.getMidx()-s.getMidx())>=1000&&b.getMidx()-s.getMidx()<0){
    		xoff=-xmax;
    	}
    	
    	if(Math.abs(b.getMidy()-s.getMidy())<1000&&b.getMidy()-s.getMidy()>0){
    		yoff=(int)(ymax*((double)Math.abs(b.getMidy()-s.getMidy())/(double)1000));
    	}
    	else if(Math.abs(b.getMidy()-s.getMidy())>=1000&&b.getMidy()-s.getMidy()>0){
    		yoff=ymax;
    	}
    	else if(Math.abs(b.getMidy()-s.getMidy())<1000&&b.getMidy()-s.getMidy()<0){
    		yoff=(int)(-ymax*((double)Math.abs(b.getMidy()-s.getMidy())/(double)1000));
    	}
    	else if(Math.abs(b.getMidy()-s.getMidy())>=1000&&b.getMidy()-s.getMidy()<0){
    		yoff=-ymax;
    	}
    	yoff=-20;
    }

    /**
     * The movement "tick" of the player. Moves player according to the velocities
     * @throws Exception 
     */
	public void updateSquare() throws Exception{ 
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
				if(s.isDashstunned()==true){//Reduces dash cooldown if 
					s.setCurrdashcooldown(s.getCurrdashcooldown()/4);
					s.setDashstunned(false);
				}
				if(s.getCurrphasewalkcooldown()<=0&&s.isStundashupgrade()==true){
					s.setInvunerableframes(s.getInvunerableframes()+10);
				}
				if(s.isPhasewalkupgrade()==true&&s.getCurrphasewalkcooldown()<=0&&s.isPhasewalking()==false){//Phase walk upgrade
					if(!cinematic){opening=false;}
					UPDATE=((double)1/(double)30);
					sound.warp();
					//sound.playeffect("Phasewalkstart");
					s.setPhasewalking(true);
					if(!deathbound){
						s.boostSpeed(2, 90);
					}
	        		s.setInvunerableframes(90);
				}
			}
		}

		
	}
	
	public void spawnEnemies(){
		int delay=10;
		if(easy){
			delay=15;
		}
		if(totaltime%delay==0||totaltime==5){
			boolean foundspot=false;
			//int num=0;
			while(foundspot==false){
				foundspot=true;
				//Point p= randomPointnear(s.getX(),s.getY(),600);
				//clusterSpawn((int)p.getX(),(int)p.getY(),4,30,6);
				int speed=6;
				if(easy){speed=4;}
				surroundSpawn(s.getMidx(),s.getMidy(),30,speed,2+((totaltime-1)/120)+(int)difficultyvarible,400);//Spawns on more Jumper every 20 seconds
				foundspot=true;
			}
			int amount=0;
			while(amount<1+(int)(difficultyvarible/2)&&totaltime%20==0){
				Point p= randomPointnear(s.getX(),s.getY(),600);
				Pusher pu= new Pusher((int)p.getX(),(int)p.getY(),30,6);
				pu.setShootcooldown(pu.getShootcooldown()-((totaltime/60)*5*(int)difficultyvarible));
				pu.setHealth(pu.getMaxhealth());
				AIs.add(pu);
				amount++;
			}
			amount=0;
			while(amount<1+(int)(difficultyvarible/2)&&((totaltime-10)%20==0)){
				Point p= randomPointnear(s.getX(),s.getY(),600);
				Sprayer sp= new Sprayer((int)p.getX(),(int)p.getY(),30,6);
				sp.setShootcooldown(sp.getShootcooldown()-(totaltime/60));
				sp.setMaxhealth(sp.getMaxhealth()+((totaltime/60)*10*(int)difficultyvarible));
				AIs.add(sp);
				amount++;
			}
			foundspot=false;
			while(foundspot==false&&totaltime>=40&&totaltime%30==0){
				Point p= randomPointnear(s.getX(),s.getY(),600);
				Teleporter tp= new Teleporter((int)p.getX(),(int)p.getY(),35,6);
				tp.setMaxhealth(tp.getMaxhealth()+((totaltime/60)*10*(int)difficultyvarible));
				tp.setHealth(tp.getMaxhealth());
				if(totaltime>60||!easy){
					AIs.add(tp);
				}
				foundspot=true;
			}
			foundspot=false;
			while(foundspot==false&&(totaltime>=120&&totaltime%40==0)){
				Point p= randomPointnear(s.getX(),s.getY(),600);
				Healer h= new Healer((int)p.getX(),(int)p.getY(),25,7);
				h.setMaxhealth(h.getMaxhealth()+((totaltime/60)*3*(int)difficultyvarible));
				h.setHealth(h.getMaxhealth());
				if(totaltime>120||!easy){
					AIs.add(h);
				}
				foundspot=true;
			}
			foundspot=false;
			while(foundspot==false&&(totaltime==180||(totaltime>=180&&totaltime%25==0))){
				Point p= randomPointnear(s.getX(),s.getY(),600);
				Dodger d= new Dodger((int)p.getX(),(int)p.getY(),30,6);
				d.setMaxhealth(d.getMaxhealth()+((totaltime/60)*5*(int)difficultyvarible));
				d.setHealth(d.getMaxhealth());
				AIs.add(d);
				foundspot=true;
			}
			foundspot=false;
			while(foundspot==false&&(totaltime==300)){
				Point p= randomPointnear(s.getX(),s.getY(),600);
				Boss b= new Boss((int)p.getX(),(int)p.getY(),50,2);
				b.setMaxhealth(b.getMaxhealth());
				b.setHealth(b.getMaxhealth());
				AIs.add(b);
				foundspot=true;
			}
		}		
	}
	
	
	
	public static void main(String[] args) throws Exception{
        Play p= new Play(); //Jpanel Class
        //frame.setSize(500,500);
        p.start();
    }
	
	public void changedifficulty(){
		difficultyvarible=1;
		if(easy){
    		difficultyvarible=difficultyvarible/2;
    	}
    	if(hard){
    		difficultyvarible=difficultyvarible*2;
    	}
	}
	
	public void addString(String str, int intialwait, int delay, int wait){
		Dialogue d= new Dialogue(str);
		d.setDelay(delay);
		d.setWait(wait);
		d.setCurrdelay(delay-1);
		d.setCurrwait(0);
		d.setCurrintialwait(0);
		d.setIntialwait(intialwait);
		Dialogues.add(d);
	}
	
	public void displayDialogues(){
		if(wave==1&&deaths==0){
			addString("DEATHBOUND",0,5,300);
		}
		else if(wave==2){
        	//addString("The Mayor(The Arena Master): ",0,2,300);
			//addString("Mayor: For the murder of my daugther",200,5,250);
			//addString("I DECLARE YOUR EXCUTION BY THE ARENA!",500,5,200);
			//addString("YOU ARE...",710,5,160);
			//addString("DEATHBOUND",960,5,200);
			//addString("Mayor: For the murder of my daugther Hina",0,5,250);
			//addString("I DECALRE YOUR EXCUTION BY THE ARENA!",250,5,200);
			//addString("YOU ARE...",460,5,160);
			//addString("DEATHBOUND",660,5,200);
		}
		if(!hard&&deaths>0){
			Dialogue d= new Dialogue("");
			Dialogues.add(0, d);
			tip=Dialogues.get(0).getRandomtip();
        	addString(tip, 0,0, 300);
		}
	}
	
	public void postCredits(){
		addString("",0,0,10000);
		Dialogues.get(0).postCredits(this);
	}
	
	public void setBackgroundEffects() throws Exception{
		if(deathbound==false&&deathtimeframes>0){
			sound.changeVolume(-10+(-20*deathtimeframes/(endtime*60)));
			s.setDizzylimit(5);
			if(deathtimeframes==((endtime*60)*3/4)){
				sound.playeffect("Cheer2");
			}
			//System.out.println(deathtimeframes+"");
			//System.out.println((endtime*60)/2+"");
			int num=244-((199*deathtimeframes/(endtime*60)));
			this.setBackground(new Color(num, num, num));
		}
		else if(!dead){
			if(100*s.getHealth()/s.getMaxhealth()<33){
				int num=s.getHealth();
    			if(num<1){
    				num=1;
    			}
    			int num2=125+((130*num)/s.getMaxhealth());
    			int num3;
    			if(num<1){
    				num3=230;
    			}
    			else{
    				num3=230+((14*s.getHealth())/s.getMaxhealth());
    			}
    			//System.out.println(num2);
    			this.setBackground( new Color(num3, num3, num3, num2) );
			}
			else{
				int num=230+((14*s.getHealth())/s.getMaxhealth());
				this.setBackground(new Color(num, num, num));
			}
		}
		if(deathbound==true){
			//System.out.println(deathtimeframes+"");
			//System.out.println((endtime*60*2)/3+"");
			if(deathtimeframes==((endtime*60*2)/3)&&deathboundkills==0){
				sound.playeffect("Crowdboo");
				deathboundkills=-1;
			}
			sound.changeVolume(-10+(-20*deathtimeframes/(endtime*60)));
			s.setDizzylimit(5);
			int num=244-((199*deathtimeframes/(endtime*60)));
			this.setBackground(new Color(num, num, num));
		}
	}
	
	public void clearBlocks(){
		for(int i=0;i<blocks.size();i++){
			blocks.remove(0);
		}
	}
	
	public void reset() throws Exception{
		scale=1;
		int size=AIs.size();
		for(int i=0;i<size;i++){
			AIs.remove(0);
		}
		size=Dialogues.size();
		for(int i=0;i<size;i++){
			Dialogues.remove(0);
		}
		
		clearBlocks();
		bm= new BlockMap(0,0,0,0,this);
		bm.createArena(4, 4);
    	bm.addBlocks(blocks);
    	addWalls();
    	
    	sound.muteEffects();
    	sound.shutdown();
    	sound= new SoundEngine("Dark","DarkHalf",2,this);
    	sound.startUp();
		s=new Square();
		changedifficulty();
		deathtime=0;
		deathtimeframes=0;
		postgameframes=0;
		totaltime=0;
		totalkills=0;
		wave=1;
		s.setShotcharges(3);
		s.setHealth(s.getMaxhealth());
		dead=false;
		deathbound=false;
		deathboundkills=0;
		inbreak=false;
		win=false;
		displayDialogues();
	}
}

