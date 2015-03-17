package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HealthComponent extends Component implements Poolable {

    public int Value;
    public int DecrementByValueNextFrame = 0;

    @Override
    public void reset() {
        Value = 0;
        DecrementByValueNextFrame = 0;
    }

}
