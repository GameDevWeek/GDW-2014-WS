package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimator;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimatorActor;
import de.hochschuletrier.gdw.ws1415.Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreditsMenu extends MenuPage implements SceneAnimator.Getter {
	
//	private Music music;

    private static final Logger logger = LoggerFactory.getLogger(CreditsMenu.class);
    private final Hotkey increaseSpeed = new Hotkey(this::increaseSpeed, Input.Keys.PAGE_UP, HotkeyModifier.CTRL);
    private final Hotkey decreaseSpeed = new Hotkey(this::decreaseSpeed, Input.Keys.PAGE_DOWN, HotkeyModifier.CTRL);
    private final Hotkey resetSpeed = new Hotkey(this::resetSpeed, Input.Keys.HOME, HotkeyModifier.CTRL);
    
    private SceneAnimator sceneAnimator;
    private  Music music;
    private void increaseSpeed() {
        sceneAnimator.setTimeFactor(Math.min(10.0f, sceneAnimator.getTimeFactor() + 0.1f));
    }

    private void decreaseSpeed() {
        sceneAnimator.setTimeFactor(Math.max(0.0f, sceneAnimator.getTimeFactor() - 0.1f));
    }

    private void resetSpeed() {
        sceneAnimator.setTimeFactor(1.0f);
    }

    public CreditsMenu(Skin skin, MenuManager menuManager) {
        super(skin, "background_menu");
        //addActor(new DecoImage(assetManager.getTexture("credits_overlay")));
       
       // MusicManager.update();
        try {
            sceneAnimator = new SceneAnimator(this, "data/json/credits.json");
            addActor(new SceneAnimatorActor(sceneAnimator));

            // If this is a build jar file, disable hotkeys
            if (!Main.IS_RELEASE) {
                increaseSpeed.register();
                decreaseSpeed.register();
                resetSpeed.register();
            }
        } catch (Exception ex) {
            logger.error("Error loading credits", ex);
        }
        
        
        addCenteredImage(450, 750, 108, 108, new DecoImage(assetManager.getTexture("back_button")), () -> menuManager.popPage());
    }

    @Override
    public BitmapFont getFont(String name) {
        return skin.getFont(name);
    }

    @Override
    public AnimationExtended getAnimation(String name) {
        return assetManager.getAnimation(name);
    }

    @Override
    public Texture getTexture(String name) {
        return assetManager.getTexture(name);
    }

    @Override
    public void setVisible(boolean visible) {
        // TODO Auto-generated method stub
        super.setVisible(visible);
        if(visible){
            music = assetManager.getMusic("credits");
            MusicManager.play(music, 2.0f);
        }else{
            music = assetManager.getMusic("menu");
            MusicManager.play(music, 2.0f); 
        }
    }

    
    
//Abspielen, wenn visible -> setVisible überschreiben    
//    music = assetManager.getMusic("menu");
//    MusicManager.play(music, 2.0f);
}
