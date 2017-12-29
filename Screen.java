package ARPG;

import javax.swing.JFrame;

public class Screen{

	public static void main(String[] args){
        Play g= new Play();
        Input i= new Input(g);
        g.addMouseListener(i);
        JFrame frame= new JFrame();
        //frame.setSize(500,500);
        frame.setSize(1300,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(i);
        frame.add(g);
        frame.setVisible(true);
        g.start();

    }
}
