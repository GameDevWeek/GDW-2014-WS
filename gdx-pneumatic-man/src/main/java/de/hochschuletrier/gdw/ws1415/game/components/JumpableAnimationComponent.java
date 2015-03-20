package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class JumpableAnimationComponent extends Component implements Pool.Poolable 
{
    public AnimationExtended idle;
    public float idleStateTime = 0;
    public AnimationExtended jump;
    public float jumpStateTime = 0;
    public boolean midair = false;
    public float pastFrameYVelocity = 0;

    @Override
    public void reset() {
        idle = null;
        jump = null;
    }
}
