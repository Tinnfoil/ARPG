package ARPG;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Button extends Object{

	private String function;
	private int stringwidth;
	public Button(int x, int y, int width, int height, String function){
		super();
		Rectangle rect= new Rectangle(x,y,width,height);
		setRect(rect);
		this.setFunction(function);
    	BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2d = img.createGraphics();
    	FontMetrics fm = g2d.getFontMetrics();
    	setButton((x+(int)fm.stringWidth(getFunction())/2-(int)fm.stringWidth(getFunction())/4), y, (int)fm.stringWidth(getFunction())+(int)fm.stringWidth(getFunction())/2, 20);
    	setStringwidth(fm.stringWidth(getFunction()));
    	g2d.dispose();
	}
	
	public void setButton(int x, int y, int width, int height){
		setRect(x,y,width,height);
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2d = img.createGraphics();
    	FontMetrics fm = g2d.getFontMetrics();
		setStringwidth(fm.stringWidth(getFunction()));
    	g2d.dispose();
	}

	public int getStringwidth() {
		return stringwidth;
	}

	public void setStringwidth(int stringwidth) {
		this.stringwidth = stringwidth;
	}
	
	
}
