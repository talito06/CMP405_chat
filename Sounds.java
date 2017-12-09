import javax.sound.sampled.AudioFormat;

import javax.sound.sampled.AudioInputStream;

import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.Clip;



// Sound Class

public class Sounds {


//data structure that holds the audio

private Clip clip;


// sound constructor

public Sounds(String s) {;

try {

AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(s));

AudioFormat baseFormat = ais.getFormat();

AudioFormat decodeFormat = new AudioFormat(

AudioFormat.Encoding.PCM_SIGNED,

baseFormat.getSampleRate(),

16,

baseFormat.getChannels(),

baseFormat.getChannels() * 2,

baseFormat.getSampleRate(),

false


);


AudioInputStream dais =

AudioSystem.getAudioInputStream(

decodeFormat, ais);

clip = AudioSystem.getClip();

clip.open(dais);

}


catch (Exception e){

e.printStackTrace();

}

}


// Method that plays sound

public void play(){

if(clip == null) return;

stop();

clip.setFramePosition(0);

clip.start();

}


// Method that stops the sound

public void stop(){

if (clip.isRunning()) clip.stop();

}

}