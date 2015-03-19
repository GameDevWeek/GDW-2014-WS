    package de.hochschuletrier.gdw.ws1415.states;

import java.awt.MouseInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.Transition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.sandbox.menu.IngameMenu;
import de.hochschuletrier.gdw.ws1415.sandbox.menu.MainMenu;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends BaseGameState implements InputProcessor {

    private final Music music;

    private final MenuManager menuManager = new MenuManager(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, null);
    private final  DecoImage hand;
    private final InputForwarder inputForwarder;
    private final AssetManagerX assetManager;
    public MainMenuState(AssetManagerX assetManager) {
        music = assetManager.getMusic("menu");
        MusicManager.play(music, 2.0f);
        
        this.assetManager = assetManager;
        Skin skin = Main.getInstance().getSkin();
        final MainMenu mainMenu = new MainMenu(skin, menuManager, MainMenu.Type.MAINMENU);
        
        //test IngameMenu:
//        final IngameMenu mainMenu =new IngameMenu(skin, menuManager, IngameMenu.Type.INGAME);
        hand = new DecoImage(assetManager.getTexture("zeigefinger"));
        menuManager.addLayer(mainMenu);
        
        menuManager.addLayer(new DecoImage(assetManager.getTexture("background_overlay")));
       // menuManager.addLayer(hand);
        
        menuManager.pushPage(mainMenu);
        mainMenu.addActor(hand);
//        menuManager.getStage().setDebugAll(true);

        Main.getInstance().addScreenListener(menuManager);
        

        inputForwarder = new InputForwarder() {
            @Override
            public boolean keyUp(int keycode) {
                if (mainProcessor != null && keycode == Input.Keys.ESCAPE) {
                    menuManager.popPage();
                    return true;
                }
                return super.keyUp(keycode);
            }
        };

        Main.inputMultiplexer.addProcessor(inputForwarder);
    }

    public void render() {
        Main.getInstance().screenCamera.bind();
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLACK);
        menuManager.render();
    }

    @Override
    public void update(float delta) {
        menuManager.update(delta);
        hand.setPosition(MouseInfo.getPointerInfo().getLocation().x-((hand.getWidth()/3)),(MouseInfo.getPointerInfo().getLocation().y*(-1))-(hand.getHeight()/3)); 
        render();
    }

    @Override
    public void onEnterComplete() {
//        MusicManager.play(music, Constants.MUSIC_FADE_TIME);
        inputForwarder.set(menuManager.getInputProcessor());
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        inputForwarder.set(null);
    }

    @Override
    public void dispose() {
        menuManager.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
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
      /*  Main main =Main.getInstance();
        if(main.isTransitioning())
        {
            main.changeState(new GameplayState(assetManager),new Transition(500), null);
        }*/
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
