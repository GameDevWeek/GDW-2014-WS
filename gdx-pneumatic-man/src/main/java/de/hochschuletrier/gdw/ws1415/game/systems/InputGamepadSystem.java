package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;


public class InputGamepadSystem extends IteratingSystem implements ControllerListener
{
	public InputGamepadSystem(){
		super(Family.all(InputComponent.class).get());
		// TODO Auto-generated constructor stub
	}

	private float jump = -1;
	private float direction;
	
	protected void processEntity(Entity entity, float deltaTime) {
		InputComponent inputComponent = entity.getComponent(InputComponent.class);
		
		inputComponent.reset();
		if(jump > 0){
			inputComponent.jump = true;
//			jump -= deltaTime; // einkommentieren wenn man möchte, dass man dauerspringen kann
		}
		inputComponent.direction = direction;
	}
	
    @Override
    public void connected(Controller controller)
    {
        // funktioniert sowieso nich
    }

    @Override
    public void disconnected(Controller controller)
    {
        // warum schreibt jemand diese methode wenn se sowieso net funktioniert
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        switch(buttonCode){
        case 0:
        case 1:
        case 2: 
        case 3: jump = 0.2f; break;
        case 7: GameConstants.pause = !GameConstants.pause; break;
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    { // alles auskommentieren wenn man nicht möchte, dass man durch halten der Sprung-Taste dauerhaft springen kann
        switch(buttonCode){
            case 0:
            case 1:
            case 2: 
            case 3: jump = -0.5f; break;
            }
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        if(axisCode == 1){
        	if(value > 0.4){
        		direction = value;
        	}
        
        	else if(value < -0.4){
        		direction = value;
        	}
        	else {
        		direction = 0;
        	}
        }
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        switch(value){
        case northEast:
        case southEast:
        case east: direction = 1; break;
        case northWest:
        case southWest:
        case west: direction = -1; break;
        default: direction = 0; break;
        }
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        // TODO Auto-generated method stub
      
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {

        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        
        return false;
    }
}
