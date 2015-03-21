package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PositionComponent extends Component implements Pool.Poolable {

    public float x;
    public float y;
    public float rotation;
    public float scaleX = 1.f, scaleY = 1.f;
    
    @Override
    public void reset() {
        rotation = x = y = 0;
        scaleX = scaleY = 1.f;
    }
}
