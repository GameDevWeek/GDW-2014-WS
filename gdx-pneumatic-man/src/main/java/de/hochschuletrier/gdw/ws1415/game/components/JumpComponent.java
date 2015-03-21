package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Use this Component to make an entity jump
 * 
 * @author David
 *
 */
public class JumpComponent extends Component implements Pool.Poolable {

    public boolean justJumped = false;
	public float jumpSpeed;
	public float restingTime, timeToNextJump;
	public float jumpTimer = 0.2f;
	
	public boolean justLanded;
    public int groundContacts;

	@Override
	public void reset() {
		jumpSpeed = 0;
		restingTime = 0;
		groundContacts = 0;
	}

}
