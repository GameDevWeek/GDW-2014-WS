package de.hochschuletrier.gdw.ws1415.game.utils;

import com.badlogic.gdx.controllers.Controllers;

import de.hochschuletrier.gdw.commons.gdx.settings.Setting;
import de.hochschuletrier.gdw.commons.gdx.settings.SettingListener;
import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.systems.InputGamepadSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputKeyboardSystem;

public class InputManager implements SettingListener<Boolean>{
	
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

	public void init()
	{
	    // Listener anmelden
	    // Wert aus den settings auslesen und darauf einstellen
	    Settings.GAMEPAD_ENABLED.addListener(this);
	    onSettingChanged(Settings.GAMEPAD_ENABLED, Settings.GAMEPAD_ENABLED.get());
	}
	
	public void close()
	{
	    // Listener abmelden
	    Settings.GAMEPAD_ENABLED.removeListener(this);

	}
    @Override
    public void onSettingChanged(Setting setting, Boolean value)
    {
        // TODO Auto-generated method stub
        if(value)
        {
            try{
                setGamepad();
            }
            catch (NoGamepadException e)
            {
                Settings.GAMEPAD_ENABLED.set(false);
            }
        }
        else
        {
            setKeyboard();
        }
    }
}
