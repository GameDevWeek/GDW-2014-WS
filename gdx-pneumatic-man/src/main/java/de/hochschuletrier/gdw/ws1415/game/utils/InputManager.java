package de.hochschuletrier.gdw.ws1415.game.utils;

import com.badlogic.gdx.controllers.Controllers;

import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.systems.InputGamepadSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputKeyboardSystem;

public class InputManager {
	
	public enum Input{
		KEYBOARD, GAMEPAD
	}
	
	public InputManager(){
		//default
		setKeyboard();
	}
	/**
	 * 
	 * @param KEYBOARD_MOUSE, GAMEPAD
	 * @throws NoGamepadException
	 */
	public void setInput(Input input)throws NoGamepadException{
		switch(input){
		case KEYBOARD: setKeyboard(); break;
		case GAMEPAD: setGamepad(); break;
		}
	}
	
	private void setKeyboard(){
		EntityCreator.engine.getSystem(InputKeyboardSystem.class).setProcessing(true);
		EntityCreator.engine.getSystem(InputGamepadSystem.class).setProcessing(false);
		
		
	}
	
	private void setGamepad() throws NoGamepadException{
		if(Controllers.getControllers().size == 0){
			throw new NoGamepadException();
		}
		Controllers.addListener(EntityCreator.engine.getSystem(InputGamepadSystem.class));
		EntityCreator.engine.getSystem(InputGamepadSystem.class).setProcessing(true);
		EntityCreator.engine.getSystem(InputKeyboardSystem.class).setProcessing(false);
	}
	
	
	public boolean isControllerConnected(){
		return Controllers.getControllers().size > 0;
	}

}
