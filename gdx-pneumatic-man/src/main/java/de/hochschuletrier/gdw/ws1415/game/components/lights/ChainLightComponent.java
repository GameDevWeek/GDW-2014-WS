package de.hochschuletrier.gdw.ws1415.game.components.lights;

import box2dLight.ChainLight;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ChainLightComponent extends Component implements Pool.Poolable
{ 
    public ChainLight chainLight;

    @Override
    public void reset()
    {
        chainLight = null; 
    }
}