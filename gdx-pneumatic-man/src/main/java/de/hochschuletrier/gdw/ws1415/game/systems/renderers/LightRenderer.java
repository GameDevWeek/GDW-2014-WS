package de.hochschuletrier.gdw.ws1415.game.systems.renderers;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DestructableBlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PointLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedFamilyRenderSystem;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightRenderer extends SortedFamilyRenderSystem.Renderer {

    @SuppressWarnings("unchecked")
    public LightRenderer() {
        super(Family.all(PointLightComponent.class).get());
    }

    @Override
    public void render(Entity entity, float deltaTime){
        PositionComponent position = ComponentMappers.position.get(entity);
        PointLightComponent pointLight = ComponentMappers.pointLight.get(entity);
        pointLight.pointLight.setPosition(position.x/GameConstants.BOX2D_SCALE, position.y/GameConstants.BOX2D_SCALE);
    }


    

    
  
}
