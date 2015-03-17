package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;


public class CameraSystem {
	private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
	private Vector3 cameraDestination;
	
	public LimitedSmoothCamera getCamera() {
		return camera;
	}
	
	void preParallax(LayerComponent layer) {
		cameraDestination = camera.getDestination();	
		
		camera.setDestination(cameraDestination.x*layer.parallax, cameraDestination.y*layer.parallax);
		camera.updateForced();
	}
	
	/**
	 * Sets the camera position back to the old camera position.
	 */
	void postParallax() {
		camera.setDestination(cameraDestination.x, cameraDestination.y);
		camera.updateForced();
	}
	
	void update(float deltaTime) {
		camera.update(deltaTime);
	}
}
