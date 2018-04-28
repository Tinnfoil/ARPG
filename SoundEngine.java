package ARPG;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.*;
import java.util.*;
/**
 * Write a description of class SoundEngine2 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SoundEngine
{
    private static double modifier;
    private static Clip bgm, warped;
    private static boolean warping;
    private static boolean bgmplaying, frozen;
    private static ArrayList<Effect> ongoing;
    Play p;
    /**
     * name1 = normal track name
     * name2 = warped or duplicate track
     * input = warped speed/multiplier 
     */
    public SoundEngine(String name1, String name2, double input, Play p) throws Exception
    {
    	ongoing=new ArrayList<Effect>();
    	this.p=p;
        try{
        	//System.out.println("SUP");
            File location = new File("C:\\Users\\Kenny\\stuff\\GameCol\\Sound\\" + name1 + ".wav");
            AudioInputStream sound = AudioSystem.getAudioInputStream(location);
            bgm = AudioSystem.getClip();
            bgm.open(sound);
            bgm.setFramePosition(0);
    
            location = new File("C:\\Users\\Kenny\\stuff\\GameCol\\Sound\\" + name2 + ".wav");
            sound = AudioSystem.getAudioInputStream(location);
            warped = AudioSystem.getClip();
            warped.open(sound);
            warped.setFramePosition(0);
    
            modifier = input;
        }
        catch(Exception e){}
    }
    
    public boolean frozen(){
    	return frozen;
    }
    

    /**
     * after initializing the object, call this to start playing the sounds
     */
    public void startUp() throws Exception
    {
        try{
            bgm.loop(Integer.MAX_VALUE);
            FloatControl gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);
            warped.loop(Integer.MAX_VALUE);
            FloatControl gainControl2 = (FloatControl) warped.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl2.setValue(-10.0f);
            warped.stop();
            bgmplaying = true;
        }
        catch(Exception e){}
    }
    
    public void changeVolume(double decibels){
    	float f= (float)decibels;
    	FloatControl gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(f);
    	FloatControl gainControl2 = (FloatControl) warped.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl2.setValue(f);
    }

    /**
     * to switch between tracks, it will automatically stop and start where
     * the other leaves off
     * 
     * also will not play if the game is "frozen"
     */
    public void warp() throws Exception
    {
        try{
            int point;
                if(bgm.getFrameLength()!=warped.getFrameLength())
                {
                    if(bgmplaying)
                    {
                        bgm.stop();
                        point = (int)(bgm.getFramePosition()*modifier);
                        warped.setFramePosition(point);
                        warped.loop(Integer.MAX_VALUE);
                        bgmplaying=false;
                    }
                    else
                    {
                        warped.stop();
                        point = (int)(warped.getFramePosition()/modifier);
                        bgm.setFramePosition(point);//replacement later
                        bgm.loop(Integer.MAX_VALUE);
                        bgmplaying=true;
                    }
                }
        }
        catch(Exception e){}
    }

    /**
     * pauses whatever is currently playing except effects and locks warp()
     */
    public void freeze() throws Exception
    {
        try{
            
            bgm.stop();
            
            warped.stop();
            frozen = true;
        }
        catch(Exception e){}
    }

    /**
     * unpauses the background music back to where it left off
     */
    public void unfreeze() throws Exception
    {
        try{
            if(bgmplaying)
            {bgm.loop(Integer.MAX_VALUE);}
            else
            {warped.loop(Integer.MAX_VALUE);}
            frozen = false;
        }
        catch(Exception e){}
    }

    /**
     * to change tracks without initializing a new object, same variable names
     */
    public void changeTrack(String name1, String name2, double input) throws Exception
    {
        try{
            shutdown();
            File location = new File("C:\\Users\\Kenny\\stuff\\GameCol\\Sound\\" + name1 + ".wav");
            AudioInputStream sound = AudioSystem.getAudioInputStream(location);
            bgm = AudioSystem.getClip();
            bgm.open(sound);
            bgm.setFramePosition(0);

            location = new File("C:\\Users\\Kenny\\stuff\\GameCol\\Sound\\" + name2 + ".wav");
            sound = AudioSystem.getAudioInputStream(location);
            warped = AudioSystem.getClip();
            warped.open(sound);
            warped.setFramePosition(0);

            modifier = input;

            frozen = false;

            startUp();
        }
        catch(Exception e){}
    }

    /**
     * to play an effect, name = file name
     */
    public void playeffect(String name) throws Exception
    {
    		ongoing.add(new Effect(name));
    }

    /**
     * checks if any effects have finished, in case that they do, it deletes
     * the object(thread) 
     * 
     * ***should be called at every frame to be safe***
     */
    public void checkDone()
    {
        for(int i = 0; i<ongoing.size(); i++)
        {
            boolean temp = ongoing.get(i).getDone();
            if(temp)
            {
                ongoing.remove(i);
                i--;
            }
        }
    }

    /**
     * closes the open threads for the two bgm tracks, should be called 
     * whenever someone presses p so that the files get closed before the
     * ne
     */
    public void shutdown()
    {
        try{
            bgm.close();
            warped.close();
        }
        catch(Exception e){}
    }
    
    public void muteEffects(){
        for(int i = 0; i<ongoing.size(); i++){
        	ongoing.get(i).changeVolume(-50);
        }
    }

	public static boolean warping() {
		return warping;
	}

	public static void setWarping(boolean warping) {
		SoundEngine.warping = warping;
	}
}
