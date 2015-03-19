package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import java.util.Comparator;

import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.AnimationRenderer;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.DestructableBlockRenderer;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.ParticleRenderer;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.TextureRenderer;

/**
 * 
 * All Entities that have to be rendered require a PositionComponent and a LayerComponent. <br>
 * If at least one of them is not provided the Entity won't be rendered.
 *
 */
public class SortedRenderSystem extends SortedFamilyRenderSystem {
	private LayerComponent currentLayer = null;
	private CameraSystem cameraSystem;
	
    @SuppressWarnings("unchecked")
	public SortedRenderSystem(CameraSystem cameraSystem) {
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
    	cameraSystem.applyParallax(newLayer);
    }
    
    @Override
	public void update (float deltaTime) {	
//        cameraSystem.update(deltaTime);
    	super.update(deltaTime);	
	}
    
    
}
