package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Add this to the LavaFountain Height: How high in block height should the lava
 * fountain shoot 
 * intervall: start the fountain every intervall seconds
 * intervallOffset: how long after the level was started should the fountain
 * start the first time 
 * length: how long should the fountain shoot of
 * projectiles
 * lavaBallSpeed: How fast should the lavaBalls be shot out
 * lavaBallSpawnIntervall: how fast after each other should lava balls be spawned
 * @author David
 *
 */
public class LavaFountainComponent extends Component implements Pool.Poolable {

    public float height, intervall, intervallOffset, length, lavaBallSpeed, lavaBallSpawnIntervall;
    
    
    
    public float timeToNextFountain = 0;
    public float timeTillFountainStops = 0;
    public float timeToNextBall = 0;
    

    @Override
    public void reset() {
        height = intervall = intervallOffset = length = timeToNextFountain = timeTillFountainStops = lavaBallSpeed = timeToNextBall = 0;

    }

}
