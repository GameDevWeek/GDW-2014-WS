package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HealthComponent extends Component implements Poolable {

    public enum HealthState
    public int DecrementByValueNextFrame = 0;
    {
        DEAD, DYING, ALIVE1, ALIVE2 ;
    }
    
    public HealthState health = HealthState.ALIVE2;

    @Override
    public void reset() {
        Value = 0;
        health = HealthState.DEAD;
        DecrementByValueNextFrame = 0;
    }

}
