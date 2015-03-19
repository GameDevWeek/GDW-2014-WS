package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;


public class CameraSystem extends EntitySystem implements EntityListener {
    
    private static final Logger logger = LoggerFactory.getLogger(CameraSystem.class);
    
	private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
	private Vector2 firstCameraPosition;
	private Vector2 cameraPosDelta = new Vector2();
	
	private Vector2 dummyVector = new Vector2();

	private Entity toFollow;
	
	private Vector2 cameraPos = new Vector2();
	private boolean firstLayer = true;
	
	private final ArrayList<Entity> entities = new ArrayList<>();
	
	public CameraSystem() {
		camera.setDestination(0f, 0f);
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.updateForced();
	}
	
    @Override
    public void addedToEngine(Engine engine) {
        // one() bekommt später noch eine geplante TextureComponent
        @SuppressWarnings("unchecked")
        Family family = Family.all(PlayerComponent.class).get();
        engine.addEntityListener(family, this);
    }
	
	public LimitedSmoothCamera getCamera() {
		return camera;
	}

	void applyParallax(LayerComponent layer) {
		if(firstCameraPosition == null) {
			firstCameraPosition = new Vector2(camera.getPosition().x, camera.getPosition().y);
		}
		
		if(firstLayer) {
			cameraPos.set(camera.getPosition().x, camera.getPosition().y);
			firstLayer = false;
		}

        cameraPosDelta.set(cameraPos).sub(firstCameraPosition.x, firstCameraPosition.y);
        
        dummyVector.set(firstCameraPosition.x, firstCameraPosition.y);
        dummyVector.mulAdd(cameraPosDelta, layer.parallax);

        camera.setDestination(dummyVector.x, dummyVector.y);
        camera.updateForced();

        camera.bind();
	}
	
	@Override
    public void update(float deltaTime) {
    	// reset camera position to the correct position after parallax
		camera.setDestination(cameraPos.x, cameraPos.y);
		camera.updateForced();
		firstLayer = true;
		
    	if(toFollow != null) {
    		PositionComponent toFollowPos = ComponentMappers.position.get(toFollow);
        	if(toFollowPos != null)
        		camera.setDestination(toFollowPos.x, toFollowPos.y);
    	}	

    	camera.updateForced();
    	
		camera.bind();
		camera.update(deltaTime);	
    }
    
    public void adjustToMap(TiledMap map) {
    	assert(map != null);
    	
        float totalMapWidth = map.getWidth() * map.getTileWidth();
        float totalMapHeight = map.getHeight() * map.getTileHeight();
        camera.setBounds(0, 0, totalMapWidth, totalMapHeight);
    }
    
    /**
     * Lets the camera follow an Entity. Make sure the Entity has a PositionComponent.
     * The camera won't follow an Entity without a PositionComponent.
     * @param toFollow The Entity which will be followed by the camera. 
     * If it's null the camera won't follow anything. (Stops following if it did)
     */
    @Deprecated
    public void follow(Entity entity) {
//    	this.toFollow = entity;
    }

    @Override
    public void entityAdded(Entity entity) {
        entities.add(entity);;
        
        if(toFollow == null)
            toFollow = entity;
    }

    @Override
    public void entityRemoved(Entity entity) {
        entities.remove(entity);
        
        if(entity == toFollow)
            if(entities.size() > 0)
                toFollow = entities.get(0);
            else
                toFollow = null;
    }
}
