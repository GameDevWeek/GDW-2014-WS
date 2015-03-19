package de.hochschuletrier.gdw.ws1415.game.utils;

import com.badlogic.gdx.controllers.Controllers;

import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.systems.InputGamepadSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputKeyboardSystem;

public class InputManager
{
    public enum Input{
        KEYBOARD, GAMEPAD
    }
    public InputManager()
    {
        //anfangs wird das Keyboard angemeldet
        setkeyboard();
    }
    
    public void setInput(Input input) throws NoGamepadException
    {
        switch(input)
        {
            case KEYBOARD: setkeyboard();
            case GAMEPAD: setgamepad();
        }
    }
    
    private void setkeyboard()
    {
        EntityCreator.engine.getSystem(InputKeyboardSystem.class).setProcessing(true);
        EntityCreator.engine.getSystem(InputGamepadSystem.class).setProcessing(false);
    }
    
    private void setgamepad() throws NoGamepadException
    {
        if(Controllers.getControllers().size == 0)
        {
            throw new NoGamepadException();
        }
        Controllers.addListener(EntityCreator.engine.getSystem(InputGamepadSystem.class));
        EntityCreator.engine.getSystem(InputKeyboardSystem.class).setProcessing(false);
        EntityCreator.engine.getSystem(InputGamepadSystem.class).setProcessing(true);
    }
    
    public boolean isControllerConnected()
    {
        return Controllers.getControllers().size > 0;
    }
}