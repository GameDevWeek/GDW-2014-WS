package de.hochschuletrier.gdw.ws1415.states;

import java.awt.Toolkit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;

public class LoadGameState extends BaseGameState {

    private boolean isDone;
    private final AssetManagerX assetManager;
    private final Runnable completeFunc;
    private final Texture overlay = new Texture(Gdx.files.internal("data/images/titel.png"));

    public LoadGameState(AssetManagerX assetManager, Runnable completeFunc) {
        this.assetManager = assetManager;
        this.completeFunc = completeFunc;
    }

    public void render() 
    {
    	Color progressUnfillColor = new Color(Color.rgb888(255, 247, 126));
    	Color progressFillColor = new Color(Color.rgb888(255, 239, 1));

    	Main.getInstance().screenCamera.bind();
        
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();
        Vector2 scaled = Scaling.fit.apply(overlay.getWidth(), overlay.getHeight(), width, height);
        final float left = (width - scaled.x)/2;
        final float top = (height - scaled.y)/2;
        final float barLeft = left + scaled.x*0.11f;
        final float barTop = top + scaled.y * 0.3f;
        final float barWidth = scaled.x * 0.78f;
        final float barHeight = scaled.y * 0.6f;
        
    	DrawUtil.fillRect(barLeft, barTop, barWidth, barHeight, progressUnfillColor);
    	DrawUtil.fillRect(barLeft, barTop, (int) (barWidth * assetManager.getProgress()), barHeight, progressFillColor);
    	DrawUtil.draw(overlay, left, top, scaled.x, scaled.y);
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
