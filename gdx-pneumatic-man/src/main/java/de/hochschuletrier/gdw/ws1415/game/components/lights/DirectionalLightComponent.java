package de.hochschuletrier.gdw.ws1415.game.components.lights;

import box2dLight.DirectionalLight;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DirectionalLightComponent extends Component implements Pool.Poolable
{ 
    public DirectionalLight directionalLight;
    public float offsetX = 0;
    public float offsetY = 0;

    @Override
    public void reset()
    {
        directionalLight.remove();
        directionalLight = null;
        offsetX = 0;
        offsetY = 0;
    }
}
