package ARPG;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image extends Object{
	private BufferedImage image;
	private int lifetime;
	private String name;
	public Image(int x, int y, String name, int lifetime){
		setX(x);
		setY(y);
		Rectangle rect= new Rectangle(x,y,1,1);
		setRect(rect);
		this.name=name;
		this.lifetime=lifetime;
		try {
			image = ImageIO.read(new File("C:\\Users\\Kenny\\stuff\\GameCol\\Image\\"+name+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public int getLifetime() {
		return lifetime;
	}
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
