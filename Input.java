package ARPG;


import javax.swing.*;
import java.awt.event.*;

public class Input extends JPanel implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	
	play p;
	
	public Input(play p){
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
     * Movement commands for player. Also will set the direction of the player's movement.
     */
    public void keyPressed(KeyEvent e){
        int c=e.getKeyCode();
        if(c==KeyEvent.VK_RIGHT||c==KeyEvent.VK_D){
            p.getSquare().setVelx(2);
            System.out.println("1");
        }
        if((c==KeyEvent.VK_UP||c==KeyEvent.VK_W)){
        	 p.getSquare().setVely(-2);
        	 System.out.println("2");
        }
        if(c==KeyEvent.VK_LEFT||c==KeyEvent.VK_A){
        	 p.getSquare().setVelx(-2);
        	 System.out.println("3");
        }
        if(c==KeyEvent.VK_DOWN||c==KeyEvent.VK_S){
        	 p.getSquare().setVely(2);
        	 System.out.println("4");
        }

        if(c==KeyEvent.VK_SPACE){
           
        }
    }

    /**
     * Stops the player from moving if they release they movement keys. 
     * however, Player's facing direction will not change
     */
    public void keyReleased(KeyEvent e){
        int c=e.getKeyCode();
        if(c==KeyEvent.VK_RIGHT||c==KeyEvent.VK_D){
        	p.getSquare().setVelx(0);
        }
        if((c==KeyEvent.VK_UP||c==KeyEvent.VK_W)){
        	p.getSquare().setVely(0);
        }
        if(c==KeyEvent.VK_LEFT||c==KeyEvent.VK_A){
        	p.getSquare().setVelx(0);
        }
        if(c==KeyEvent.VK_DOWN||c==KeyEvent.VK_S){
        	p.getSquare().setVely(0);
        }

        if(c==KeyEvent.VK_SPACE){
           
        }
    }
	
}
