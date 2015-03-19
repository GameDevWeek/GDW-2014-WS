package de.hochschuletrier.gdw.ws1415.game.components.lights;

import box2dLight.ConeLight;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ConeLightComponent extends Component implements Pool.Poolable
{ 
    public ConeLight coneLight;

    @Override
    public void reset()
    {
        coneLight = null; 
    }
}
