package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;


public class CameraSystem {
	private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
	private Vector2 cameraDestination = new Vector2();
	private Vector2 firstCameraPosition;
	private Vector2 cameraPosition = new Vector2();
	private Vector2 cameraPosDelta = new Vector2();
	
	private Vector2 dummyVector = new Vector2();
    
	private Entity toFollow;
	
	public LimitedSmoothCamera getCamera() {
		return camera;
	}

	void preParallax(LayerComponent layer) {
		cameraDestination.set(camera.getDestination().x, camera.getDestination().y);
		cameraPosition.set(camera.getPosition().x, camera.getPosition().y);
		
		if(firstCameraPosition == null) {
			firstCameraPosition = new Vector2(cameraPosition.x, cameraPosition.y);
		}

        cameraPosDelta.set(cameraPosition).sub(firstCameraPosition.x, firstCameraPosition.y);
        
        dummyVector.set(firstCameraPosition.x, firstCameraPosition.y);
        dummyVector.mulAdd(cameraPosDelta, layer.parallax);

        camera.setDestination(dummyVector.x, dummyVector.y);
        camera.updateForced();

        camera.bind();
	}
	
	/**
	 * Sets the camera position/destination back to the old camera position/destination.
	 */
	void postParallax() {
		camera.setDestination(cameraPosition.x, cameraPosition.y);
		camera.updateForced();
		
		camera.setDestination(cameraDestination.x, cameraDestination.y);
		camera.bind();
	}
	
    void update(float deltaTime) {
    	PositionComponent toFollowPos = ComponentMappers.position.get(toFollow);
    	
    	if(toFollowPos != null)
    		camera.setDestination(toFollowPos.x, toFollowPos.y);
    	
		camera.bind();
		camera.update(deltaTime);	
    }
    
    /**
     * Lets the camera follow an Entity. Make sure the Entity has a PositionComponent.
     * The camera won't follow an Entity without a PositionComponent.
     * @param toFollow The Entity which will be followed by the camera. 
     * If it's null the camera won't follow anything. (Stops following if it did)
     */
    public void follow(Entity entity) {
    	this.toFollow = entity;
    }
}
