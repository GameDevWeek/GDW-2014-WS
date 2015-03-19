package de.hochschuletrier.gdw.ws1415.states;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.ws1415.Main;

/**
 * PauseGameState
 * 
 * @author Tobias Thierbach 19.03.2015
 * 
 *         #keine Ahnung
 *
 *
 */
public class PauseGameState extends BaseGameState implements InputProcessor {

    InputInterceptor inputProcessor;

    private BaseGameState previosGameState;

    public PauseGameState() {
        System.out.println("PauseGameState");
        inputProcessor = new InputInterceptor(this);
        Main.inputMultiplexer.addProcessor(inputProcessor);
    }

    @Override
    public void update(float delta) {
        // keine updates machen ?
        // game.update(0);
    }

    @Override
    public void onEnter(BaseGameState previousState) {
        this.previosGameState = previousState;
        inputProcessor.setActive(true);
        inputProcessor.setBlocking(true);
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        inputProcessor.setActive(false);
        inputProcessor.setBlocking(false);
    }

    @Override
    public void dispose() {
        // game.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub

        switch (keycode) {
        case Input.Keys.ESCAPE: // fallthrough intended
        case Input.Keys.P:
            Main main = Main.getInstance();
            main.changeState(previosGameState);
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
