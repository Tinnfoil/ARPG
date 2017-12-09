package ARPG;

import javax.swing.*;

import java.awt.Point;
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
       	 	p.getHandler().readList();
        }
        if((c==KeyEvent.VK_UP||c==KeyEvent.VK_W)){
        	p.getHandler().addInput("UP");
        	p.getHandler().readList();
        }
        if(c==KeyEvent.VK_LEFT||c==KeyEvent.VK_A){
        	p.getHandler().addInput("LEFT");
        	p.getHandler().readList();
        }
        if(c==KeyEvent.VK_DOWN||c==KeyEvent.VK_S){
        	p.getHandler().addInput("DOWN");
        	p.getHandler().readList();
        }

        if(c==KeyEvent.VK_SPACE){
           
        }
    }

    /**
     * Stops the Player from moving if they release they movement keys. 
     * however, Player's facing direction will not change
     */
    public void keyReleased(KeyEvent e){
        int c=e.getKeyCode();
        if(c==KeyEvent.VK_RIGHT||c==KeyEvent.VK_D){
        	p.getHandler().removeInput("RIGHT");
        	p.getHandler().readList();
        }
        if((c==KeyEvent.VK_UP||c==KeyEvent.VK_W)){
        	p.getHandler().removeInput("UP");
        	p.getHandler().readList();
        }
        if(c==KeyEvent.VK_LEFT||c==KeyEvent.VK_A){
        	p.getHandler().removeInput("LEFT");
        	p.getHandler().readList();
        }
        if(c==KeyEvent.VK_DOWN||c==KeyEvent.VK_S){
        	p.getHandler().removeInput("DOWN");
        	p.getHandler().readList();
        }
        if(c==KeyEvent.VK_SPACE){
           
        }
    }

	public void mouseClicked(MouseEvent e) {
		//System.out.println("Clicked");
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Clicked");
		Point p=e.getPoint();		
		System.out.println("X:"+p.getX()+" Y:"+p.getY());
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
