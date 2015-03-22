    package de.hochschuletrier.gdw.ws1415.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.menu.MainMenu;

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
    private final DecoImage overlay;
            
    public MainMenuState(AssetManagerX assetManager) {
       
            music = assetManager.getMusic("menu");
        //MusicManager.play(assetManager.getMusic("menu"),2.0f);
            MusicManager.play(music, 2.0f);
        
        
        Skin skin = Main.getInstance().getSkin();
        final MainMenu mainMenu =new MainMenu(skin, menuManager, MainMenu.Type.MAINMENU);
       
        //test IngameMenu:
//        final IngameMenu mainMenu =new IngameMenu(skin, menuManager, IngameMenu.Type.INGAME);
        hand = new DecoImage(assetManager.getTexture("zeigefinger2"));
        //hand.setSize(hand.getWidth()/2,hand.getWidth()/2);
        menuManager.addLayer(mainMenu);
        
        overlay = new DecoImage(assetManager.getTexture("background_overlay"));
        
        menuManager.pushPage(mainMenu);
        //System.out.println(menuManager.getStage());
        menuManager.getStage().addActor(overlay);
        menuManager.getStage().addActor(hand);
//        menuManager.getStage().setDebugAll(true);

        Main.getInstance().addScreenListener(menuManager);
       
        
        inputForwarder = new InputForwarder() {
            @Override
            public boolean keyUp(int keycode) {
                if (mainProcessor != null && keycode == Input.Keys.ESCAPE) {
                    SoundEmitter.playGlobal(assetManager.getSound("pmCancel"),false);
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
        final Stage stage = menuManager.getStage();
        overlay.setPosition((stage.getWidth() - Main.WINDOW_WIDTH)/2 - 630, (stage.getHeight() - Main.WINDOW_HEIGHT)/2 - 730);
        
        Vector2 vector= stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
        hand.setPosition(vector.x-765,vector.y-1200);
      
//        MusicManager.update(delta);
        render();
    }

    @Override
    public void onEnterComplete() {
        
        MusicManager.play(music, GameConstants.MUSIC_FADE_TIME);
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
