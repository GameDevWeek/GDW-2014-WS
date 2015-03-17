package de.hochschuletrier.gdw.ss14.sandbox.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

public class GamepadController implements ControllerListener
{
    @Override
    public void connected(Controller controller)
    {
        // TODO Auto-generated method stub
        controller.addListener(this);
        System.out.println("Controller angeschlossen: " + controller.getName());
    }

    @Override
    public void disconnected(Controller controller)
    {
        // TODO Auto-generated method stub
        System.out.println("Gamepad entfernt: " + controller.getName());
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        // TODO Auto-generated method stub
        System.out.println("Button gedrückt: " + buttonCode + controller.getName());
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        // TODO Auto-generated method stub
        System.out.println("Button losgelassen: " + buttonCode + controller.getName());
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        // TODO Auto-generated method stub
        System.out.println("Achse bewegt: " + axisCode + controller.getName());
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        // TODO Auto-generated method stub
        System.out.println("pov bewegt: " + povCode + controller.getName());
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        // TODO Auto-generated method stub
        System.out.println("xSlider bewegt: " + sliderCode + controller.getName());
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        // TODO Auto-generated method stub
        System.out.println("ySlider bewegt: " + sliderCode + controller.getName());
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
