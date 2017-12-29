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
     * Stops the Player from moving if they release they movement keys. 
     * however, Player's facing direction will not change
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

	public void mouseClicked(MouseEvent e) {// projectile lifetime, multikey pressing, cooldowns
		//System.out.println("X:"+m.getX()+" Y:"+m.getY());
		int x1 = p.getSquare().getX() + p.getSquare().getWidth() / 2;
		int y1 = p.getSquare().getY() + p.getSquare().getHeight() / 2;
		int x2 = e.getX() + p.getSquare().getX() - p.camxoff;
		int y2 = e.getY() + p.getSquare().getY() - p.camyoff;
		p.getHandler().addInput(x1+"+"+y1+"+"+x2+"+"+y2);
		p.spawnProjectile(x1,y1,x2,y2);
		
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
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
