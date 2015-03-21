package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.GoalComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;

public class HudRenderSystem extends IteratingSystem implements EntityListener {

    private final CVarBool showFps = new CVarBool("hud_showFps", true, 0, "Show FPS");
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
    Family goalFamily = Family.all(GoalComponent.class).get();
    private final Texture textureMinerLeft;
    private final Texture textureMinerFound;
    private final Texture textureHudStein;
    private final Texture textureHudTime;

    private final BitmapFont font;
    private Entity goal;
    
    private TextBounds blockbound;
    private TextBounds timebound;
    

    public HudRenderSystem() {
        super(Family.all(PlayerComponent.class).get(), GameConstants.PRIORITY_HUD);
        final AssetManagerX assetManager = Main.getInstance().getAssetManager();
        
        font = assetManager.getFont("orbitron_32");
        font.setColor(Color.valueOf("ffe601"));
        textureMinerFound = assetManager.getTexture("miner_found");
        textureMinerLeft = assetManager.getTexture("miner_notfound");
        
        textureHudStein = assetManager.getTexture("hud_ico_stein");
        textureHudTime = assetManager.getTexture("hud_ico_time");
        
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        
        engine.addEntityListener(goalFamily, this);
        Main.getInstance().console.register(showFps);
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
        Main.getInstance().console.unregister(showFps);
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
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if(showFps.get()) {
            fpsCalc.addFrame();
            
            String str = String.format("%.2f FPS", fpsCalc.getFps());
            float width = font.getBounds(str).width;
            font.draw(DrawUtil.batch, str, Gdx.graphics.getWidth()-width, 0);
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent playerComponent = entity.getComponent(PlayerComponent.class);
        Main.getInstance().screenCamera.bind();
        
        DrawUtil.draw(textureHudStein, 44, 22);
        DrawUtil.draw(textureHudTime, Gdx.graphics.getWidth() / 2 - textureHudTime.getWidth()/2, 22);

        
        blockbound = font.getBounds("" + playerComponent.destroyed_blocks);
        timebound = font.getBounds("" + playerComponent.game_time);
        
        //with text bounding
//        font.draw(DrawUtil.batch, "" + playerComponent.game_time, Gdx.graphics.getWidth() / 2 - 5 - timebound.width/2, 47);
//        font.draw(DrawUtil.batch, "" + playerComponent.destroyed_blocks, 85 - blockbound.width/2, 47);
        
        
        //without text bounding
        font.draw(DrawUtil.batch, "" + playerComponent.game_time, Gdx.graphics.getWidth() / 2 - 20, 47);
//        font.draw(DrawUtil.batch, "" + playerComponent.destroyed_blocks, 68, 47);
        
        
        //with cases for big blocknumber
        if(playerComponent.destroyed_blocks >= 20)
        {
            font.draw(DrawUtil.batch, "" + playerComponent.destroyed_blocks, 53, 47);
        }
        else if(playerComponent.destroyed_blocks >= 10)
        {
            font.draw(DrawUtil.batch, "" + playerComponent.destroyed_blocks, 60, 47);
        }
        else
        {
            font.draw(DrawUtil.batch, "" + playerComponent.destroyed_blocks, 68, 47);
        }

            
        int x = 40;
        for (int i = 0; i < playerComponent.saved_miners; i++) {
            DrawUtil.draw(textureMinerFound, x, Gdx.graphics.getHeight() - 75);
            x += 70;
        }
        
        int minimumMiners = goal.getComponent(GoalComponent.class).miners_threshold;
        int minersLeft = minimumMiners - playerComponent.saved_miners;
        for (int i = 0; i < minersLeft; i++) {
            DrawUtil.draw(textureMinerLeft, x, Gdx.graphics.getHeight() - 75);
            x += 70;
        }
    }
}
