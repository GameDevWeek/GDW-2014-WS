package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;


public class InputGamepadSystem extends IteratingSystem implements ControllerListener
{
	public InputGamepadSystem(){
		super(Family.all(InputComponent.class).get());
		// TODO Auto-generated constructor stub
	}

	public boolean jump;
	public float direction;
	public boolean pause;
	
	protected void processEntity(Entity entity, float deltaTime) {
		InputComponent inputComponent = entity.getComponent(InputComponent.class);
		
		inputComponent.reset();
		if(jump){
			inputComponent.jump = true;
		}
		if(pause){
			inputComponent.pause = true;
		}
		inputComponent.direction = direction;
		
	}
	
    @Override
    public void connected(Controller controller)
    {
    	
    }

    @Override
    public void disconnected(Controller controller)
    {
        // TODO Auto-generated method stub
      
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        switch(buttonCode){
        case 0:
        case 1:
        case 2: 
        case 3: jump = true; System.out.println("Jump");break;
        case 7: pause = true; System.out.println("Pause");break;
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
    	switch(buttonCode){
        case 0:
        case 1:
        case 2:
        case 3: jump = false; break;
        case 7: pause = false; break;
        }
      
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        if(axisCode == 1){
        	if(value > 0.5){
        		direction = value;
        		System.out.println("Right");
        	}
        
        	else if(value < -0.5){
        		direction = value;
        		System.out.println("Left");
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
        case east: direction = 1; System.out.println("Right"); break;
        case northWest:
        case southWest:
        case west: direction = -1; System.out.println("Left"); break;
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
