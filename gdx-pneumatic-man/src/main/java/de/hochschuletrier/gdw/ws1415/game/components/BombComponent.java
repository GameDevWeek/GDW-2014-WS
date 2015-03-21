package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BombComponent extends Component implements Pool.Poolable {

    public int RadiusInTiles = 2;

    @Override
    public void reset() {
        RadiusInTiles = 2;
    }
    
    
    
}
