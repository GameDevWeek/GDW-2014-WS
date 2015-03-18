package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import box2dLight.PointLight;
import com.badlogic.gdx.utils.Pool;

public class PointLightComponent extends Component implements Pool.Poolable
{ 
    public PointLight pointLight;

    @Override
    public void reset()
    {
        pointLight = null; 
    }
}
