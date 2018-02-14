package ARPG;

import javax.swing.*;

import java.awt.MouseInfo;
import java.awt.event.*;

public class Input extends JPanel implements ActionListener, KeyListener, MouseListener{

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
        if(c==KeyEvent.VK_1){
        	if(p.getSquare().getSkillpoints()>0&&p.inbreak==true){
        		if(p.page1){
        			p.s.setProjectileshots(p.getSquare().getProjectileshots()+2);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-1);
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
            	else if(p.getSquare().getSkillpoints()>=2){
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
            	else if(p.getSquare().getSkillpoints()>=2){
        			p.getSquare().setPhasewalkupgrade(true);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
        		}
        	}
        }
        if(c==KeyEvent.VK_4){
        	if(p.getSquare().getSkillpoints()>0&&p.inbreak==true){
        		if(p.page1){
        			p.s.setAttackdamage(p.getSquare().getAttackdamage()+10);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-1);
        		}
            	else if(p.getSquare().getSkillpoints()>=2){
        			//
        			//p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
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
            	else if(p.getSquare().getSkillpoints()>=2){
        			//
        			//p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
        		}
        	}
        }
        if(c==KeyEvent.VK_6){
        	if(p.getSquare().getSkillpoints()>0&&p.inbreak==true){
        		if(p.page1){
        			p.s.setHealthonhit(p.getSquare().getHealthonhit()+4);
        			p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-1);
        		}
            	else if(p.getSquare().getSkillpoints()>=2){
        			//
        			//p.getSquare().setSkillpoints(p.getSquare().getSkillpoints()-2);
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
        	canteleport=true;
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
        if(c==KeyEvent.VK_P){
        	p.reset();
        }
        if(c==KeyEvent.VK_O){
        	if(p.inbreak==true){
        		p.inbreak=false;
        	}
        }
        if(c==KeyEvent.VK_R){
        	//p.projectileExplosion(p.getSquare().getMidx()+200, p.getSquare().getMidy(), true, true, 50 ,20, 30);
        }
        if(c==KeyEvent.VK_X){
        	p.spawnPusher(p.getSquare().getMidx()+200, p.getSquare().getMidy(),30,6);
        }
        if(c==KeyEvent.VK_C){
        	Teleporter t= new Teleporter(p.getSquare().getMidx()+200, p.getSquare().getMidy(),35,6);
        	p.AIs.add(t);
        }
        if(c==KeyEvent.VK_G){
        	double interval=60/6;
        	Projectile[] ps= new Projectile[5];
        	for(int i=0;i<ps.length;i++){
        		Projectile po= new Projectile(p.getSquare().getMidx(), p.getSquare().getMidy(),p.getSquare().getMidx(), p.getSquare().getMidy(),7,40);
        		po.setDamage(20);
        		po.setHurtsenemy(true);
        		po.setSquareshot(true);
        		po.changeAngle(40-(60/2)+(i*interval));
        		ps[i]=po;
        	}
        	p.spawnMultipleProjectiles(ps);
        	//p.spawnMultipleProjectiles(p.getSquare().getMidx(), p.getSquare().getMidy(), 50, 10, 60, 20);
        }

        if(c==KeyEvent.VK_SPACE){
        	if(p.s.getCurrdashcooldown()<=0){
        		p.getHandler().addInput("SPACE");
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
	public void mousePressed(MouseEvent e) {
		// TODO FIX PROJECTILE REGISTERING
		int x1 = p.getSquare().getX() + p.getSquare().getWidth() / 2;
		int y1 = p.getSquare().getY() + p.getSquare().getHeight() / 2;
		int x2 = e.getX() + p.getSquare().getX() - p.camxoff;
		int y2 = e.getY() + p.getSquare().getY() - p.camyoff;
		if(e.getButton()==2&&canteleport==true){//teleport ability, press t and left click
			p.getSquare().setX(x2);
			p.getSquare().setY(y2);
			canteleport=false;
		}
		else if(e.getButton()==2&&p.getSquare().getShotcharges()>0){
			if(p.getHandler().spawning==false){//*****removing spawning will create a "Wall"
				p.getSquare().setShotcharges(p.getSquare().getShotcharges()-1);
				p.getHandler().setMouseCoords(x1, y1, x2, y2);
				p.getHandler().addInput("CLICKED");
				p.getHandler().spawning=true;
				//System.out.println("Clicked");
			}
		}
		else if(e.getButton()==1){
			if(p.getHandler().attacking==false&&p.getSquare().getCurrattackcooldown()<=0){
				p.getHandler().setMouseCoords(x1, y1, x2, y2);
				p.getHandler().attackdelay=4;
				p.getHandler().addInput("ATTACK");
				p.getHandler().attacking=true;
				
				p.getSquare().setCurrattackcooldown(p.getSquare().getAttackcooldown());
			}
		}
		else if(e.getButton()==3&&p.getSquare().getCurrparrycooldown()==0){
			System.out.println("X:"+x2+" Y:"+y2);
			p.getHandler().setMouseCoords(x1, y1, x2, y2);//not the problem
			p.getSquare().stop();//Stop moving
			p.getSquare().setStunframes(25);//must be equal to the parry frames
			p.getSquare().setParryframes(25);
			p.getSquare().setCurrparrycooldown(p.getSquare().getParrycooldown());
		}
		else if(e.getButton()==3&&canlinecast==true){
			mousecoords[0]=x2;
			mousecoords[1]=y2;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==3&&canlinecast==true){
			//System.out.println("Relaease");
			int x2 = e.getX() + p.getSquare().getX() - p.camxoff;
			int y2 = e.getY() + p.getSquare().getY() - p.camyoff;
			p.getHandler().setMouseCoords(mousecoords[0], mousecoords[1], x2, y2);
			p.getHandler().addInput("CLICKED");
			p.getHandler().spawning=true;
			canlinecast=false;
		}

	}

}
