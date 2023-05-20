package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {
    public static int SONG = 0;
    public static int SWORD = 0;
    public static int DIE = 1;
    private Clip[] songs, effects;
    private int currentSoundId;
    private float volume = 1f;
    private boolean songMute, effectMute;
    private Random rand = new Random();
    public AudioPlayer(){
        loadSongs();
        loadEffects();
        playSong(SONG);
    }
    private void loadSongs() {
        String[] names = {"victory_or_death"};
        songs = new Clip[names.length];
        for (int i = 0; i<songs.length; i++)
            songs[i] = getClip(names[i]);
    }
    private void loadEffects(){
        String [] effectNames = {"sword", "dying"};
        effects = new Clip[effectNames.length];
        for (int i = 0; i< effects.length; i++)
            effects[i] = getClip(effectNames[i]);
        updateEffectsVolume();
    }
    private Clip getClip(String name){
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException |LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void playAttackSound(){
        int start = 0;
        playEffect(start);
    }

    public void setVolume(float volume){
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    public void playEffect(int effect){
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }
    public void playSong(int song){
        if (songs[currentSoundId].isActive())
            songs[currentSoundId].stop();
        currentSoundId = song;
        updateSongVolume();
        songs[currentSoundId].setMicrosecondPosition(0);
        songs[currentSoundId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void toggleSongMute(){
        this.songMute = !songMute;
        for (Clip c: songs){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }
    public void toggleEffectMute(){
        this.effectMute = !effectMute;
        for (Clip c: effects){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute)
            playEffect(SWORD);
    }

    private void updateSongVolume(){
        FloatControl gainControl = (FloatControl) songs[currentSoundId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range*volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }
    private void updateEffectsVolume(){
        for (Clip c: effects){
            FloatControl gainControl = (FloatControl) songs[currentSoundId].getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range*volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
