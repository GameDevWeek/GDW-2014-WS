package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HealthComponent extends Component implements Poolable {

    public int Value;
    public int DecrementByValueNextFrame = 0;

    public enum HealthState {
        DEAD, DYING, ALIVE;
    }

    public HealthState health = HealthState.ALIVE;

    @Override
    public void reset() {
        Value = 0;
        health = HealthState.DEAD;
        DecrementByValueNextFrame = 0;
    }

}
