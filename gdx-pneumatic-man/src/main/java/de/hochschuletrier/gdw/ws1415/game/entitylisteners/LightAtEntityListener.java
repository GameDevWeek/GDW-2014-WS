package de.hochschuletrier.gdw.ws1415.game.entitylisteners;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;

import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;

public class LightAtEntityListener implements EntityListener
{
    private RayHandler rayHandler;
    
    public LightAtEntityListener(RayHandler rayHandler){
        this.rayHandler = rayHandler;
    }

    @Override
    public void entityAdded(Entity entity)
    {
        if(ComponentMappers.pointLight.has(entity)){
            
            PointLight pointLight = new PointLight(null, 15f, new Color(), 100f, 0, 0);
        } 
        
    }

    @Override
    public void entityRemoved(Entity entity)
    {
        
    }

}
