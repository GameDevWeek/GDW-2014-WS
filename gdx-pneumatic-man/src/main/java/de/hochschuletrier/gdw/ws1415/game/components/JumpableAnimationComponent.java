package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class JumpableAnimationComponent extends Component implements Pool.Poolable 
{
    public AnimationExtended idle;
    public AnimationExtended jump;
    public boolean midair = false;

    @Override
    public void reset() {
        idle = null;
        jump = null;
    }
}
