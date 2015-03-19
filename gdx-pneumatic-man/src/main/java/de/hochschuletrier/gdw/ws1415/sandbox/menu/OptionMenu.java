package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ws1415.Settings;

public class OptionMenu extends MenuPage
{	
	private final Label soundLabel, musicLabel;
    private final Slider soundSlider, musicSlider;
    private final ButtonGroup<TextButton> buttonGroup = new ButtonGroup<TextButton>();
    private final TextButton gamepadButton, keyboardButton;
   // private final TextButton soundMuteButton, musicMuteButton;
	
	public OptionMenu(Skin skin, MenuManager menuManager)
	{ 		
		super(skin, "background_menu");
		int y = 600;
		
		createLabel(menuManager.getWidth()/2-75, y).setText("SOUND");
		y-= 20;
        soundSlider = createSlider(menuManager.getWidth()/2-160, y, this::onSoundVolumeChanged);
        //soundSlider.setValue(1);
        soundLabel = createLabel(menuManager.getWidth()/2+190, y);
        soundLabel.setVisible(false);
       // soundMuteButton = createToggleButton(menuManager.getWidth()/2+250, y, "Aus", this::onSoundMuteChanged);
        //soundMuteButton.setVisible(false);
        y -= 50;

        createLabel(menuManager.getWidth()/2-75, y).setText("MUSIC");
        y -= 20;
        musicSlider = createSlider(menuManager.getWidth()/2-160, y, this::onMusicVolumeChanged);
        //musicSlider.setValue(1);
        musicLabel = createLabel(menuManager.getWidth()/2+190, y);
        musicLabel.setVisible(false);
       // musicMuteButton = createToggleButton(menuManager.getWidth()/2+250, y, "Aus", this::onMusicMuteChanged);
       // musicMuteButton.setVisible(false);
       // y -= 50;
        
       // createLabel(menuManager.getWidth()/2-150, y).setText("GAMEPAD / KEYBOARD");
        y -= 50;
        gamepadButton = addButton(menuManager.getWidth()/2-150, y, 100, 50, "GAMEPAD", this::onSoundVolumeChanged, "toggle");
        keyboardButton = addButton(menuManager.getWidth()/2-40, y, 100, 50, "KEYBOARD", this::onSoundVolumeChanged, "toggle");
        
        buttonGroup.add(gamepadButton);
        buttonGroup.add(keyboardButton);
        buttonGroup.setChecked("keyboardButton");	
		
		addCenteredButton(450, 850, 200, 200, "<", () -> menuManager.popPage());
	}
	
	private Label createLabel(int x, int y)
	{
	    Label label = new Label("", skin);
	    label.setBounds(x, y, 60, 30);
	    addActor(label);
	    return label;
	}
	
	private Slider createSlider(int x, int y, Runnable runnable) 
	{
        Slider slider = new Slider(0, 1, 0.01f, false, skin);
        slider.setBounds(x, y, 200, 30);
        addActor(slider);
        slider.addListener(new ChangeListener() 
        {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) 
            {
                runnable.run();
            }
        });
        return slider;
    }
	
	public final String pctToString(float value) 
	{
        int pct = (int) (100 * value);
        return pct + "%";
    }

    private void onSoundVolumeChanged() 
    {
        final float value = soundSlider.getValue();
        soundLabel.setText(pctToString(value));
        SoundEmitter.setGlobalVolume(value);
    }

    private void onMusicVolumeChanged() 
    {
        final float value = musicSlider.getValue();
        //System.out.println("Test");
        musicLabel.setText(pctToString(value));
        MusicManager.setGlobalVolume(value);
    }

//    private void onSoundMuteChanged() 
//    {
//        boolean soundOn = soundMuteButton.isChecked();
//        soundMuteButton.setText(soundOn ? "An" : "Aus");
//        SoundEmitter.setMuted(!soundOn);
//    }
//
//    private void onMusicMuteChanged() 
//    {
//        boolean musicOn = musicMuteButton.isChecked();
//        musicMuteButton.setText(musicOn ? "An" : "Aus");
//        MusicManager.setMuted(!musicOn);
//    }

    @Override
    public void setVisible(boolean visible) 
    {
        if (soundSlider != null && isVisible() != visible) 
        {
            if (visible) 
            {
                restoreSettings();
            } 
            else 
            {
                storeSettings();
            }
        }
        super.setVisible(visible);
    }

    private void restoreSettings() 
    {
    	System.out.println("Restore");
        soundSlider.setValue(Settings.SOUND_VOLUME.get());
       // soundMuteButton.setChecked(!Settings.SOUND_MUTE.get());
        musicSlider.setValue(Settings.MUSIC_VOLUME.get());
       // musicMuteButton.setChecked(!Settings.MUSIC_MUTE.get());
        //fullscreenButton.setChecked(Gdx.graphics.isFullscreen());
    }

    private void storeSettings() 
    {
    	System.out.println("Store");
        Settings.SOUND_VOLUME.set(soundSlider.getValue());
       // Settings.SOUND_MUTE.set(!soundMuteButton.isChecked());
        Settings.MUSIC_VOLUME.set(musicSlider.getValue());
      //  Settings.MUSIC_MUTE.set(!musicMuteButton.isChecked());
        //Settings.FULLSCREEN.set(Gdx.graphics.isFullscreen());
        Settings.flush();
    }
    
    private TextButton createToggleButton(int x, int y, String text, Runnable runnable) 
    {
        TextButton button = new TextButton(text, skin, "toggle");
        button.setBounds(x, y, 100, 30);
        addActor(button);

        button.addListener(new ChangeListener() 
        {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) 
            {
                runnable.run();
            }
        });
        return button;
    }
}
