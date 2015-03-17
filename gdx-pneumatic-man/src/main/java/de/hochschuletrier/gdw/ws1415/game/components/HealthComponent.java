package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HealthComponent extends Component implements Poolable {

    public enum HealthState
    {
        DEAD, DYING, ALIVE1, ALIVE2 ;
    }
    
    public HealthState Value = HealthState.ALIVE2;

    @Override
    public void reset() {
        Value = HealthState.DEAD;
    }

}
