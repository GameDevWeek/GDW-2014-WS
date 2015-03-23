package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1415.states.PauseGameState;

public class InputKeyboardSystem extends IteratingSystem implements InputProcessor {

    private float jump = -1;

    private boolean left = false;

    private boolean right = false;

    public InputKeyboardSystem() {
        super(Family.all(InputComponent.class).get());

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InputComponent inputComponent = entity.getComponent(InputComponent.class);

        inputComponent.reset();
        if (jump > 0) {
            inputComponent.jump = true;
            jump -= deltaTime;
            System.out.println(deltaTime);
        }
        if (right) {
            inputComponent.direction++;
        }
        if (left) {
            inputComponent.direction--;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.SPACE:
            case Input.Keys.W:
                jump = 0.2f;
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                left = true;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                right = true;
                break;
            case Input.Keys.ESCAPE:
            case Input.Keys.P: GameConstants.pause = !GameConstants.pause; break;
        }
        if(!Main.IS_RELEASE){  // nur für Testzwecke
            if(keycode == Input.Keys.C){
                Settings.GAMEPAD_ENABLED.set(true);
            }
            else if(keycode == Input.Keys.K){
                Settings.GAMEPAD_ENABLED.set(false);
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
//        case Input.Keys.UP:
//        case Input.Keys.SPACE:
//        case Input.Keys.W:
//            jump = false;
//            break;
        case Input.Keys.LEFT:
        case Input.Keys.A:
            left = false;
            break;
        case Input.Keys.RIGHT:
        case Input.Keys.D:
            right = false;
            break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
}
