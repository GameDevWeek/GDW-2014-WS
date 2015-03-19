package de.hochschuletrier.gdw.ws1415.game.systems.renderers;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.ChainLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.ConeLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.DirectionalLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.PointLightComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedFamilyRenderSystem;

public class LightRenderer extends SortedFamilyRenderSystem.Renderer {

    @SuppressWarnings("unchecked")
    public LightRenderer() {
        super(Family.one(PointLightComponent.class/*, DirectionalLightComponent.class*/, ChainLightComponent.class, ConeLightComponent.class).get());
    }

    @Override
    public void render(Entity entity, float deltaTime){
        PositionComponent position = ComponentMappers.position.get(entity);
        PointLightComponent pointLight = ComponentMappers.pointLight.get(entity);
        //DirectionalLightComponent directionalLight = ComponentMappers.directionalLight.get(entity);
        ChainLightComponent chainLight = ComponentMappers.chainLight.get(entity);
        ConeLightComponent coneLight = ComponentMappers.coneLight.get(entity);
        
        //directionalLight.directionalLight.setPosition(x, y);
        
        if(pointLight != null){
            pointLight.pointLight.setPosition(position.x/GameConstants.BOX2D_SCALE, position.y/GameConstants.BOX2D_SCALE);
        }
        if(chainLight != null){
            chainLight.chainLight.setPosition(position.x/GameConstants.BOX2D_SCALE, position.y/GameConstants.BOX2D_SCALE);
        }
        if(coneLight != null){
            coneLight.coneLight.setPosition(position.x/GameConstants.BOX2D_SCALE, position.y/GameConstants.BOX2D_SCALE);
        }
    }


    

    
  
}
