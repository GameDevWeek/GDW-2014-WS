package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import java.util.Comparator;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.AnimationRenderer;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.DestructableBlockRenderer;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.TextureRenderer;

public class SortedRenderSystem extends SortedFamilyRenderSystem {
	private LayerComponent currentLayer = null;
	private CameraSystem cameraSystem = new CameraSystem();
	
    @SuppressWarnings("unchecked")
	public SortedRenderSystem() {
        super(Family.all(PositionComponent.class, LayerComponent.class).get(), new Comparator<Entity>() {

            @Override
            public int compare(Entity e1, Entity e2) {
                LayerComponent ac = ComponentMappers.layer.get(e1);
                LayerComponent bc = ComponentMappers.layer.get(e2);
                return ac.layer > bc.layer ? 1 : (ac.layer == bc.layer) ? 0 : -1;
            }
        });

        
        // Order of adding = order of choosing of the renderer for the entity
        // addRenderer(new ParticleRenderer());
        addRenderer(new AnimationRenderer());
        addRenderer(new DestructableBlockRenderer());
        addRenderer(new TextureRenderer());
    }
    
    @Override
    public void processEntity(Entity entity, float deltaTime) {
    	super.processEntity(entity, deltaTime);
    	
    	LayerComponent layerComponent = ComponentMappers.layer.get(entity);
    	if (layerComponent != currentLayer) {
    		onLayerChanged(currentLayer, layerComponent);
    		currentLayer = layerComponent;
    	}
    }
    
    private void onLayerChanged(LayerComponent oldLayer, LayerComponent newLayer) {
    	if(oldLayer != null)
    		cameraSystem.postParallax();

    	cameraSystem.preParallax(newLayer);
    }
    
    public LimitedSmoothCamera getCamera() {
    	return cameraSystem.getCamera();
    }
}
