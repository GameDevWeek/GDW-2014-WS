package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.Comparator;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.AnimationRenderer;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.DestructableBlockRenderer;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.LightRenderer;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.ParticleRenderer;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.TextureRenderer;

/**
 * 
 * All Entities that have to be rendered require a PositionComponent and a LayerComponent. <br>
 * If at least one of them is not provided the Entity won't be rendered.
 *
 */
public class SortedRenderSystem extends SortedFamilyRenderSystem {
    
    public RayHandler rayHandler;
	private LayerComponent currentLayer = null;
	private CameraSystem cameraSystem;
	private Matrix4 scaleMatrix = new Matrix4();
	
    @SuppressWarnings("unchecked")
	public SortedRenderSystem(CameraSystem cameraSystem, RayHandler rayHandler) {
        super(Family.all(PositionComponent.class, LayerComponent.class).get(), new Comparator<Entity>() { 
            @Override
            public int compare(Entity e1, Entity e2) {
                LayerComponent ac = ComponentMappers.layer.get(e1);
                LayerComponent bc = ComponentMappers.layer.get(e2);
                return ac.layer > bc.layer ? 1 : (ac.layer == bc.layer) ? 0 : -1;
            }
        });

        
        // Order of adding = order of renderer selection for the entity
        addRenderer(new AnimationRenderer());
        addRenderer(new DestructableBlockRenderer());
        addRenderer(new TextureRenderer());
        addRenderer(new ParticleRenderer());
        addRenderer(new LightRenderer());
        
        this.rayHandler = rayHandler;
        this.rayHandler.setAmbientLight(0.5f);
        this.cameraSystem = cameraSystem;
    }
    
    @Override
    public void processEntity(Entity entity, float deltaTime) {
    	LayerComponent layerComponent = ComponentMappers.layer.get(entity);
    	
    	if (currentLayer == null || layerComponent.layer != currentLayer.layer) {
    		onLayerChanged(currentLayer, layerComponent);
    		currentLayer = layerComponent;
    	}
    	
    	super.processEntity(entity, deltaTime);
    }
    
    private void onLayerChanged(LayerComponent oldLayer, LayerComponent newLayer) {
//    	cameraSystem.applyParallax(newLayer);
    }
    
    @Override
	public void update (float deltaTime) {
    	super.update(deltaTime);
    	
    	// rayHandler.updateAndRender() not allowed between begin() and end()
    	DrawUtil.batch.end();
    	updateRayHandler();
    	DrawUtil.batch.begin();
	}
    
    private void updateRayHandler(){
        OrthographicCamera camera = cameraSystem.getCamera().getOrthographicCamera();
        scaleMatrix.set(camera.combined).scl(GameConstants.BOX2D_SCALE);
        rayHandler.setCombinedMatrix(scaleMatrix , 
                                     camera.position.x/GameConstants.BOX2D_SCALE, 
                                     camera.position.y/GameConstants.BOX2D_SCALE, 
                                     camera.viewportWidth*camera.zoom/GameConstants.BOX2D_SCALE, 
                                     camera.viewportHeight*camera.zoom/GameConstants.BOX2D_SCALE);
        rayHandler.updateAndRender(); 
    }

    public RayHandler getRayHandler(){
        return this.rayHandler;
    }
    
    
}
