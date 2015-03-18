package de.hochschuletrier.gdw.ws1415.states;

import java.awt.MouseInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.sandbox.menu.MainMenu;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends BaseGameState {

    private final Music music;

    private final MenuManager menuManager = new MenuManager(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, null);
    private final  DecoImage hand;
    private final InputForwarder inputForwarder;
    
    public MainMenuState(AssetManagerX assetManager) {
        music = assetManager.getMusic("menu");

        Skin skin = Main.getInstance().getSkin();
        final MainMenu mainMenu =new MainMenu(skin, menuManager, MainMenu.Type.MAINMENU);
        hand=new DecoImage(assetManager.getTexture("zeigefinger"));
        menuManager.addLayer(mainMenu);
        mainMenu.addActor(hand);
        menuManager.pushPage(mainMenu);
        
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
        hand.setPosition(MouseInfo.getPointerInfo().getLocation().x-(hand.getWidth()/2),MouseInfo.getPointerInfo().getLocation().y*(-1)); 
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
}
