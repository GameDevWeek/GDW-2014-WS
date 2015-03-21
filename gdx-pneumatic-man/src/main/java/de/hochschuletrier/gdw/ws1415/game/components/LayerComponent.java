package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class LayerComponent extends Component implements Pool.Poolable {
    public int layer = 2;
    public float parallaxX = 1.f;
    public float parallaxY = 1.f;
    
    /**
     * Use parallaxX and parallaxY
     */
    @Deprecated
    public float parallax = 1.f;
    
    @Override
    public void reset() {
        layer = 0;
        parallax = 1.f;
        parallaxX = 1.f;
        parallaxY = 1.f;
    }
}
