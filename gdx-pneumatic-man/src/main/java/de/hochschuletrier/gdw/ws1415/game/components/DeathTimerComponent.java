package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DeathTimerComponent extends Component implements Pool.Poolable{

    public float deathTimer = 0.25f;;

	@Override
	public void reset() {
        deathTimer = 0.25f;
	}
}
