package de.hochschuletrier.gdw.ws1415.game;

import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended.PlayMode;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TileSetAnimation;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.components.lights.ChainLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.ConeLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.DirectionalLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.PointLightComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedRenderSystem;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;
import de.hochschuletrier.gdw.ws1415.game.utils.MapLoader;
import de.hochschuletrier.gdw.ws1415.game.utils.PlatformMode;
import javafx.geometry.Pos;

public class EntityCreator {

    public static PooledEngine engine;
    public static PhysixSystem physixSystem;
    public static AssetManagerX assetManager;
    
    public static Entity createAndAddPlayer(float x, float y, float rotation) {
        Entity entity = engine.createEntity();

        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(InputComponent.class));
        entity.add(engine.createComponent(PlayerComponent.class));
        
        ParticleComponent pe = engine.createComponent(ParticleComponent.class);
        pe.particleEffect = new ParticleEffect(assetManager.getParticleEffect("laser"));
        
        pe.loop=true;
        pe.particleEffect.flipY();
        pe.particleEffect.start();
        pe.offsetY = 60f;
        pe.offsetX = -7f;
        entity.add(pe);

        //addTestParticleAndLightComponent(entity);
        
//        entity.getComponent(AnimationComponent.class).animation = new AnimationExtended(AnimationExtended.PlayMode.NORMAL, 400, );

        HealthComponent Health = engine.createComponent(HealthComponent.class);
        Health.Value = 1;
        entity.add(Health);

        float width = GameConstants.getTileSizeX() * 0.9f;
        float height = GameConstants.getTileSizeY() * 1.5f;

        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.DynamicBody,
                physixSystem).position(x - width/2, y - height/2).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        bodyComponent.getBody().setUserData(bodyComponent);
        bodyComponent.getBody().setGravityScale(1.75f);
        // Upper body
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0).restitution(0.1f)
                .shapeCircle(width * 0.1f, new Vector2(0, -height * 0.4f));
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);

       fixtureDef = new PhysixFixtureDef(physixSystem)
        .density(1f).friction(0f).restitution(0.1f)
        .shapeBox(width * 0.2f, height * 0.5f, new Vector2(0, height * 0.2f), 0);
        fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);

        fixtureDef = new PhysixFixtureDef(physixSystem)
        .density(1).friction(0).restitution(0.1f)
        .shapeCircle(width*0.4f,new Vector2(0,-height * 0.1f)).sensor(true);
        fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);


        //laser
        fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(10f).restitution(0.1f)
                .shapeCircle(width * 0.09f, new Vector2(0, GameConstants.getTileSizeY()*0.7f));
        fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        
        entity.add(bodyComponent);

        JumpComponent jumpComponent = engine.createComponent(JumpComponent.class);
        jumpComponent.jumpImpulse = 14000.0f;
        jumpComponent.restingTime = 0.001f;
        entity.add(jumpComponent);

        MovementComponent moveComponent = engine.createComponent(MovementComponent.class);
        moveComponent.speed = 20000.0f;
        entity.add(moveComponent);

        PositionComponent pos = engine.createComponent(PositionComponent.class);
        pos.scaleX = 0.5f;
        pos.scaleY = 0.5f;
        
        entity.add(pos);
        
        // ***** temporary *****
        AnimationComponent anim = engine.createComponent(AnimationComponent.class);
        anim.IsActive = true;
        anim.animation = assetManager.getAnimation("char_idle");
        entity.add(anim);
        
        JumpableAnimationComponent jumpable = engine.createComponent(JumpableAnimationComponent.class);
        jumpable.idle = anim.animation;
        jumpable.jump = assetManager.getAnimation("char_jump");
        entity.add(jumpable);
        
        LayerComponent layer = engine.createComponent(LayerComponent.class);
        layer.layer = 10; // TODO: Change later
        entity.add(layer);

        engine.addEntity(entity);
        return entity;
    }
    
    public static Entity modifyPlayerToDying(Entity entityToDie) {
        //Entity dyingEntity = engine.createEntity();
        
        entityToDie.remove(AnimationComponent.class);
        entityToDie.remove(DamageComponent.class);
        entityToDie.remove(InputComponent.class);
        entityToDie.remove(PlayerComponent.class);
        entityToDie.remove(HealthComponent.class);
        entityToDie.remove(PhysixBodyComponent.class);
        entityToDie.remove(MovementComponent.class);
        entityToDie.remove(JumpComponent.class);
        entityToDie.remove(ParticleComponent.class);
        entityToDie.remove(PointLightComponent.class);

        DeathComponent deathComponent = engine.createComponent(DeathComponent.class);
        entityToDie.add(deathComponent);
        
        AnimationComponent deathAnimation = engine.createComponent(AnimationComponent.class);
        deathAnimation.animation = assetManager.getAnimation("char_death");
        deathAnimation.isDyingPlayer = true;
        entityToDie.add(deathAnimation);


        return entityToDie;
    }

    /**
     *  Enemy FIXME: there are more different types of enemies, implement them
     */
    public static Entity createAndAddEnemy(float x, float y, Direction direction, AIType type) {
        Entity entity = engine.createEntity();

        float width = GameConstants.getTileSizeX();
        float height = GameConstants.getTileSizeY();

        DamageComponent Damage = engine.createComponent(DamageComponent.class);
        Damage.damage  = 1;
        Damage.damageToPlayer = true;
        entity.add(Damage);
        entity.add(engine.createComponent(AIComponent.class));
        entity.add(engine.createComponent(AnimationComponent.class));
        entity.add(engine.createComponent(PositionComponent.class));
        entity.add(engine.createComponent(SpawnComponent.class));

        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef pbdy = new PhysixBodyDef(BodyDef.BodyType.DynamicBody,
                physixSystem).position(x - width/2, y - height/2).fixedRotation(true);
        bodyComponent.init(pbdy, physixSystem, entity);
        PhysixFixtureDef pfx = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0f).restitution(0f)
                .shapeBox(width, height);
        Fixture fixture = bodyComponent.createFixture(pfx);
        fixture.setUserData(bodyComponent);
        entity.add(bodyComponent);
        AIComponent ai = new AIComponent();
        ai.type = type;
        entity.add(ai);

        MovementComponent movementComponent = engine.createComponent(MovementComponent.class);
        if(type == AIType.CHAMELEON)
            movementComponent.speed = 15000.0f;
        else
            movementComponent.speed = 17000.0f;
        entity.add(movementComponent);

        JumpComponent jumpComponent = engine.createComponent(JumpComponent.class);
        jumpComponent.jumpImpulse = 10000.0f;
        jumpComponent.restingTime = 0.002f;
        entity.add(jumpComponent);

        DirectionComponent d = new DirectionComponent();
        d.facingDirection = Direction.LEFT;
        entity.add(d);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity createAndAddEventBox(float x, float y) {
        Entity box = engine.createEntity();

        box.add(engine.createComponent(TriggerComponent.class));
        box.add(engine.createComponent(PositionComponent.class));
        
        float width = GameConstants.getTileSizeX();
        float height = GameConstants.getTileSizeY() * 0.4f;
        
        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody,
                physixSystem).position(x - width/2, y - height/2).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, box);
        bodyComponent.getBody().setUserData(bodyComponent);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0).restitution(0.1f)
                .shapeBox(width, height).sensor(true);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        box.add(bodyComponent);;

        engine.addEntity(box);
        return box;
    }

    public static Entity createAndAddMiner(float x, float y)
    {
        Entity Miner = engine.createEntity();

        float width = GameConstants.getTileSizeX();
        float height = GameConstants.getTileSizeY();

        Miner.add(engine.createComponent(MinerComponent.class));
        Miner.add(engine.createComponent(PositionComponent.class));
        Miner.add(engine.createComponent(HealthComponent.class));

        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef pbdy = new PhysixBodyDef(BodyDef.BodyType.DynamicBody,
                physixSystem).position(x - width/2, y - height/2).fixedRotation(true);
        bodyComponent.init(pbdy, physixSystem, Miner);
        PhysixFixtureDef pfx = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).restitution(0.1f)
                .shapeBox(width, height);
        Fixture fixture = bodyComponent.createFixture(pfx);
        fixture.setUserData(bodyComponent);
        Miner.add(bodyComponent);

        engine.addEntity(Miner);
        return(Miner);
    }

    public static Entity createAndAddGoal(float x, float y, int requiredMiners) {
        Entity goal = engine.createEntity();

        goal.add(engine.createComponent(TriggerComponent.class));
        goal.add(engine.createComponent(PositionComponent.class));
        goal.add(engine.createComponent(GoalComponent.class));
        
        goal.getComponent(GoalComponent.class).reset();
        goal.getComponent(GoalComponent.class).miners_threshold = requiredMiners;
        
        float width = GameConstants.getTileSizeX();
        float height = GameConstants.getTileSizeY() * 0.4f;
        
        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody,
                physixSystem).position(x - width/2, y - height/2).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, goal);
        bodyComponent.getBody().setUserData(bodyComponent);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0).restitution(0.1f)
                .shapeBox(width, height).sensor(true);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        goal.add(bodyComponent);;

        engine.addEntity(goal);
        return goal;
    }

    /**
     *  Indestructable Block
     */
    public static Entity createAndAddInvulnerableFloor(Rectangle rect) {
        float width = rect.width * GameConstants.getTileSizeX();
        float height = rect.height * GameConstants.getTileSizeY();
        float x = rect.x * GameConstants.getTileSizeX() + width / 2;
        float y = rect.y * GameConstants.getTileSizeY() + height / 2;

        Entity entity = engine.createEntity();

        entity.add(defineBoxPhysixBodyComponent(entity, x, y, width, height,
                true, 1f, 1f, 0.1f));

        engine.addEntity(entity);
        return entity;
    }

    /**
     * This is the Block who'll fall down onto the player
     */
    public static Entity createTrapBlock(float x, float y, int trapId) {

        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        bodyComponent.getBody().setActive(false);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).shapeBox(GameConstants.getTileSizeX(), GameConstants.getTileSizeY())
                .restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        entity.add(bodyComponent);

        FallingRockComponent rockComponent = new FallingRockComponent();
        rockComponent.id = trapId;
        entity.add(rockComponent);

        HealthComponent Health = engine.createComponent(HealthComponent.class);
        Health.Value = 1;

        DamageComponent damageComp = engine.createComponent(DamageComponent.class);
        damageComp.damage = 4;
        damageComp.damageToPlayer = true;
        damageComp.damageToTile = true;
        
        engine.addEntity(entity);
        return entity;
    }

    /**
     * This is a Sensor to trigger a falling block
     */
    public static Entity createTrapSensor(float x, float y, float dx, float dy, Entity rock) {

        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f)
                .shapeBox(dx, dy)
                .restitution(0.1f)
                .sensor(true);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        entity.add(bodyComponent);

        FallingRockTriggerComponent rockComponent = new FallingRockTriggerComponent();
        rockComponent.rockEntity = rock;
        entity.add(rockComponent);
        
        engine.addEntity(entity);
        return entity;
    }

    /**
     *  Destructable Block
     */
    public static Entity createAndAddVulnerableFloor(float x, float y, TiledMap map, TileInfo info, int health,  int tileX, int tileY) {
        Entity entity = engine.createEntity();
        
        addRenderComponents(entity, map, info, tileX, tileY, PlayMode.LOOP, true); // TODO: Change as soon as design team added the animation properties.
        
        entity.add(defineBoxPhysixBodyComponent(entity, x, y,
                GameConstants.getTileSizeX(), GameConstants.getTileSizeY(),
                true, 1f, 1f, 0.1f));

        DestructableBlockComponent blockComp = engine.createComponent(DestructableBlockComponent.class);
        entity.add(blockComp);

        HealthComponent Health = engine.createComponent(HealthComponent.class);
        Health.Value = health;
        entity.add(Health);

        engine.addEntity(entity);
        return entity;

    }

    public static Entity createAndAddLava(Rectangle rect) {
        Entity entity = engine.createEntity();

        

        float scale = 0.9f;
        float width = rect.width * GameConstants.getTileSizeX();
        float height = rect.height * GameConstants.getTileSizeY() * scale;
        float x = rect.x * GameConstants.getTileSizeX() + width / 2;
        float y = rect.y * GameConstants.getTileSizeY() + height / 2 + height * (1-scale);
        
        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody,
                physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1f).friction(1f).shapeBox(width, height)
                .restitution(0.1f);
        fixtureDef.sensor(true);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);

        KillsPlayerOnContactComponent killComponent = engine
                .createComponent(KillsPlayerOnContactComponent.class);
        entity.add(killComponent);

        PositionComponent positionComponent = engine
                .createComponent(PositionComponent.class);
        entity.add(positionComponent);

        engine.addEntity(entity);
        return entity;
    }
    
    private static PhysixBodyComponent defineBoxPhysixBodyComponent(
            Entity entity, float x, float y, float width, float height,
            boolean fixedRotation, float density, float friction,
            float restitution) {
        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody,
                physixSystem).position(x, y).fixedRotation(fixedRotation);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(density).friction(friction).shapeBox(width, height)
                .restitution(restitution);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        return bodyComponent;
    }
    
    
    /**
     *  Moveable Platform
     */
    private static Entity createPlatformBlock(float x, float y, int travelDistance, Direction dir, float speed, PlatformMode mode) {
        Entity entity = engine.createEntity();
        
        int boxWidth = GameConstants.getTileSizeX();
        int boxHeight = GameConstants.getTileSizeY();       
        
        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.KinematicBody,
                physixSystem).position(x+0.5f*GameConstants.getTileSizeX(), y+0.5f*GameConstants.getTileSizeY()).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        bodyComponent.getBody().setGravityScale(0f);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(500).friction(1f).shapeBox(boxWidth, boxHeight)
                .restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        entity.add(bodyComponent);

        PlatformComponent pl = new PlatformComponent();
        pl.travelDistance = travelDistance * GameConstants.getTileSizeX();
        pl.mode = mode;
        pl.startPos = new Vector2(x, y);
        pl.platformSpeed = speed;

        DirectionComponent d = new DirectionComponent();
        d.facingDirection = dir;

        entity.add(pl);
        entity.add(d);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity DestructablePlattformBlock(float x, float y, int travelDistance, Direction dir, float speed, PlatformMode mode, int hitpoints) {
        Entity e = createPlatformBlock(x,y, travelDistance, dir, speed, mode);
        HealthComponent h = new HealthComponent();
        h.Value = hitpoints;
        e.add(h);
        DestructableBlockComponent b = new DestructableBlockComponent();
        e.add(b);
        return e;
    }

    public static Entity IndestructablePlattformBlock(float x, float y, int travelDistance, Direction dir, float speed, PlatformMode mode) {
        return createPlatformBlock(x,y, travelDistance, dir, speed, mode);
    }

    public static Entity createSpike(int x, int y, Direction direction, TileInfo info, TiledMap map){
        Entity entity = engine.createEntity();

        if(direction == Direction.TOP)
            addRenderComponents(entity, map, info, (int)x, (int)y, PlayMode.NORMAL, false);
        else
            addRenderComponents(entity, map, info, (int)x, (int)y);

        PhysixBodyComponent bodyComponent = new PhysixBodyComponent();
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, physixSystem)
                .position((x+0.5f) * GameConstants.getTileSizeX(), (y+0.5f) * GameConstants.getTileSizeY())
                .fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        bodyComponent.getBody().setUserData(bodyComponent);
        bodyComponent.getBody().setGravityScale(0f);

        PhysixFixtureDef fixtureDefSpikeGround = new PhysixFixtureDef(physixSystem)
                .density(1)
                .friction(1f)
                .shapeBox(GameConstants.getTileSizeX() * 0.8f, GameConstants.getTileSizeY() * 0.8f,
                        direction.toVector2().scl(GameConstants.getTileSizeX() * 0.1f), 0)
                .restitution(0f)
                .sensor(true);
        Fixture fixtureSpikeGround = bodyComponent.createFixture(fixtureDefSpikeGround);
        fixtureSpikeGround.setUserData(bodyComponent);

        entity.add(bodyComponent);

        DirectionComponent directionComponent = engine.createComponent(DirectionComponent.class);
        directionComponent.facingDirection = direction;
        entity.add(directionComponent);

        DamageComponent Damage = engine.createComponent(DamageComponent.class);
        Damage.damageToPlayer = true;
        Damage.damage = 2;
        entity.add(Damage);

        entity.add(engine.createComponent(SpikeComponent.class));

        engine.addEntity(entity);

        return entity;
    }

    @Deprecated

    public static Entity createAndAddSpike(PooledEngine engine, PhysixSystem physixSystem, float x, float y, float width, float height, String direction, TiledMap map, TileInfo info, int tileX, int tileY) {
    /*    Entity entity = engine.createEntity();

        //addTestParticleAndLightComponent(entity);
        addRenderComponents(entity, map, info, tileX, tileY); 

        float angle;
        Vector2 verschiebung;
        Vector2 rayCastTarget;
        float rayLength = 50000f;
        if (direction.equals("SpikeRight")) {
            angle = (float) Math.PI / 2;
            verschiebung = new Vector2(width * 0.1f, 0);
            rayCastTarget = new Vector2(x - rayLength, y);
        } else if (direction.equals("SpikeTop")) {
            angle = (float) Math.PI;
            verschiebung = new Vector2(0, -height * 0.1f);
            rayCastTarget = new Vector2(x, y + rayLength);
        } else if (direction.equals("SpikeDown")) {
            angle = 0;
            verschiebung = new Vector2(0, height * 0.1f);
            rayCastTarget = new Vector2(x, y - rayLength);
        } else {
            angle = (float) Math.PI * 3 / 2;
            verschiebung = new Vector2(-width * 0.1f, 0);
            rayCastTarget = new Vector2(x + rayLength, y);
        }

        PhysixBodyComponent bodyComponent = new PhysixBodyComponent();
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        bodyComponent.getBody().setUserData(bodyComponent);
        bodyComponent.getBody().setGravityScale(0f);

        PhysixFixtureDef fixtureDefSpikeGround = new PhysixFixtureDef(physixSystem)
                .density(1)
                .friction(1f)
                .shapeBox(width*0.8f, height * 0.8f, verschiebung, angle)
                .restitution(0.1f)
                .sensor(true);
        Fixture fixtureSpikeGround = bodyComponent.createFixture(fixtureDefSpikeGround);
        fixtureSpikeGround.setUserData(bodyComponent);

        entity.add(bodyComponent);
        
        DamageComponent Damage = engine.createComponent(DamageComponent.class);
        Damage.damageToPlayer = true;
        Damage.damage = 2;
        entity.add(Damage);

        entity.add(engine.createComponent(SpikeComponent.class));


        engine.addEntity(entity);
        return entity;
*/
        return new Entity();
    }

    // ********** Light section BEGIN **********
    public static Entity createPointLight(float x, float y, Color color, float distance){
        Entity e = engine.createEntity();
        PositionComponent pc = engine.createComponent(PositionComponent.class);
        pc.x=x;
        pc.y=y;
        LayerComponent lc = engine.createComponent(LayerComponent.class);
        PointLightComponent plc = engine.createComponent(PointLightComponent.class);
        plc = engine.createComponent(PointLightComponent.class);
        plc.pointLight = new PointLight(engine.getSystem(SortedRenderSystem.class).getRayHandler(), GameConstants.LIGHT_RAYS, color,distance,0,0);
        
        e.add(pc);
        e.add(lc);
        e.add(plc);
        
        engine.addEntity(e);
        
        return e;
    }
    
    /**
     * EntityCreator.createDirectionalLight(obj.getX(), obj.getY(), new Color(1f, 1f, 1f, 1f), 45f);
     * @param x Pos x
     * @param y Pos y
     * @param color Color
     * @param distance Light Range
     * @param directionDegree Direction of Light
     * @param coneDegree Degree of the Cone
     * @return
     */
    
    public static Entity createConeLight(float x, float y, Color color, float distance, float directionDegree, float coneDegree){
        Entity e = engine.createEntity();
        PositionComponent pc = engine.createComponent(PositionComponent.class);
        pc.x=x;
        pc.y=y;
        LayerComponent lc = engine.createComponent(LayerComponent.class);
        ConeLightComponent clc = engine.createComponent(ConeLightComponent.class);
        clc = engine.createComponent(ConeLightComponent.class);
        clc.coneLight = new ConeLight(engine.getSystem(SortedRenderSystem.class).getRayHandler(), GameConstants.LIGHT_RAYS, color, distance, 0, 0, directionDegree, coneDegree);
        
        e.add(pc);
        e.add(lc);
        e.add(clc);
        
        engine.addEntity(e);
        
        return e;
    }
    
    /**
     * Example: EntityCreator.createChainLight(obj.getX(), obj.getY(), new Color(1f, 1f, 1f, 1f), 100f, true, new float[]{50f, -300f, 500f, -300f}); 
     * @param x Pos x
     * @param y Pos y
     * @param color Color
     * @param distance Distance
     * @param rayDirection TRUE = unten / FALSE = oben
     * @param chain Linie von Punkt zu Punkt {x1, y1, x2, y2} realtiv zu Pos x & Pos y
     * @return
     */
    public static Entity createChainLight(float x, float y, Color color, float distance, boolean rayDirection, float[] chain){
        Entity e = engine.createEntity();
        PositionComponent pc = engine.createComponent(PositionComponent.class);
        pc.x=x;
        pc.y=y;
        LayerComponent lc = engine.createComponent(LayerComponent.class);
        ChainLightComponent clc = engine.createComponent(ChainLightComponent.class);
        clc = engine.createComponent(ChainLightComponent.class);
        for(int i = 0; i<chain.length;i++){
            if(i%2 == 0){
                chain[i]=(x+chain[i])/GameConstants.BOX2D_SCALE; 
            } else {
                chain[i]=(y+chain[i])/GameConstants.BOX2D_SCALE; 
            }
        }
        clc.chainLight = new ChainLight(engine.getSystem(SortedRenderSystem.class).getRayHandler(), GameConstants.LIGHT_RAYS, color, distance, rayDirection ? 1:-1, chain);
        
        e.add(pc);
        e.add(lc);
        e.add(clc);
        
        engine.addEntity(e);
        
        return e;
    }
    
    /**
     * Example: EntityCreator.createDirectionalLight(obj.getX(), obj.getY(), new Color(1f, 1f, 1f, 1f), 45f)
     * @param x Pos x
     * @param y Pos y
     * @param color Color
     * @param directionDegree Direction of Light
     * @return
     */
    public static Entity createDirectionalLight(float x, float y, Color color, float directionDegree){
        Entity e = engine.createEntity();
        PositionComponent pc = engine.createComponent(PositionComponent.class);
        pc.x=x;
        pc.y=y;
        LayerComponent lc = engine.createComponent(LayerComponent.class);
        DirectionalLightComponent dlc = engine.createComponent(DirectionalLightComponent.class);
        dlc = engine.createComponent(DirectionalLightComponent.class);
        dlc.directionalLight = new DirectionalLight(engine.getSystem(SortedRenderSystem.class).getRayHandler(), GameConstants.LIGHT_RAYS, color, directionDegree);
        
        e.add(pc);
        e.add(lc);
        e.add(dlc);
        
        engine.addEntity(e);
        
        return e;
    }
    public static void createLavaFountain(float x, float y, float height, float intervall, 
            float intervallOffset, float length){
        Entity entity = engine.createEntity();
        
        float lavaBallSpeed = -2000.0f;
        float lavaBallSpawnIntervall = 0.5f;
        
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = x;
        position.y = y;
        entity.add(position);
        
        LavaFountainComponent lavaFountain = engine.createComponent(LavaFountainComponent.class);
        lavaFountain.height = height;
        lavaFountain.intervall = intervall;
        lavaFountain.intervallOffset = intervallOffset;
        lavaFountain.length = length;
        lavaFountain.lavaBallSpeed = lavaBallSpeed;
        lavaFountain.lavaBallSpawnIntervall = lavaBallSpawnIntervall;
        entity.add(lavaFountain);
        
        engine.addEntity(entity);
    }
    
    public static void createLavaBall(float x, float y, float lavaBallSpeed, float travelLength){
        Entity entity = engine.createEntity();
        
        float radius = GameConstants.getTileSizeX()/2;
        
        float positionX = x + GameConstants.getTileSizeX()/2;
        float positionY = y + GameConstants.getTileSizeY()/2;
        
        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.DynamicBody,
                physixSystem).position(positionX, positionY).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1f).friction(1f).shapeCircle(radius)
                .restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        entity.add(bodyComponent);
        
        MovementComponent moveComponent = engine.createComponent(MovementComponent.class);
        moveComponent.velocity.set(0, lavaBallSpeed);
        entity.add(moveComponent);
        
        DamageComponent damageComponent = engine.createComponent(DamageComponent.class);
        damageComponent.damage = 1;
        damageComponent.damageToPlayer = true;
        entity.add(damageComponent);
        
        LavaBallComponent lavaBallComponent = engine.createComponent(LavaBallComponent.class);
        lavaBallComponent.travelLength = travelLength;
        lavaBallComponent.startPositionX = positionX;
        lavaBallComponent.startPositionY = positionY;
        entity.add(lavaBallComponent);
        
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = positionX;
        position.y = positionY;
        entity.add(position);
        
        engine.addEntity(entity);
    }
    
    
    // ********** Rendering section BEGIN **********
    
    public static void addTestParticleAndLightComponent(Entity entity) {
        ParticleComponent pe = engine.createComponent(ParticleComponent.class);
        
        pe.particleEffect = new ParticleEffect(assetManager.getParticleEffect("explosion"));
        
        pe.loop=true;
        pe.particleEffect.flipY();
        pe.particleEffect.start();
        pe.offsetY = 100f;
        PointLightComponent pl = engine.createComponent(PointLightComponent.class);
        pl.pointLight = new PointLight(engine.getSystem(SortedRenderSystem.class).getRayHandler(),125,new Color(1f,0f,0f,1f),5f,0,0);
        
        entity.add(pe);
        entity.add(pl);
    }
    
    public static Entity createAndAddVisualEntity(TiledMap map, TileInfo info, int tileX, int tileY) {
    	Entity entity = engine.createEntity();
    	
    	addRenderComponents(entity, map, info, tileX, tileY);
    	
    	engine.addEntity(entity);
    	return entity;
    }
    
    public static Entity createAndAddVisualEntity(TiledMap map, TileInfo info, int tileX, int tileY, PlayMode playMode, boolean start) {
        Entity entity = engine.createEntity();
        
        addRenderComponents(entity, map, info, tileX, tileY, playMode, start);
        
        engine.addEntity(entity);
        return entity;
    }
    
    private static void addRenderComponents(Entity entity, float x, float y, int layer, float parallax, 
    		AnimationExtended animation, boolean start, float stateTime) {
        LayerComponent entityLayer = engine.createComponent(LayerComponent.class);
        entityLayer.layer = layer;
        entityLayer.parallax = parallax;
        
        
        AnimationComponent anim = engine.createComponent(AnimationComponent.class);
        anim.IsActive = start;
        anim.stateTime = stateTime;
        anim.permanent_stateTime = stateTime;
        anim.animation = animation;
        
        PositionComponent pos = engine.createComponent(PositionComponent.class);
        pos.x = x;
        pos.y = y;
        pos.rotation = 0;
        
        entity.add(pos);
        entity.add(entityLayer);
        entity.add(anim);
    }
    
    private static void addRenderComponents(Entity entity, float x, float y, int layer, float parallax, Texture texture, TextureRegion region) {
        LayerComponent entityLayer = engine.createComponent(LayerComponent.class);
        entityLayer.layer = layer;
        entityLayer.parallax = parallax;
        
        TextureComponent tex = engine.createComponent(TextureComponent.class);
        tex.texture = texture;
        tex.region = region;
        
        PositionComponent pos = engine.createComponent(PositionComponent.class);
        pos.x = x;
        pos.y = y;
        pos.rotation = 0;
        
        entity.add(pos);
        entity.add(entityLayer);
        entity.add(tex);

    }
    
    /**
     * Extracts information from the map and tile info to add components to the the given entity.
     */
    private static void addRenderComponents(Entity entity, TiledMap map, TileInfo info, int tileX, int tileY) {
    	TileSet tileset = map.findTileSet(info.globalId);
    	Texture image = (Texture) tileset.getAttachment();

        int sheetX = tileset.getTileX(info.localId);
        int sheetY = tileset.getTileY(info.localId);

        int mapTileWidth = map.getTileWidth();
        int mapTileHeight = map.getTileHeight();
        
        int tileOffsetY = tileset.getTileHeight() - mapTileHeight;

        float px = (tileX * mapTileWidth) + mapTileWidth*0.5f;
        float py = (tileY * mapTileHeight) - tileOffsetY + mapTileHeight*0.5f;
        
        int coordX = (int) (sheetX * tileset.getTileWidth()); 
        coordX += tileset.getTileMargin() + sheetX * tileset.getTileSpacing();
        int coordY = ((int) sheetY * tileset.getTileHeight());
        coordY += tileset.getTileMargin() + sheetY * tileset.getTileSpacing();                
        
        TextureRegion region = new TextureRegion(image);
        region.setRegion(coordX, coordY, tileset.getTileWidth(), tileset.getTileHeight());
        addRenderComponents(entity, px, py, 0, 0.2f, image, region);
    }
    
    /**
     * Extracts information from the map and tile info to add components to the the given entity.
     * Make sure the property "animationFrames" of the TileSet is set to greater than 1.
     */
    private static void addRenderComponents(Entity entity, TiledMap map, TileInfo info, int tileX, int tileY, PlayMode playMode, boolean start) {
    	TileSet tileset = map.findTileSet(info.globalId);
    	int frames = tileset.getIntProperty("animationFrames", 1); /// default set to 1 from 0 : editet by asset to load bomb
    			
    	assert(frames > 1);

    	Texture image = (Texture) tileset.getAttachment();
    	
    	TileSetAnimation animation = new TileSetAnimation(
                frames,
                tileset.getFloatProperty("animationDuration", 1),  /// default set to 1 from 0 : editet by asset to load bomb
                tileset.getIntProperty("animationOffset", 0));
    	
    	TextureRegion[] regions = new TextureRegion[frames];
    	float[] frameDurations = new float[frames];
    	
    	int tileOffsetY = tileset.getTileHeight() - map.getTileHeight();
    	
        float px = (tileX * map.getTileWidth()) + map.getTileWidth()*0.5f;
        float py = (tileY * map.getTileHeight()) - tileOffsetY + map.getTileHeight()*0.5f;
        
        float stateTime = tileset.getTileX(info.localId) * animation.frameDuration;
        
    	for(int i=0; i<frames; i++) {
    		tileset.updateAnimation(animation.frameDuration*i);

            int sheetX = tileset.getTileX(0);
            int sheetY = tileset.getTileY(info.localId);
            
            int coordX = (int) (sheetX * tileset.getTileWidth()); 
            coordX += tileset.getTileMargin() + sheetX * tileset.getTileSpacing();
            int coordY = (int) (sheetY * tileset.getTileHeight());
            coordY += tileset.getTileMargin() + sheetY * tileset.getTileSpacing();  
            
            regions[i] = new TextureRegion(image);
            regions[i].setRegion(coordX, coordY, tileset.getTileWidth(), tileset.getTileHeight());
            frameDurations[i] = animation.frameDuration;
    	}

    	tileset.updateAnimation(0f);
    	AnimationExtended anim = new AnimationExtended(playMode, frameDurations, regions);

    	addRenderComponents(entity, px, py, 0, 0.2f, anim, start, stateTime);
    }
    // ********** Rendering section END **********
    

   
}
