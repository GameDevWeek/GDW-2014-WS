package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by oliver on 16.03.15.
 */
public class PlayerComponent extends Component implements Pool.Poolable {

    public int saved_miners;
    public int destroyed_blocks = 0;
    public int game_time;
    
    @Override
    public void reset() {
        saved_miners = 0;
        destroyed_blocks = 0;
        game_time = 0;
    }

}
