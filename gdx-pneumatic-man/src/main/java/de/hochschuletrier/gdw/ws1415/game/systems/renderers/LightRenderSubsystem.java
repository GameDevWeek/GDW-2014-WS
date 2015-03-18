package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.PointLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightRenderSubsystem
{
    private HashMap<Integer, RayHandler> rayHandlers = new HashMap<Integer, RayHandler>();
    
    Vector2 dummyVector = new Vector2();
    
    void updateLight(Entity entity, float deltaTime){
        PositionComponent position = ComponentMappers.position.get(entity);
        PointLightComponent pointLight = ComponentMappers.pointLight.get(entity);
        pointLight.pointLight.setPosition(position.x/GameConstants.BOX2D_SCALE, position.y/GameConstants.BOX2D_SCALE);
    }
    
    void renderLayer(int layer, OrthographicCamera camera){
        Matrix4 matrixCopy = new Matrix4(camera.combined);
        rayHandlers.get(layer).setCombinedMatrix(matrixCopy.scl(GameConstants.BOX2D_SCALE));
        rayHandlers.get(layer).updateAndRender();
    }
    
    public PointLight createPointLight(Color color, int distance, int layer, World world){
        PointLight plc = new PointLight(getRayHandler(layer, world), 360, color, distance, 0, 0);
        return plc;
    }
    
    /**
     * Creates a new handler if needed.
     */
    private RayHandler getRayHandler(int layer, World world) {
        if(!rayHandlers.containsKey(layer)){
            RayHandler rayHandler = new RayHandler(world);
            rayHandler.setShadows(true);
            rayHandlers.put(layer, rayHandler);
        }
        return rayHandlers.get(layer);
    }
}
