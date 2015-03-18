package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class AnimationComponent extends Component implements Pool.Poolable {

    public boolean IsActive = true;
    public AnimationExtended animation;
    public float stateTime;
    public boolean animationFinished;

    @Override
    public void reset() {
        IsActive = true;
        animation = null;
        stateTime = 0;
        animationFinished = false;
    }
}
