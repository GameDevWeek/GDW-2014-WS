package de.hochschuletrier.gdw.ws1415.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;

public class LoadGameState extends BaseGameState {

    private boolean isDone;
    private final AssetManagerX assetManager;
    private final Runnable completeFunc;
    //private final Texture loadScreen = new Texture(Gdx.files.internal("data/images/background_menu.png"));
    private final Texture overlay = new Texture(Gdx.files.internal("data/images/titel.png"));

    public LoadGameState(AssetManagerX assetManager, Runnable completeFunc) {
        this.assetManager = assetManager;
        this.completeFunc = completeFunc;
    }

    public void render() 
    {
    	float drawWidth = Gdx.graphics.getWidth() - 90.0f;
    	float x = (Gdx.graphics.getWidth() - overlay.getWidth())/2;
    	float y = (Gdx.graphics.getWidth() - overlay.getWidth())/2;
    	
    	Color progressUnfillColor = new Color(Color.rgb888(255, 247, 126));
    	Color progressFillColor = new Color(Color.rgb888(255, 239, 1));

    	Main.getInstance().screenCamera.bind();
    	    	    	
    	DrawUtil.fillRect(50, Gdx.graphics.getHeight()/4, drawWidth, 200, progressUnfillColor);
    	DrawUtil.fillRect(50, Gdx.graphics.getHeight()/4, (int) (drawWidth * assetManager.getProgress()), 200, progressFillColor);
    	DrawUtil.draw(overlay, x, y);
    	
    	
    	 /* Main.getInstance().screenCamera.bind();
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLACK);

        float drawWidth = Gdx.graphics.getWidth() - 100.0f;
        DrawUtil.fillRect(50, Gdx.graphics.getHeight() / 2 - 25, (int) (drawWidth * assetManager.getProgress()), 50, Color.GREEN);
        DrawUtil.drawRect(50, Gdx.graphics.getHeight() / 2 - 25, drawWidth, 50, Color.GREEN);*/
    }

    @Override
    public void update(float delta) {
        if (!isDone) {
            assetManager.update();

            if (assetManager.getProgress() == 1) {
                completeFunc.run();
                isDone = true;
            }
        }

        render();
    }

    @Override
    public void dispose() {
    }
}
