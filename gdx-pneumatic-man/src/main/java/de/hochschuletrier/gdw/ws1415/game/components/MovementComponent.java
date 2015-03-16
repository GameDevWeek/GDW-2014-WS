package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Used as "interface" for Gamelogic to move left or right
 * 
 * @author David
 *
 */
public class MovementComponent extends Component implements Pool.Poolable {

	public Vector2 velocity;
	public float speed;
	public boolean movingRight, movingLeft;
	
	

	public void setVelocity(float x, float y) {
		velocity.set(x, y);
	}

	public void setVelocity(Vector2 v) {
		velocity.set(v);
	}

	/**
	 * If button to move right was pressed
	 */
	public void moveRight() {
		if (!movingRight) {
			velocity.add(speed, 0);
		}
	}

	/**
	 * If button to move right was released
	 */
	public void stopMovingRight() {
		if (movingRight) {
			velocity.add(-speed, 0);
		}
	}

	/**
	 * If button to move left was pressed
	 */
	public void moveLeft() {
		if (!movingLeft) {
			velocity.add(-speed, 0);
		}
	}

	/**
	 * If button to move left was released
	 */
	public void stopMovingLeft() {
		if (movingLeft) {
			velocity.add(speed, 0);
		}
	}

	@Override
	public void reset() {
		velocity.set(0, 0);
		speed = 0;
		movingRight = movingLeft = false;
	}

}
