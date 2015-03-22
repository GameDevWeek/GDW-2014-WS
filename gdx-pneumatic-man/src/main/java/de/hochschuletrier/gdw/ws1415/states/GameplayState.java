package de.hochschuletrier.gdw.ws1415.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.Game;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.menu.MainMenu;
import de.hochschuletrier.gdw.ws1415.game.menu.WinScreen;

/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends BaseGameState {

    private static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f, 0.5f);

    private final Game game;
    private final Music music;

    private final MenuManager menuManager = new MenuManager(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, this::onMenuEmptyPop);
    private final InputForwarder inputForwarder;
    private final InputProcessor menuInputProcessor;
    private final InputProcessor gameInputProcessor;
    private final DecoImage hand;
    private final DecoImage overlay;

    public GameplayState(AssetManagerX assetManager) {
        game = new Game();
        game.init(assetManager);
        music = assetManager.getMusic("game");
        
        Skin skin = Main.getInstance().getSkin();
        final MainMenu mainMenu =new MainMenu(skin, menuManager, MainMenu.Type.INGAME);
       
        //test IngameMenu:
//        final IngameMenu winScreen =new IngameMenu(skin, menuManager, IngameMenu.Type.INGAME);
        hand = new DecoImage(assetManager.getTexture("zeigefinger2"));
        //hand.setSize(hand.getWidth()/2,hand.getWidth()/2);
        menuManager.addLayer(mainMenu);
        menuInputProcessor = menuManager.getInputProcessor();
        gameInputProcessor = game.getInputProcessor();
        
        overlay = new DecoImage(assetManager.getTexture("background_overlay"));
        menuManager.pushPage(mainMenu);
        menuManager.getStage().addActor(overlay);
        menuManager.getStage().addActor(hand);
//        menuManager.getStage().setDebugAll(true);

        Main.getInstance().addScreenListener(menuManager);

        inputForwarder = new InputForwarder() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (mainProcessor == gameInputProcessor) {
                        mainProcessor = menuInputProcessor;
                        GameConstants.menuOpen = true;
                    } else {
                        menuManager.popPage();
                    }
                    return true;
                }
                return super.keyUp(keycode);
            }
        };
    }

    private void onMenuEmptyPop() {
        inputForwarder.set(gameInputProcessor);
        GameConstants.menuOpen = false;
    }

    @Override
    public void update(float delta) {
        game.update(delta);
        
        if (inputForwarder.get() == menuInputProcessor) {
            
            menuManager.update(delta);
            Main.getInstance().screenCamera.bind();
        
            DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), OVERLAY_COLOR);
            final Stage stage = menuManager.getStage();
            overlay.setPosition((stage.getWidth() - Main.WINDOW_WIDTH)/2 - 630, (stage.getHeight() - Main.WINDOW_HEIGHT)/2 - 730);

            Vector2 vector= stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
            hand.setPosition(vector.x-765,vector.y-1200);
            menuManager.render();
        }
    }

    @Override
    public void onEnter(BaseGameState previousState) {
        MusicManager.play(music, GameConstants.MUSIC_FADE_TIME);
    }

    @Override
    public void onEnterComplete() {
        Main.inputMultiplexer.addProcessor(inputForwarder);
        inputForwarder.set(gameInputProcessor);
        GameConstants.menuOpen = false;
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        Main.inputMultiplexer.removeProcessor(inputForwarder);
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
