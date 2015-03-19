package de.hochschuletrier.gdw.ws1415.sandbox.menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxGame;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CreditsMenu extends SandboxGame implements SceneAnimator.Getter {

    public CreditsMenu(Skin skin, String background)
    {
    }

    private static final Logger logger = LoggerFactory.getLogger(CreditsMenu.class);
    
    private SceneAnimator sceneAnimator;
    private AssetManagerX assetManager;
    

    @Override
    public void init(AssetManagerX assetManager) {
        this.assetManager = assetManager;
        try {
            sceneAnimator = new SceneAnimator(this, "data/json/credits.json");
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