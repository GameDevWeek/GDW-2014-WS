package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.GoalComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;

public class HudRenderSystem extends IteratingSystem implements EntityListener {

    Family goalFamily = Family.all(GoalComponent.class).get();
    private final Texture textureMinerLeft;
    private final Texture textureMinerFound;

    private final BitmapFont font;
    private Entity goal;

    public HudRenderSystem() {
        super(Family.all(PlayerComponent.class).get(), GameConstants.PRIORITY_HUD);
        final AssetManagerX assetManager = Main.getInstance().getAssetManager();
        
        font = assetManager.getFont("orbitron");
        textureMinerFound = assetManager.getTexture("miner_found");
        textureMinerLeft = assetManager.getTexture("miner_notfound");
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        
        engine.addEntityListener(goalFamily, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        if(goalFamily.matches(entity))
        {
            goal = entity;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        Main.getInstance().screenCamera.bind();

        font.draw(DrawUtil.batch, "Time: " + playerComponent.game_time, Gdx.graphics.getWidth() / 2 - 40, Gdx.graphics.getHeight() - 30);

        font.draw(DrawUtil.batch, "Destroyed Blocks: " + playerComponent.destroyed_blocks, 10, Gdx.graphics.getHeight() - 30);

        int x = 5;
        for (int i = 0; i < playerComponent.saved_miners; i++) {
            DrawUtil.draw(textureMinerFound, x, 0);
            x += 70;
        }
        
        int minimumMiners = goal.getComponent(GoalComponent.class).miners_threshold;
        int minersLeft = minimumMiners - playerComponent.saved_miners;
        for (int i = 0; i < minersLeft; i++) {
            DrawUtil.draw(textureMinerLeft, x, 0);
            x += 70;
        }
    }
}
