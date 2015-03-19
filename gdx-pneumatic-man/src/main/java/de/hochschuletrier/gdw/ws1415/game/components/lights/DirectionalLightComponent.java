package de.hochschuletrier.gdw.ws1415.game.components.lights;

import box2dLight.DirectionalLight;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DirectionalLightComponent extends Component implements Pool.Poolable
{ 
    public DirectionalLight directionalLight;

    @Override
    public void reset()
    {
        directionalLight = null; 
    }
}
