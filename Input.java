package ARPG;

import javax.swing.*;

import java.awt.MouseInfo;
import java.awt.event.*;
import java.util.Random;

public class Input extends JPanel implements ActionListener, KeyListener, MouseListener, MouseWheelListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;
	Play p;
	private int[] mousecoords= new int[2];
	private boolean canteleport;
	private boolean canlinecast;
	
	public Input(Play p){
		this.p=p;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        mousecoords[0]=0;mousecoords[1]=0;
        canteleport=false;
	}

	public void actionPerformed(ActionEvent e){
		
    }

    public void keyTyped(KeyEvent e){

    }

    /**
     * Movement commands for Player. Also will set the direction of the Player's movement.
     */
    public void keyPressed(KeyEvent e){
        int c=e.getKeyCode();
        if(p.startscreen==false){
        if(c==KeyEvent.VK_RIGHT||c==KeyEvent.VK_D){
       	 	p.getHandler().addInput("RIGHT");
       	 	//p.getHandler().readList();
        }
        if((c==KeyEvent.VK_UP||c==KeyEvent.VK_W)){
        	p.getHandler().addInput("UP");
        	//p.getHandler().readList();
        }
        if(c==KeyEvent.VK_LEFT||c==KeyEvent.VK_A){
        	p.getHandler().addInput("LEFT");
        	//p.getHandler().readList();
        }
        if(c==KeyEvent.VK_DOWN||c==KeyEvent.VK_S){
        	p.getHandler().addInput("DOWN");
        	//p.getHandler().readList();
        }
        }
        if(c==KeyEvent.VK_1){
        	if(p.getSquare().getSkillpoints()>0&&p.inbreak==true){
        		if(p.page1){
        			if(p.getSquare().getProjectileshots()<5){
        				p.s.setProjectileshots(p.getSquare().getProjectileshots()+2);
        				p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-1);
        			}
        		}
        		else if(p.getSquare().getSkillpoints()>=2&&p.getSquare().isFireupgrade()==false){
        			p.getSquare().setFireupgrade(true);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
        		}
        	}
        }
        if(c==KeyEvent.VK_2){
        	if(p.getSquare().getSkillpoints()>0&&p.inbreak==true){
        		if(p.page1){
        			if(p.getSquare().getMaxspeed()<=7){
        				p.s.setMaxspeed(p.getSquare().getMaxspeed()+1);
        				p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-1);
        			}
        		}
            	else if(p.getSquare().getSkillpoints()>=2&&p.getSquare().isCanfreeze()==false){
        			p.getSquare().setCanfreeze(true);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
        		}
        	}
        }
        if(c==KeyEvent.VK_3){
        	if(p.getSquare().getSkillpoints()>0&&p.inbreak==true){
        		if(p.page1){
        			p.s.setMaxhealth(p.getSquare().getMaxhealth()+50);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-1);
        		}
            	else if(p.getSquare().getSkillpoints()>=2&&p.getSquare().isPhasewalkupgrade()==false){
        			p.getSquare().setPhasewalkupgrade(true);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
        		}
        	}
        }
        if(c==KeyEvent.VK_4){
        	if(p.getSquare().getSkillpoints()>0&&p.inbreak==true){
        		if(p.page1){
        			p.s.setAttackdamage(p.getSquare().getAttackdamage()+15);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-1);
        		}
            	else if(p.getSquare().getSkillpoints()>=2&&p.getSquare().isComboupgrade()==false){
            		p.getSquare().setComboupgrade(true);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
        		}
        	}
        }
        if(c==KeyEvent.VK_5){
        	if(p.getSquare().getSkillpoints()>0&&p.inbreak==true){
        		if(p.page1){
        			if(p.s.getDashcooldown()>=36){
        				p.s.setDashcooldown(p.getSquare().getDashcooldown()-12);
        				p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-1);
        			}
        		}
            	else if(p.getSquare().getSkillpoints()>=2&&p.getSquare().isStundashupgrade()==false){
        			p.getSquare().setStundashupgrade(true);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
        		}
        	}
        }
        if(c==KeyEvent.VK_6){
        	if(p.getSquare().getSkillpoints()>0&&p.inbreak==true){
        		if(p.page1){
        			p.s.setHealthonhit(p.getSquare().getHealthonhit()+4);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-1);
        		}
            	else if(p.getSquare().getSkillpoints()>=2&&p.getSquare().isSpinupgrade()==false){
            		p.getSquare().setSpinupgrade(true);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
        		}
        	}
        }
        if(c==KeyEvent.VK_0){
        	if(p.inbreak==true&&p.page1==true){
        		p.page1=false;
        	}
        	else if(p.inbreak==true){
        		p.page1=true;
        	}
        }
        
        if(c==KeyEvent.VK_T){
        	//canteleport=true;
        	Teleporter t= new Teleporter(p.getSquare().getMidx()+200, p.getSquare().getMidy(),35,6);
        	p.AIs.add(t);
        }
        if(c==KeyEvent.VK_Q){
        	Sprayer s= new Sprayer(p.getSquare().getX()+200,p.getSquare().getY(),30,5);
        	p.AIs.add(s);
        }
        if(c==KeyEvent.VK_E){
        	int x= (int)MouseInfo.getPointerInfo().getLocation().x+p.getSquare().getX() - p.camxoff;
        	int y= (int)MouseInfo.getPointerInfo().getLocation().y+p.getSquare().getY() - p.camyoff;
        	System.out.println("X:"+x+" Y:"+y);
        }
        if(c==KeyEvent.VK_L){
        	canlinecast=true;
        }
        if(c==KeyEvent.VK_ENTER){
        	if(p.startscreen){
        		p.startscreen=false;
				p.clearButtons();
				try {
					p.sound.changeTrack("Dark", "DarkHalf", 2);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        	try {
				p.reset();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
        if(c==KeyEvent.VK_O){
        	if(p.inbreak==true&&p.AIs.size()==0&&p.win==false){
				p.getSquare().setHealth(p.getSquare().getMaxhealth());
				try {
					p.sound.changeTrack("Dark", "DarkHalf", 2);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        		p.inbreak=false;
        		p.displayDialogues();
        	}
        }
        if(c==KeyEvent.VK_R){
        	Dodger d= new Dodger(p.getSquare().getMidx()+200, p.getSquare().getMidy(),30,6);
        	p.AIs.add(d);
        }
        if(c==KeyEvent.VK_H){
        	Healer h= new Healer(p.getSquare().getMidx()+200, p.getSquare().getMidy(),25,7);
        	p.AIs.add(h);
        }
        if(c==KeyEvent.VK_X){
        	p.spawnPusher(p.getSquare().getMidx()+200, p.getSquare().getMidy(),30,6);
        }
        if(c==KeyEvent.VK_C){
        	if(p.opening==false){
        		p.cinematic=false;
        		p.opening=true;
        	}
        	else{
        		p.cinematic=true;
        		p.opening=false;
        	}
        }
        if(c==KeyEvent.VK_B){
        	Boss b= new Boss(p.getSquare().getMidx()+200, p.getSquare().getMidy(),50,2);
        	p.AIs.add(b);
        }
        if(c==KeyEvent.VK_M){
        	p.sound.changeVolume(-50);
        }
        if(c==KeyEvent.VK_EQUALS){
        	if(p.easy==true){
        		p.easy=false;
        	}
        	else if(p.hard==false){
        		p.hard=true;
        	}
        	p.changedifficulty();
        }
        if(c==KeyEvent.VK_MINUS){
        	if(p.hard==true){
        		p.hard=false;
        	}
        	else if(p.easy==false){
        		p.easy=true;
        	}
        	p.changedifficulty();
        }
        if(c==KeyEvent.VK_ESCAPE){
        	 p.frame.dispatchEvent(new WindowEvent(p.frame, WindowEvent.WINDOW_CLOSING));
        }
        if(c==KeyEvent.VK_SPACE){
        	if(p.s.getCurrdashcooldown()<=0){
        		p.getHandler().addInput("SPACE");
        	}
        }
        if(c==KeyEvent.VK_L){
        	p.scale+=.1;
        }
        if(c==KeyEvent.VK_K){
        	if(p.scale>1){
        		p.scale-=.1;
        	}
        }
    }

    /**
     * Events that happen when a key is released. In this case, when movement inputs are released,
     * they are removed form the Inputhandler's multikey arraylist and therefore stopping the movement
     */
    public void keyReleased(KeyEvent e){
        int c=e.getKeyCode();
        if(c==KeyEvent.VK_RIGHT||c==KeyEvent.VK_D){
        	p.getHandler().removeInput("RIGHT");
        	//p.getHandler().readList();
        }
        if((c==KeyEvent.VK_UP||c==KeyEvent.VK_W)){
        	p.getHandler().removeInput("UP");
        	//p.getHandler().readList();
        }
        if(c==KeyEvent.VK_LEFT||c==KeyEvent.VK_A){
        	p.getHandler().removeInput("LEFT");
        	//p.getHandler().readList();
        }
        if(c==KeyEvent.VK_DOWN||c==KeyEvent.VK_S){
        	p.getHandler().removeInput("DOWN");
        	//p.getHandler().readList();
        }
        if(c==KeyEvent.VK_SPACE){
        		p.getHandler().removeInput("SPACE");
        }
    }

    /**
     * Shoots a projectile when the mouse is clicked and toward the position of the mouse arrow
     * *TO BE FIXED, some clicks dont get registered
     */
	public void mouseClicked(MouseEvent e) {// projectile lifetime, multikey pressing, cooldowns
		//System.out.println("as");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e){
		// TODO FIX PROJECTILE REGISTERING
		Random RN= new Random();
		int x1 = p.getSquare().getX() + p.getSquare().getWidth() / 2;
		int y1 = p.getSquare().getY() + p.getSquare().getHeight() / 2;
		int x2 = e.getX() + p.getSquare().getX() - p.camxoff;
		int y2 = e.getY() + p.getSquare().getY() - p.camyoff;
		//System.out.println(x2+","+y2);
		for(int i=0;i<p.Buttons.size();i++){
			//Point point=new Point(x2,y2);
			if(p.Buttons.get(i).getRect().contains(e.getX(), e.getY())){
				if(p.Buttons.get(i).getFunction().equals("Start Game")){
					p.startscreen=false;
					p.clearButtons();
					try {
						p.sound.changeTrack("Dark", "DarkHalf", 2);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("Start");
				}
			}
		}
		if(e.getButton()==2&&canteleport==true){//teleport ability, press t and left click
			p.getSquare().setX(x2);
			p.getSquare().setY(y2);
			canteleport=false;
		}
		else if(e.getButton()==2&&p.getSquare().getShotcharges()>0&&p.getSquare().isShooting()==false){
			p.getSquare().setShooting(true);
		}
		else if(e.getButton()==1&&p.startscreen==false){
			if(p.getHandler().attacking==false&&p.getSquare().getCurrattackcooldown()<=0&&p.dead==false){
				try {
					if(p.getSquare().getAttackcombo()<3&&p.sound.frozen()==false){
						//p.sound.playeffect("Swordswing1");
						p.sound.playeffect("Swordswing"+(RN.nextInt(4)+1)+"");
					}
					else if(p.sound.frozen()==false){
						p.sound.playeffect("Swordswing5");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				p.getHandler().setMouseCoords(x1, y1, x2, y2);
				p.getHandler().attackdelay=4;
				p.getHandler().addInput("ATTACK");
				p.getHandler().attacking=true;
				p.getSquare().setCanlifesteal(true);
				p.getSquare().setCurrattackcooldown(p.getSquare().getAttackcooldown());
			}
		}
		else if(e.getButton()==3&&p.getSquare().getCurrparrycooldown()==0){
			//System.out.println("X:"+x2+" Y:"+y2);
			try {
				//p.sound.warp();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			p.getHandler().setMouseCoords(x1, y1, x2, y2);//not the problem
			p.getHandler().clickangle=(int)p.getSquare().findAngle(y2-y1,x2-x1);
			p.getSquare().stop();//Stop moving
			p.getSquare().setParryframes(20);
			p.getSquare().setCurrparrycooldown(p.getSquare().getParrycooldown());
			//p.angle=(int)p.getSquare().findAngle(y2-y1, x2-x1)+180;
			//p.getSquare().setAttackangle((int)p.getSquare().findAngle(y2-y1, x2-x1));
			//if(p.s.getAttackcombo()==1){p.s.setAttackangle(p.s.getAttackangle()-30);}
			//if(p.s.getAttackcombo()==2){p.s.setAttackangle(p.s.getAttackangle()-30);}
		}
		else if(e.getButton()==3&&canlinecast==true){
			mousecoords[0]=x2;
			mousecoords[1]=y2;
		}
		mouseMoved(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x1 = p.getSquare().getX() + p.getSquare().getWidth() / 2;
		int y1 = p.getSquare().getY() + p.getSquare().getHeight() / 2;
		int x2 = e.getX() + p.getSquare().getX() - p.camxoff;
		int y2 = e.getY() + p.getSquare().getY() - p.camyoff;
		if(e.getButton()==3&&canlinecast==true){
			//System.out.println("Relaease");
			int x3 = e.getX() + p.getSquare().getX() - p.camxoff;
			int y3 = e.getY() + p.getSquare().getY() - p.camyoff;
			p.getHandler().setMouseCoords(mousecoords[0], mousecoords[1], x3, y3);
			p.getHandler().addInput("CLICKED");
			p.getHandler().spawning=true;
			canlinecast=false;
		}
		if(e.getButton()==2&&p.getSquare().isShooting()){
			if(p.getHandler().spawning==false){//*****removing spawning will create a "Wall"
				p.getSquare().setShotcharges(p.getSquare().getShotcharges()-1);
				p.getHandler().setMouseCoords(x1, y1, x2, y2);
				p.getHandler().addInput("CLICKED");
				p.getHandler().spawning=true;
				try {
					p.sound.playeffect("Shoot");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.out.println("Clicked");
			}
			p.getSquare().setShooting(false);
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		p.mousex=(int)(e.getX()*1-(p.scale-1)) + (int)(p.getSquare().getX()*1-(p.scale-1)) - (int)(p.camxoff*1-(p.scale-1));
		p.mousey=(int)(e.getY()*1-(p.scale-1)) + (int)(p.getSquare().getY()*1-(p.scale-1)) - (int)(p.camyoff*1-(p.scale-1));
		p.getSquare().setAngle(p.getSquare().findAngle(p.mousey-p.getSquare().getMidy(), p.mousex-p.getSquare().getMidx()));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		p.mousex=(int)(e.getX()*1-(p.scale-1)) + (int)(p.getSquare().getX()*1-(p.scale-1)) - (int)(p.camxoff*1-(p.scale-1));
		p.mousey=(int)(e.getY()*1-(p.scale-1)) + (int)(p.getSquare().getY()*1-(p.scale-1)) - (int)(p.camyoff*1-(p.scale-1));
		p.getSquare().setAngle(p.getSquare().findAngle(p.mousey-p.getSquare().getMidy(), p.mousex-p.getSquare().getMidx()));
		//p.getHandler().setMouseCoords(mousecoords[0],mousecoords[1],mousecoords[2],mousecoords[3]);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if(arg0.getWheelRotation()<0&&p.scale<2){
			//p.scale=(double)(((int)((p.scale*1.05)*10000))/100)/100;
			p.scale*=1.05;
		}
		if(arg0.getWheelRotation()>0&&p.scale>.1){
			//p.scale=(double)(((int)((p.scale/1.05)*10000))/100)/100;		
			p.scale/=1.05;
		}
		if(Math.abs(p.scale-1)<.04){
			p.scale=1;
		}
	}

}
