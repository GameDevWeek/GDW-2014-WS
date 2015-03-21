package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.Comparator;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Engine;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
import de.hochschuletrier.gdw.commons.devcon.cvar.ICVarListener;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
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
    private final CVarFloat ambientLight = new CVarFloat("light_ambient", 0.5f, 0, 1, 0, "Ambient light value");
    private final CVarBool culling = new CVarBool("light_culling", true, 0, "Light Culling");
    private final CVarBool shadows = new CVarBool("light_shadows", true, 0, "Light Shadows");
    private final CVarInt blur = new CVarInt("light_blur", 2, 0, 3, 0, "Light blur value");
    private final DevConsole console;
	
    @SuppressWarnings("unchecked")
	public SortedRenderSystem(CameraSystem cameraSystem, RayHandler rayHandler, int priority) {
        super(Family.all(PositionComponent.class, LayerComponent.class).get(), new Comparator<Entity>() { 
            @Override
            public int compare(Entity e1, Entity e2) {
                LayerComponent ac = ComponentMappers.layer.get(e1);
                LayerComponent bc = ComponentMappers.layer.get(e2);
                return ac.layer > bc.layer ? 1 : (ac.layer == bc.layer) ? 0 : -1;
            }
        }, priority);

        
        // Order of adding = order of renderer selection for the entity
        addRenderer(new AnimationRenderer());
        addRenderer(new DestructableBlockRenderer());
        addRenderer(new TextureRenderer());
        addRenderer(new ParticleRenderer());
        addRenderer(new LightRenderer());
        
        this.rayHandler = rayHandler;
  
        this.rayHandler.setAmbientLight(GameConstants.LIGHT_AMBIENT);
        this.rayHandler.setBlur(GameConstants.LIGHT_BLUR);
        this.rayHandler.setBlurNum(GameConstants.LIGHT_BLURNUM);
        this.rayHandler.setShadows(GameConstants.LIGHT_SHADOW);
        this.rayHandler.useDiffuseLight(GameConstants.LIGHT_DIFFUSE);
        this.cameraSystem = cameraSystem;
        
        console = Main.getInstance().console;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        
        addCVar(ambientLight, this::onAmbientLightChange);
        addCVar(culling, this::onCullingChange);
        addCVar(blur, this::onBlurChange);
        addCVar(shadows, this::onShadowsChange);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        
        console.unregister(ambientLight);
        console.unregister(culling);
        console.unregister(blur);
        console.unregister(shadows);
    }

    private void addCVar(CVar cvar, ICVarListener listener) {
        console.register(cvar);
        cvar.addListener(listener);
        listener.modified(cvar);
    }
    
    private void onAmbientLightChange(CVar cvar) {
        rayHandler.setAmbientLight(ambientLight.get());
    }
    
    private void onCullingChange(CVar cvar) {
        rayHandler.setCulling(culling.get());
    }
    
    private void onBlurChange(CVar cvar) {
        rayHandler.setBlurNum(blur.get());
    }
    
    private void onShadowsChange(CVar cvar) {
        rayHandler.setShadows(shadows.get());
    }
    
    @Deprecated
    public SortedRenderSystem(CameraSystem cameraSystem, RayHandler rayHandler) {
        this(cameraSystem, rayHandler, 0);
    }
    
	@Override
	public void entityRemoved (Entity entity) {
		super.entityRemoved(entity);
		forceSort(); // sort is needed after an entity is removed!
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
    	super.update(deltaTime);
        cameraSystem.undoParallax();
    	
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
