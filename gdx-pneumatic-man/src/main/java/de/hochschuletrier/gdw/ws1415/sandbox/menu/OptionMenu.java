package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class OptionMenu extends MenuPage
{	
	//Musik/Sound laut/leise
	//Musik/Sound mute
	//Gamepad/Keyboard
	
	private final Label soundLabel, musicLabel;
    private final Slider soundSlider, musicSlider;
    private final TextButton soundMuteButton, musicMuteButton;
	
	public OptionMenu(Skin skin, MenuManager menuManager)
	{ 		
		super(skin, "logo");
		int y = 400;
		
		createLabel(100, y).setText("Sound:");
        soundSlider = createSlider(170, y, this::onSoundVolumeChanged);
        soundLabel = createLabel(380, y);
        soundMuteButton = createToggleButton(440, y, "Aus", this::onSoundMuteChanged);
        y -= 50;

        createLabel(100, y).setText("Musik:");
        musicSlider = createSlider(170, y, this::onMusicVolumeChanged);
        musicLabel = createLabel(380, y);
        musicMuteButton = createToggleButton(440, y, "Aus", this::onMusicMuteChanged);
        y -= 50;
        
        createLabel(100, y).setText("Gamepad/Keyboard");
       // createToggleButton(x, y, text, runnable)
       // createToggleButton(x, y, text, runnable)
		
		addCenteredButton(menuManager.getWidth() - 100, 54, 100, 40, "RETURN", () -> menuManager.popPage());
	}
	
	private Label createLabel(int x, int y)
	{
	    Label label = new Label("", skin);
	    label.setBounds(x, y, 60, 30);
	    addActor(label);
	    return label;
	}
	
	private Slider createSlider(int x, int y, Runnable runnable) {
        Slider slider = new Slider(0, 1, 0.01f, false, skin);
        slider.setBounds(x, y, 200, 30);
        addActor(slider);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                runnable.run();
            }
        });
        return slider;
    }
	
	public final String pctToString(float value) {
        int pct = (int) (100 * value);
        return pct + "%";
    }

    private void onSoundVolumeChanged() {
        final float value = soundSlider.getValue();
        soundLabel.setText(pctToString(value));
        SoundEmitter.setGlobalVolume(value);
    }

    private void onMusicVolumeChanged() {
        final float value = musicSlider.getValue();
        musicLabel.setText(pctToString(value));
        MusicManager.setGlobalVolume(value);
    }

    private void onSoundMuteChanged() {
        boolean soundOn = soundMuteButton.isChecked();
        soundMuteButton.setText(soundOn ? "An" : "Aus");
        SoundEmitter.setMuted(!soundOn);
    }

    private void onMusicMuteChanged() {
        boolean musicOn = musicMuteButton.isChecked();
        musicMuteButton.setText(musicOn ? "An" : "Aus");
        MusicManager.setMuted(!musicOn);
    }

    @Override
    public void setVisible(boolean visible) {
        if (soundSlider != null && isVisible() != visible) {
            if (visible) {
                restoreSettings();
            } else {
                storeSettings();
            }
        }
        super.setVisible(visible);
    }

    private void restoreSettings() {
//        soundSlider.setValue(Settings.SOUND_VOLUME.get());
//        soundMuteButton.setChecked(!Settings.SOUND_MUTE.get());
//        musicSlider.setValue(Settings.MUSIC_VOLUME.get());
//        musicMuteButton.setChecked(!Settings.MUSIC_MUTE.get());
//        fullscreenButton.setChecked(Gdx.graphics.isFullscreen());
    }
//
    private void storeSettings() {
 //       Settings.SOUND_VOLUME.set(soundSlider.getValue());
//        Settings.SOUND_MUTE.set(!soundMuteButton.isChecked());
//        Settings.MUSIC_VOLUME.set(musicSlider.getValue());
//        Settings.MUSIC_MUTE.set(!musicMuteButton.isChecked());
//        Settings.FULLSCREEN.set(Gdx.graphics.isFullscreen());
//        Settings.flush();
    }
    
    private TextButton createToggleButton(int x, int y, String text, Runnable runnable) {
        TextButton button = new TextButton(text, skin, "toggle");
        button.setBounds(x, y, 100, 30);
        addActor(button);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                runnable.run();
            }
        });
        return button;
    }
    
}
