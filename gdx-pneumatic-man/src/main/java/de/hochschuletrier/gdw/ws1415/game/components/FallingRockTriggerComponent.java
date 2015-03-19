package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by oliver on 18.03.15.
 */
public class FallingRockTriggerComponent extends Component implements Pool.Poolable{
    public Entity rockEntity;


    @Override
    public void reset() {
        rockEntity = null;
    }
}
