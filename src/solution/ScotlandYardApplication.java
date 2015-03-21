package solution;

import scotlandyard.*;

import javax.sound.sampled.*;
import java.io.File;

class ScotlandYardApplication
{
	public static void main(String args[])
	{
		playMusic("../resources/audio.wav");
		WelcomeView view = new WelcomeView();
		WelcomePresenter presenter = new WelcomePresenter(view);
		view.run();	
	}

	//plays soundtrack in continuous loop
	public static synchronized void playMusic(final String url) {
  		new Thread(new Runnable() {
 	
   		 public void run() {
     			 try {
     			 	File file = new File(url);
       				Clip clip = AudioSystem.getClip();
       				AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
         			
        			clip.open(inputStream);
        			clip.loop(Clip.LOOP_CONTINUOUSLY); 
      			} catch (Exception e) {
       			 	System.err.println(e.getMessage());
      			}
    		}
  		}).start();
	}

	
}
