package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

/**
 * 
 * Use for anything that is supposed to be animated in the game. <br>
 * If it's not animated use TextureComponent. <br>
 * The animation can be easily set with the global available AssetManagerX class. <br>
 * Usage example: assetManager.getAnimation("walking") <br>
 * Make sure the animation is <b>not null</b>. Otherwise you will get an assertion error or a NullPointerException. <br>
 * <br>
 * The animations can be found in the resources/animations folder. They should be set/changed in the available json file and loaded
 * at the start of the game.
 */
public class AnimationComponent extends Component implements Pool.Poolable {

    public boolean flipX = false;
    public boolean flipY = false;
    public boolean IsActive = true;
    public boolean isDyingPlayer = false;
    public boolean isSpawningPlayer = false;
    public AnimationExtended animation;
    public float stateTime;
    public float permanent_stateTime;
    public boolean animationFinished;
    public float offsetX = 0.0f;

    @Override
    public void reset() {
        flipY = false;
        IsActive = true;
        animation = null;
        stateTime = 0;
        permanent_stateTime = 0;
        animationFinished = false;
    }
}
