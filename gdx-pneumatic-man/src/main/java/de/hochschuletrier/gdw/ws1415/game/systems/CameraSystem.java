package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;


public class CameraSystem {
	private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
	private Vector3 cameraPos;
	
	public LimitedSmoothCamera getCamera() {
		return camera;
	}
	
	void preParallax(LayerComponent layer) {
		cameraPos = camera.getPosition();	
		
		camera.setDestination(cameraPos.x*layer.parallax, cameraPos.y*layer.parallax);
		camera.updateForced();
	}
	
	/**
	 * Sets the camera position back to the old camera position.
	 */
	void postParallax() {
		camera.setDestination(cameraPos.x, cameraPos.y);
		camera.updateForced();
	}
}
