package ARPG;

import javax.swing.*;

import java.awt.event.*;

public class Input extends JPanel implements ActionListener, KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	Play p;
	
	public Input(Play p){
		this.p=p;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
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

        if(c==KeyEvent.VK_SPACE){
        	p.getHandler().addInput("SPACE");
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
		// TODO FIX PROJECTILE REGISTERING
		if(p.getHandler().spawning==false){
			int x1 = p.getSquare().getX() + p.getSquare().getWidth() / 2;
			int y1 = p.getSquare().getY() + p.getSquare().getHeight() / 2;
			int x2 = e.getX() + p.getSquare().getX() - p.camxoff;
			int y2 = e.getY() + p.getSquare().getY() - p.camyoff;
			p.getHandler().setMouseCoords(x1, y1, x2, y2);
			p.getHandler().spawning=true;
			p.getHandler().addInput("CLICKED");
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
