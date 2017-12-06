package ARPG;

import javax.swing.JFrame;

public class Screen{

	public static void main(String[] args){
        play g= new play();
        Input i= new Input(g);
        JFrame frame= new JFrame();
        frame.setSize(700,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(i);
        frame.add(g);
        frame.setVisible(true);
        g.start();

    }
}
