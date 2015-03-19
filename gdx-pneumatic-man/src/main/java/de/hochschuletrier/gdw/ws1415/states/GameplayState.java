package de.hochschuletrier.gdw.ws1415.states;

import com.badlogic.gdx.audio.Music;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.Game;

/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends BaseGameState {

    private final Game game;
    private final Music music = Main.getInstance().getAssetManager().getMusic("menu");

    public GameplayState(AssetManagerX assetManager) {
        game = new Game();
        game.init(assetManager);
    }

    @Override
    public void update(float delta) {
        game.update(delta);
    }

    @Override
    public void onEnter(BaseGameState previousState) {
        MusicManager.play(music, 2.0f);
    }

    @Override
    public void onLeave(BaseGameState nextState) {
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
