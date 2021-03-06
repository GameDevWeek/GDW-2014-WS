package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ws1415.game.utils.PlatformMode;

/**
 * Created by oliver on 18.03.15.
 */
public class PlatformComponent extends Component implements Pool.Poolable {

    public PlatformMode mode;
    public float travelDistance;
    public Vector2 startPos;
    public float platformSpeed;
    public Vector2 traveledDistanceVector = new Vector2();
    public Vector2 velocity = new Vector2();

    @Override
    public void reset() {
        travelDistance = 64;
        mode = PlatformMode.ALWAYS;
        startPos.set(0, 0);
        platformSpeed = 0;
    }
}
