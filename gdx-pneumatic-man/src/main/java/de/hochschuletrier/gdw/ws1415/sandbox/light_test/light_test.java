package de.hochschuletrier.gdw.ws1415.sandbox.light_test;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.ParticleComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.PointLightComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.CameraSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedRenderSystem;
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxGame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class light_test extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(light_test.class);

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );
    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final CameraSystem camera = new CameraSystem();
    
    private final RayHandler rayHandler = new RayHandler(physixSystem.getWorld());
    private final SortedRenderSystem sortedRenderSystem = new SortedRenderSystem(camera, rayHandler);
    private PhysixBodyComponent playerBody;
    
    PositionComponent move;
    public light_test() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(sortedRenderSystem);
        physixSystem.setGravity(0f, 0f);
    }

    @Override
    public void init(AssetManagerX assetManager) {
        Entity player = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        player.add(modifyComponent);

        modifyComponent.schedule(() -> {
            playerBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).fixedRotation(true);
            playerBody.init(bodyDef, physixSystem, player);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0f).restitution(0.0001f).shapeBox(58, 90);
            playerBody.createFixture(fixtureDef);
            player.add(playerBody);
        }); 
        
       PositionComponent pos = engine.createComponent(PositionComponent.class);
       pos.x =50f;
       pos.y =50f;
       move = pos;
       player.add(pos);
       
       ParticleComponent pe = engine.createComponent(ParticleComponent.class);
       
       pe.particleEffect = new ParticleEffect();
       
       pe.particleEffect.load(Gdx.files.internal("src/main/resources/data/particle/test.p"),Gdx.files.internal("src/main/resources/data/particle/"));
       pe.loop=true;
       pe.particleEffect.flipY();
       pe.particleEffect.start();
       
       player.add(pe);
      
       PointLightComponent pl = engine.createComponent(PointLightComponent.class);
       pl.pointLight = new PointLight(this.sortedRenderSystem.getRayHandler(),360,new Color(1f,0f,0f,1f),50f,20f,20f);
       player.add(pl);
       
       
       
       LayerComponent ly = engine.createComponent(LayerComponent.class);
       ly.layer = 0;
       ly.parallax = 1;
       player.add(ly);
       camera.getCamera().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
       camera.follow(player);
       engine.addEntity(player);
    }

 

    @Override
    public void dispose() {
     
    }

 
    @Override
    public void update(float delta) {
        float speed = 10000.0f;
        float velX = 0, velY = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velX -= delta * speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velX += delta * speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velY -= delta * speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velY += delta * speed;
        }

        playerBody.setLinearVelocity(velX, velY);

        move.x = playerBody.getPosition().x;
        move.y = playerBody.getPosition().y;
        
        engine.update(delta);
    }
    
}

