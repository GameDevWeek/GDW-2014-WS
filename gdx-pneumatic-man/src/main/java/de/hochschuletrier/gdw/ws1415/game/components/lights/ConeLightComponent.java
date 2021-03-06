package de.hochschuletrier.gdw.ws1415.game.components.lights;

import box2dLight.ConeLight;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ConeLightComponent extends Component implements Pool.Poolable
{ 
    public ConeLight coneLight;
    public float offsetX = 0;
    public float offsetY = 0;

    @Override
    public void reset()
    {
        coneLight.remove();
        coneLight = null;
        offsetX = 0;
        offsetY = 0;
    }
}
