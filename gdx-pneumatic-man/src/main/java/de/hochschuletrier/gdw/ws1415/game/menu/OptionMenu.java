package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.Settings;

public class OptionMenu extends MenuPage
{	
   private final DecoImage overlay = new DecoImage(assetManager.getTexture("options_overlay"));
	private final DecoImage head = new DecoImage(assetManager.getTexture("optionen_button_active"));
	
	private final DecoImage gamepad = new DecoImage(assetManager.getTexture("gamepad_inactive"));
	private final DecoImage keyboard = new DecoImage(assetManager.getTexture("keyboard_inactive"));
	private final DecoImage gamepadActive = new DecoImage(assetManager.getTexture("gamepad_active"));
	private final DecoImage keyboardActive = new DecoImage(assetManager.getTexture("keyboard_active"));
	
	private final DecoImage soundUpSound = new DecoImage(assetManager.getTexture("soundUp"));
	private final DecoImage soundDownSound = new DecoImage(assetManager.getTexture("soundDown"));
	private final DecoImage soundUpMusic = new DecoImage(assetManager.getTexture("soundUp"));
	private final DecoImage soundDownMusic = new DecoImage(assetManager.getTexture("soundDown"));
	
	private final DecoImage[] soundBox = new DecoImage[10];
	private final DecoImage[] musicBox = new DecoImage[10];
	private final DecoImage[] soundBoxActive = new DecoImage[10];
	private final DecoImage[] musicBoxActive = new DecoImage[10];
	
	private final int y_sound, y_music;
	private final int width, height; 
	private int countVol[] = new int[10];
	private int countSou[] = new int[10];
	
	public OptionMenu(Skin skin, MenuManager menuManager)
	{ 		
		super(skin, "background_menu");
		int x_step = -260;
		y_sound = menuManager.getHeight()/2+55;
		y_music = menuManager.getHeight()/2-115;
		width = menuManager.getWidth();
		height = menuManager.getHeight();
				
		addImage(Main.WINDOW_WIDTH/5+10, Main.WINDOW_HEIGHT/5-15, (int)overlay.getWidth(),(int) overlay.getHeight(), overlay);
		addImage((int)(Main.WINDOW_WIDTH/2 - 320), 750, (int) head.getWidth(), (int) head.getHeight(), head);
		
		for (int i = 0; i < soundBox.length; i ++)
		{
			soundBox[i] = new DecoImage(assetManager.getTexture("slider_inactive"));
			musicBox[i] = new DecoImage(assetManager.getTexture("slider_inactive"));
			
			addImage(menuManager.getWidth()/2 + x_step, y_sound, (int)soundBox[i].getWidth(), (int)soundBox[i].getHeight(), soundBox[i]);
			addImage(menuManager.getWidth()/2 + x_step, y_music, (int)musicBox[i].getWidth(), (int)musicBox[i].getHeight(), musicBox[i]);
			
			countSou[i] = x_step;
			countVol[i] = x_step;

			x_step += (int)(soundBox[0].getWidth() + 5);
		}
		
		addCenteredImage(menuManager.getWidth()/2 - 350, y_sound - 17, (int)soundDownSound.getWidth(), (int)soundDownSound.getHeight(), soundDownSound, this::onSoundVolumeDown);
		addCenteredImage(menuManager.getWidth()/2 + 260, y_sound - 17, (int)soundUpSound.getWidth(), (int)soundUpSound.getHeight(), soundUpSound, this::onSoundVolumeUp);
		
		addCenteredImage(menuManager.getWidth()/2 - 350, y_music - 17, (int)soundDownMusic.getWidth(), (int)soundDownMusic.getHeight(), soundDownMusic, this::onMusicVolumeDown);
		addCenteredImage(menuManager.getWidth()/2 + 260, y_music - 17, (int)soundUpMusic.getWidth(), (int)soundUpMusic.getHeight(), soundUpMusic, this::onMusicVolumeUp);

		int x_step_active_s = -260;
		
		for (int i = 0; i < soundBoxActive.length; i++)
		{
			soundBoxActive[i] = new DecoImage(assetManager.getTexture("slider_active"));
			addImage(menuManager.getWidth()/2 + x_step_active_s, y_sound, (int)soundBox[0].getWidth(), (int)soundBox[0].getHeight(), soundBoxActive[i]);
			x_step_active_s += (int)(soundBox[0].getWidth() + 5);
		}
		
		int x_step_active_m = -260;
		
		for (int i = 0; i < musicBoxActive.length; i++)
		{
			musicBoxActive[i] = new DecoImage(assetManager.getTexture("slider_active"));
			addImage(menuManager.getWidth()/2 + x_step_active_m, y_music, (int)musicBox[0].getWidth(), (int)musicBox[0].getHeight(), musicBoxActive[i]);
			x_step_active_m += (int)(musicBox[0].getWidth() + 5);
		}
		
		addCenteredImage(menuManager.getWidth()/2 - 400, menuManager.getHeight()/2 - 290, (int)gamepad.getWidth(), (int)gamepad.getHeight(), gamepad, this::onGamepadChanged);
		addCenteredImage(menuManager.getWidth()/2 + 50, menuManager.getHeight()/2 - 290, (int)keyboard.getWidth(), (int)keyboard.getHeight(), keyboard, this::onKeyboardChanged);
				
		addImage(width/2 - 400, height/2 - 290, (int)gamepad.getWidth(), (int)gamepad.getHeight(), gamepadActive);
                addImage(width/2 + 50, height/2 - 290, (int)keyboard.getWidth(), (int)keyboard.getHeight(), keyboardActive);
        
   		addCenteredImage(450, 750, 108, 108, new DecoImage(assetManager.getTexture("back_button")), () -> menuManager.popPage());
	}
	
    private void onSoundVolumeUp()
    {
    	if(Settings.SOUND_VOLUME.get() < 1.0)
    	{
    		int act = (int)(Settings.SOUND_VOLUME.get()*10);
    		Settings.SOUND_VOLUME.set((float)(((act+1)/10.0)));
    	}
        restoreSettings();
    }
    
    private void onSoundVolumeDown()
    {    	
    	if(Settings.SOUND_VOLUME.get() > 0)
    	{
    		int act = (int)(Settings.SOUND_VOLUME.get() * 10);
    	    Settings.SOUND_VOLUME.set((float)((act-1)/10.0));
    	}
        restoreSettings();
    	
    }
    
    private void onMusicVolumeUp()
    {
    	if(Settings.MUSIC_VOLUME.get() < 1.0)
    	{
    		int act = (int)(Settings.MUSIC_VOLUME.get()*10);
    		Settings.MUSIC_VOLUME.set((float)(((act+1)/10.0)));
    	}
        restoreSettings();
    }
    
    private void onMusicVolumeDown()
    {
    	if(Settings.MUSIC_VOLUME.get() > 0)
    	{
    		int act = (int)(Settings.MUSIC_VOLUME.get() * 10);
    	    Settings.MUSIC_VOLUME.set((float)((act-1)/10.0));
    	}
        restoreSettings();
    }
    
    private void onGamepadChanged()
    {
    	Settings.GAMEPAD_ENABLED.set(true);
        restoreSettings();
    }
    
    private void onKeyboardChanged()
    {
    	Settings.GAMEPAD_ENABLED.set(false);
        restoreSettings();
    }

    private void storeSettings() 
    {
        Settings.flush();
    }
    
    private void restoreSettings() {
        gamepadActive.setVisible(Settings.GAMEPAD_ENABLED.get());
        keyboardActive.setVisible(!Settings.GAMEPAD_ENABLED.get());

        int act = (int)(Settings.SOUND_VOLUME.get() * 10);
        for (int i = 0; i < soundBoxActive.length; i++) {
            soundBoxActive[i].setVisible(i<act);
        }

        act = (int)(Settings.MUSIC_VOLUME.get() * 10);
        for (int i = 0; i < musicBoxActive.length; i++) {
            musicBoxActive[i].setVisible(i<act);
        }
    }
    
    @Override
    public void setVisible(boolean visible) {
        if (gamepad != null && isVisible() != visible) {
            if (visible) {
                restoreSettings();
            } else {
                storeSettings();
            }
        }
        super.setVisible(visible);
    }
}
