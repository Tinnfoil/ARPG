package ARPG;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Render extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
     int x;
     int y;
     int width;
     int height;
	
	public Render(Play a){
		x=a.getSquare().getX();
		y=a.getSquare().getY();
		width=a.getSquare().getWidth();
		height=a.getSquare().getHeight();
	}
	
	public void render(Square s){
		x=s.getX();
		y=s.getY();
		width=s.getWidth();
		height=s.getHeight();
		repaint();
	}
	
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        g.setColor(Color.BLACK);
        try{
            g.drawRect(x,y,width,height);
        }
        catch(Exception e){
        }
        
    }

	
}
