package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.ScreenUtil;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Settings;

public class OptionMenu extends MenuPage
{	
	private final Label soundLabel, musicLabel;
    private final Slider soundSlider, musicSlider;
    private final ButtonGroup<TextButton> buttonGroup = new ButtonGroup<TextButton>();
    private final TextButton gamepadButton, keyboardButton, fullscreenButton;
   // private final TextButton soundMuteButton, musicMuteButton;
	
	public OptionMenu(Skin skin, MenuManager menuManager)
	{ 		
		super(skin, "background_menu");
		int y = 600;
		
		createLabel(menuManager.getWidth()/2-75, y).setText("SOUND");
		y-= 20;
        soundSlider = createSlider(menuManager.getWidth()/2-160, y, this::onSoundVolumeChanged);
        soundSlider.setValue(Settings.SOUND_VOLUME.get());
        soundLabel = createLabel(menuManager.getWidth()/2+190, y);
        soundLabel.setVisible(false);
       // soundMuteButton = createToggleButton(menuManager.getWidth()/2+250, y, "Aus", this::onSoundMuteChanged);
        //soundMuteButton.setVisible(false);
        y -= 50;

        createLabel(menuManager.getWidth()/2-75, y).setText("MUSIC");
        y -= 20;
        musicSlider = createSlider(menuManager.getWidth()/2-160, y, this::onMusicVolumeChanged);
        musicSlider.setValue(Settings.MUSIC_VOLUME.get());
        musicLabel = createLabel(menuManager.getWidth()/2+190, y);
        musicLabel.setVisible(false);
       // musicMuteButton = createToggleButton(menuManager.getWidth()/2+250, y, "Aus", this::onMusicMuteChanged);
       // musicMuteButton.setVisible(false);
       // y -= 50;
        
        //DrawUtil.fillRect(menuManager.getWidth()/2-160, y, Gdx.graphics.getWidth() - 190.0f, Color.WHITE);
//        DrawUtil.fillRect(50, Gdx.graphics.getHeight()/4, drawWidth, 200, progressUnfillColor);
//    	DrawUtil.fillRect(50, Gdx.graphics.getHeight()/4, (int) (drawWidth * assetManager.getProgress()), 200, progressFillColor);
        
       // createLabel(menuManager.getWidth()/2-150, y).setText("GAMEPAD / KEYBOARD");
        y -= 75;
        gamepadButton = addButton(menuManager.getWidth()/2-160, y, 100, 50, "GAMEPAD", this::onGamepadChanged, "toggle");
        keyboardButton = addButton(menuManager.getWidth()/2-50, y, 100, 50, "KEYBOARD", this::onKeyboardChanged, "toggle");
        y -= 75;
        
        fullscreenButton = addButton(menuManager.getWidth()/2-160, y, 100, 50, "FULLSCREEN", this::onFullscreenChanged, "toggle");
        
        buttonGroup.add(gamepadButton);
        buttonGroup.add(keyboardButton);
        buttonGroup.setChecked("KEYBOARD");
        
   		addCenteredImage(450, 750, 108, 108, new DecoImage(assetManager.getTexture("back_button")), () -> menuManager.popPage());
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
        //soundLabel.setText(pctToString(value));
        SoundEmitter.setGlobalVolume(value);
    }

    private void onMusicVolumeChanged() 
    {
        final float value = musicSlider.getValue();
        //System.out.println("Test");
        //musicLabel.setText(pctToString(value));
        MusicManager.setGlobalVolume(value);
    }
    
    private void onGamepadChanged()
    {
    	Settings.GAMEPAD_ENABLED.set(true);
    	//System.out.println("Gamepad");
    }
    
    private void onKeyboardChanged()
    {
    	Settings.GAMEPAD_ENABLED.set(false);
    	//System.out.println("Keyboard");
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
    	//System.out.println("Restore");
        soundSlider.setValue(Settings.SOUND_VOLUME.get());
       // soundMuteButton.setChecked(!Settings.SOUND_MUTE.get());
        musicSlider.setValue(Settings.MUSIC_VOLUME.get());
       // musicMuteButton.setChecked(!Settings.MUSIC_MUTE.get());
        //fullscreenButton.setChecked(Gdx.graphics.isFullscreen());
    }

    private void storeSettings() 
    {
    	//System.out.println("Store");
        Settings.SOUND_VOLUME.set(soundSlider.getValue());
       // Settings.SOUND_MUTE.set(!soundMuteButton.isChecked());
        Settings.MUSIC_VOLUME.set(musicSlider.getValue());
      //  Settings.MUSIC_MUTE.set(!musicMuteButton.isChecked());
        //Settings.FULLSCREEN.set(Gdx.graphics.isFullscreen());
        Settings.flush();
    }
    
    private void onFullscreenChanged() {
        boolean fullscreenOn = fullscreenButton.isChecked();
        fullscreenButton.setText(fullscreenOn ? "An" : "Aus");

        ScreenUtil.setFullscreen(fullscreenOn);
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
