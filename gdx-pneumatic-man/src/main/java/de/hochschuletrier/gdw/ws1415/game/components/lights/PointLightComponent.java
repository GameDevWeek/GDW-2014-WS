package de.hochschuletrier.gdw.ws1415.game.components.lights;

import com.badlogic.ashley.core.Component;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ws1415.game.systems.SortedRenderSystem;

/**
 * Implement this Light like this:
 *  PointLightComponent pl = engine.createComponent(PointLightComponent.class);
 *  pl.pointLight = new PointLight(engine.getSystem(SortedRenderSystem.class).getRayHandler(), 
 *                                 GameConstants.LIGHT_RAYS, 
 *                                 new Color(float r,float g,float b, float a), 
 *                                 float distance, 
 *                                 0, 
 *                                 0);    
 * @author Dennis
 *
 */

public class PointLightComponent extends Component implements Pool.Poolable
{ 
    public PointLight pointLight;

    @Override
    public void reset()
    {
        pointLight = null; 
    }
}
