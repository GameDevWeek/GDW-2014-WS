package de.hochschuletrier.gdw.ss14.sandbox.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

public class GamepadSystem extends SandboxGame
{
    GamepadController gamepadController = new GamepadController();
    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init(AssetManagerX assetManager)
    {
        // TODO Auto-generated method stub
        for (Controller controller : Controllers.getControllers()) {
            Gdx.app.log("Gamepad", controller.getName());
            System.out.println("Gamepad angemeldet" + controller.getName());
            controller.addListener(gamepadController);
        }
    }

    @Override
    public void update(float delta)
    {
        // TODO Auto-generated method stub
        
    }

}
