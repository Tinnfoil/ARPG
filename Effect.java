package ARPG;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.*;

/**
 * Write a description of class Effect here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Effect
{
    private boolean done;
    private Clip bgm;
    /**
     * name of file is passed in
     * 
     * it should do its thing and then await deletion by itself
     */
    public Effect(String name) throws Exception 
    {
        try{
            done = false;
            File location = new File("C:\\Users\\Kenny\\stuff\\GameCol\\Sound\\" + name + ".wav");
            AudioInputStream sound = AudioSystem.getAudioInputStream(location);
            bgm = AudioSystem.getClip();
            bgm.open(sound);
            bgm.setFramePosition(0);
            FloatControl gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);
            bgm.start();
            //Thread.sleep(bgm.getMicrosecondLength()*1000);
           // bgm.close();
           // done = true;
        }
        catch(Exception e){}
    }
    
    public void end(){
    	bgm.close();
    }

    public void changeVolume(double decibels){
    	try{
    		float f= (float)decibels;
    		FloatControl gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
    		gainControl.setValue(f);
    	}
    	catch(Exception e){
    		
    	}
    }
    /**
     * returns if the effect is done playing
     */
    public boolean getDone()
    {
        return done;
    }

}
