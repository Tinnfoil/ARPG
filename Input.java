package ARPG;

import javax.swing.*;

import java.awt.event.*;

public class Input extends JPanel implements ActionListener, KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	Play p;
	private int[] mousecoords= new int[2];
	private boolean canteleport;
	
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
        
        if(c==KeyEvent.VK_T){
        	canteleport=true;
        }
        if(c==KeyEvent.VK_Q){
        	p.clusterSpawn(p.getSquare().getX(), p.getSquare().getY(), 8, 30, 5);
        }
        if(c==KeyEvent.VK_E){
        	p.getSquare().knockback(p.getSquare(),p.getSquare().getX(),p.getSquare().getY(),2);
        }
        if(c==KeyEvent.VK_P){
        	p.reset();
        }
        if(c==KeyEvent.VK_R){
        	p.getSquare().stop();
        	p.getSquare().setStunframes(120);
        }

        if(c==KeyEvent.VK_SPACE){
        	if(p.s.getCurrdashcooldown()<=0){
        		p.getHandler().addInput("SPACE");
        	}
        	if(p.s.getCurrdashcooldown()>=p.s.getAttackcooldown()){
        		p.getHandler().removeInput("SPACE");
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
		else if(e.getButton()==2){
			if(p.getHandler().spawning==false){//*****removing spawning will create a "Wall"
				p.getHandler().setMouseCoords(x1, y1, x2, y2);
				p.getHandler().addInput("CLICKED");
				p.getHandler().spawning=true;
				//System.out.println("Clicked");
			}
		}
		else if(e.getButton()==1){
			if(p.getHandler().attacking==false&&p.getSquare().getCurrattackcooldown()<=0){
				p.getHandler().setMouseCoords(x1, y1, x2, y2);
				p.getHandler().attackdelay=5;
				p.getHandler().addInput("ATTACK");
				p.getHandler().attacking=true;
				
				p.getSquare().setCurrattackcooldown(p.getSquare().getAttackcooldown());
			}
		}
		else if(e.getButton()==3){
			mousecoords[0]=x2;
			mousecoords[1]=y2;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==3){
			//System.out.println("Relaease");
			int x2 = e.getX() + p.getSquare().getX() - p.camxoff;
			int y2 = e.getY() + p.getSquare().getY() - p.camyoff;
			p.getHandler().setMouseCoords(mousecoords[0], mousecoords[1], x2, y2);
			p.getHandler().addInput("CLICKED");
			p.getHandler().spawning=true;
		}

	}

}
