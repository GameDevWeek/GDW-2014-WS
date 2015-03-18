package de.hochschuletrier.gdw.ss14.sandbox.credits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class Credits extends SandboxGame implements SceneAnimator.Getter {

    private static final Logger logger = LoggerFactory.getLogger(Credits.class);
    private final Hotkey increaseSpeed = new Hotkey(this::increaseSpeed, Input.Keys.PAGE_UP, HotkeyModifier.CTRL);
    private final Hotkey decreaseSpeed = new Hotkey(this::decreaseSpeed, Input.Keys.PAGE_DOWN, HotkeyModifier.CTRL);
    private final Hotkey resetSpeed = new Hotkey(this::resetSpeed, Input.Keys.HOME, HotkeyModifier.CTRL);
    
    private SceneAnimator sceneAnimator;
    private AssetManagerX assetManager;
    
    public Credits() {
    }
    
    private void increaseSpeed() {
        sceneAnimator.setTimeFactor(Math.min(10.0f, sceneAnimator.getTimeFactor() + 0.1f));
    }
    
    private void decreaseSpeed() {
        sceneAnimator.setTimeFactor(Math.max(0.0f, sceneAnimator.getTimeFactor() - 0.1f));
    }
    
    private void resetSpeed() {
        sceneAnimator.setTimeFactor(1.0f);
    }

    @Override
    public void init(AssetManagerX assetManager) {
        this.assetManager = assetManager;
        try {
            sceneAnimator = new SceneAnimator(this, "data/json/credits.json");
            
            // If this is a build jar file, disable hotkeys
            if (!Main.IS_RELEASE) {
                increaseSpeed.register();
                decreaseSpeed.register();
                resetSpeed.register();
            }
        } catch (Exception ex) {
            logger.error("Error loading credits", ex);
        }
    }


    @Override
    public BitmapFont getFont(String name) {
        return assetManager.getFont(name);
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
    public void dispose() {
    }

    @Override
    public void update(float delta) {
        if(sceneAnimator != null)
            sceneAnimator.update(delta);
        
        Main.getInstance().screenCamera.bind();
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.DARK_GRAY);
        if(sceneAnimator != null)
            sceneAnimator.render();
    }
}