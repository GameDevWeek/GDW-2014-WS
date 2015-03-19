package de.hochschuletrier.gdw.ws1415.game;

import javafx.geometry.Pos;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended.PlayMode;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TileSetAnimation;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ws1415.game.components.AIComponent;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DamageComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DeathComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DestructableBlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DirectionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.FallingRockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.FallingRockTriggerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1415.game.components.JumpComponent;
import de.hochschuletrier.gdw.ws1415.game.components.KillsPlayerOnContactComponent;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlatformComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SpawnComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;
import de.hochschuletrier.gdw.ws1415.game.utils.PlatformMode;

public class EntityCreator {

    public static PooledEngine engine;
    public static PhysixSystem physixSystem;
    public static AssetManagerX assetManager;
    
    public static Entity createAndAddPlayer(float x, float y, float rotation) {
        Entity entity = engine.createEntity();

        entity.add(engine.createComponent(AnimationComponent.class));
        entity.add(engine.createComponent(PositionComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(InputComponent.class));
        entity.add(engine.createComponent(PlayerComponent.class));

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
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0).restitution(0.1f)
                .shapeBox(width, height);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).restitution(0.1f)
                .shapeCircle(10, new Vector2(0, GameConstants.getTileSizeY()*0.7f));
        fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        
        entity.add(bodyComponent);

        JumpComponent jumpComponent = engine.createComponent(JumpComponent.class);
        jumpComponent.jumpImpulse = 25000.0f;
        jumpComponent.restingTime = 0.02f;
        entity.add(jumpComponent);

        MovementComponent moveComponent = engine.createComponent(MovementComponent.class);
        moveComponent.speed = 12000.0f;
        entity.add(moveComponent);

//        DestructableBlockComponent blockComp = engine.createComponent(DestructableBlockComponent.class);
//        entity.add(blockComp);

        engine.addEntity(entity);
        return entity;
    }
    
    public static Entity modifyPlayerToDying(Entity entityToDie) {
        //Entity dyingEntity = engine.createEntity();
        
        //TODO
        //Loading new Dying-Animation - waiting for Assets
        
        entityToDie.remove(AnimationComponent.class);
//        entityToDie.remove(DamageComponent.class);
//        entityToDie.remove(InputComponent.class);
//        entityToDie.remove(PlayerComponent.class);
//        entityToDie.remove(HealthComponent.class);
//        entityToDie.remove(PhysixBodyComponent.class);
//        entityToDie.remove(MovementComponent.class);
//        entityToDie.remove(JumpComponent.class);
//        entityToDie.remove(PositionComponent.class);
        
        DeathComponent deathComponent = engine.createComponent(DeathComponent.class);
        AnimationComponent deathAnimation = engine.createComponent(AnimationComponent.class);
        deathAnimation.IsActive = true;
        deathAnimation.animation = assetManager.getAnimation("walking");
        
        entityToDie.add(deathAnimation);
        entityToDie.add(deathComponent);
        LayerComponent Layer = engine.createComponent(LayerComponent.class);
        Layer.layer = 1;
        entityToDie.add(Layer);

        return entityToDie;
    }

    /**
     *  Enemy FIXME: there are more different types of enemies, implement them
     */
    public static Entity createAndAddEnemy(float x, float y, Direction direction, AIType type) {
        Entity entity = engine.createEntity();

        float width = GameConstants.getTileSizeX();
        float height = GameConstants.getTileSizeY();

        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(AIComponent.class));
        entity.add(engine.createComponent(AnimationComponent.class));
        entity.add(engine.createComponent(PositionComponent.class));
        entity.add(engine.createComponent(SpawnComponent.class));

        PhysixBodyComponent pbc = new PhysixBodyComponent();
        PhysixBodyDef pbdy = new PhysixBodyDef(BodyDef.BodyType.DynamicBody,
                physixSystem).position(x - width/2, y - height/2).fixedRotation(true);
        pbc.init(pbdy, physixSystem, entity);
        PhysixFixtureDef pfx = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).restitution(0.1f)
                .shapeBox(width, height);
        Fixture fixture = pbc.createFixture(pfx);
        fixture.setUserData(pbdy);
        entity.add(pbc);
        AIComponent ai = new AIComponent();
        ai.type = type;
        entity.add(ai);

        DirectionComponent d = new DirectionComponent();
        d.facingDirection = direction;
        entity.add(d);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity createAndAddEventBox(float x, float y) {
        Entity box = engine.createEntity();

        box.add(engine.createComponent(TriggerComponent.class));
        box.add(engine.createComponent(PositionComponent.class));

        engine.addEntity(box);
        return box;
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

        DestructableBlockComponent blockComp = engine.createComponent(DestructableBlockComponent.class);
        entity.add(blockComp);

        engine.addEntity(entity);
        return entity;
    }
    
    public static Entity createAndAddVisualEntity(TiledMap map, TileInfo info, int tileX, int tileY) {
    	Entity entity = engine.createEntity();

    	addRenderComponents(entity, map, info, tileX, tileY);
    	
    	engine.addEntity(entity);
    	return entity;
    }

    /**
     * This is the Block who'll fall down onto the player
     */
    public static Entity createTrapBlock(float x, float y, int trapId) {

        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).shapeBox(GameConstants.getTileSizeX(), GameConstants.getTileSizeY())
                .restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        entity.add(bodyComponent);

        FallingRockComponent rockComponent = new FallingRockComponent();
        rockComponent.falling = false;
        rockComponent.id = trapId;

        engine.addEntity(entity);
        return entity;
    }

    /**
     * This is a Sensor to trigger a falling block
     */
    public static Entity createTrapSensor(float x, float y, float dx, float dy, Entity rock) {

        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.KinematicBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f)
                .shapeBox(dx, dy)
                .restitution(0.1f)
                .sensor(true);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        entity.add(bodyComponent);

        FallingRockTriggerComponent rockComponent = new FallingRockTriggerComponent();
        rockComponent.rockEntity = rock;

        engine.addEntity(entity);
        return entity;
    }

    /**
     *  Destructable Block
     */
    public static Entity createAndAddVulnerableFloor(float x, float y, TiledMap map, TileInfo info, int tileX, int tileY) {
        Entity entity = engine.createEntity();
        
        addRenderComponents(entity, map, info, tileX, tileY); // TODO: Change as soon as design team added the animation properties.
        
        entity.add(defineBoxPhysixBodyComponent(entity, x, y,
                GameConstants.getTileSizeX(), GameConstants.getTileSizeY(),
                true, 1f, 1f, 0.1f));

        DestructableBlockComponent blockComp = engine.createComponent(DestructableBlockComponent.class);
        entity.add(blockComp);

        HealthComponent Health = engine.createComponent(HealthComponent.class);
        Health.Value = 1;
        entity.add(Health);

        engine.addEntity(entity);
        return entity;

    }

    public static Entity createAndAddLava(Rectangle rect) {
        Entity entity = engine.createEntity();

        float width = rect.width * GameConstants.getTileSizeX();
        float height = rect.height * GameConstants.getTileSizeY();
        float x = rect.x * GameConstants.getTileSizeX() + width / 2;
        float y = rect.y * GameConstants.getTileSizeY() + height / 2;

        entity.add(defineBoxPhysixBodyComponent(entity, x, y,
               width, height,
                true, 1f, 1f, 0.1f));

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
        fixture.setUserData(entity);
        return bodyComponent;
    }
    
    
    /**
     *  Moveable Platform
     */
    private static Entity createPlatformBlock(float x, float y, int travelDistance, Direction dir, PlatformMode mode) {
        Entity entity = engine.createEntity();
        
        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.KinematicBody,
                physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).shapeBox(GameConstants.getTileSizeX(), GameConstants.getTileSizeY())
                .restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        entity.add(bodyComponent);

        DestructableBlockComponent blockComp = engine.createComponent(DestructableBlockComponent.class);
        entity.add(blockComp);

        PlatformComponent pl = new PlatformComponent();
        pl.travelDistance = travelDistance * GameConstants.getTileSizeX();
        pl.mode = mode;
        pl.startPos = new Vector2(x, y);

        DirectionComponent d = new DirectionComponent();
        d.facingDirection = dir;

        entity.add(pl);
        entity.add(d);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity DestructablePlattformBlock(float x, float y, int travelDistance, Direction dir, float speed, PlatformMode mode, int hitpoints) {
        Entity e = createPlatformBlock(x,y, travelDistance, dir, mode);
        HealthComponent h = new HealthComponent();
        h.Value = hitpoints;
        e.add(h);
        DestructableBlockComponent b = new DestructableBlockComponent();
        e.add(b);
        return e;
    }

    public static Entity IndestructablePlattformBlock(float x, float y, int travelDistance, Direction dir, float speed, PlatformMode mode) {
        return createPlatformBlock(x,y, travelDistance, dir, mode);
    }

    public static Entity createAndAddSpike(PooledEngine engine, PhysixSystem physixSystem, float x, float y, float width, float height, String direction, TiledMap map, TileInfo info, int tileX, int tileY) {
        Entity entity = engine.createEntity();

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
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        bodyComponent.getBody().setUserData(bodyComponent);

        PhysixFixtureDef fixtureDefSpikeGround = new PhysixFixtureDef(physixSystem)
                .density(1)
                .friction(1f)
                .shapeBox(width, height * 0.8f, verschiebung, angle)
                .restitution(0.1f)
                .sensor(true);
        Fixture fixtureSpikeGround = bodyComponent.createFixture(fixtureDefSpikeGround);
        fixtureSpikeGround.setUserData(bodyComponent);

        entity.add(bodyComponent);
        
        DamageComponent Damage = engine.createComponent(DamageComponent.class);
        Damage.damageToPlayer = true;
        Damage.damage = 2;
        entity.add(Damage);

        DestructableBlockComponent blockComp = new DestructableBlockComponent();
        entity.add(blockComp);

        engine.addEntity(entity);
        return entity;

    }
    
    
    // ********** Rendering section BEGIN **********
    
    private static void addRenderComponents(Entity entity, float x, float y, int layer, float parallax, AnimationExtended animation) {
        LayerComponent entityLayer = engine.createComponent(LayerComponent.class);
        entityLayer.layer = layer;
        entityLayer.parallax = parallax;
        
        AnimationComponent anim = engine.createComponent(AnimationComponent.class);
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
     * The frame number (param frames) should be greater than 1.
     */
    private static void addRenderComponents(Entity entity, TiledMap map, TileInfo info, int tileX, int tileY, int frames) {
    	assert(frames > 1);
    	
    	TileSet tileset = map.findTileSet(info.globalId);
    	Texture image = (Texture) tileset.getAttachment();
    	
    	TileSetAnimation animation = new TileSetAnimation(
                frames,
                tileset.getFloatProperty("animationDuration", 0),
                tileset.getIntProperty("animationTileOffset", 0));
    	
    	TextureRegion[] regions = new TextureRegion[frames];
    	float[] frameDurations = new float[frames];
    	
    	int tileOffsetY = tileset.getTileHeight() - map.getTileHeight();
    	
        float px = (tileX * map.getTileWidth());
        float py = (tileY * map.getTileHeight()) - tileOffsetY;
        
    	for(int i=0; i<frames; i++) {
            tileset.updateAnimation(animation.frameDuration*i);
            
            int sheetX = tileset.getTileX(info.localId);
            int sheetY = tileset.getTileY(info.localId);
            
            int coordX = (int) (sheetX * tileset.getTileWidth()); 
            coordX += tileset.getTileMargin() + sheetX * tileset.getTileSpacing();
            int coordY = ((int) sheetY * tileset.getTileHeight());
            coordY += tileset.getTileMargin() + sheetY * tileset.getTileSpacing();  
            
            regions[i] = new TextureRegion(image);
            regions[i].setRegion(coordX, coordY, tileset.getTileWidth(), tileset.getTileHeight());
            frameDurations[i] = animation.frameDuration;
    	}

    	tileset.updateAnimation(0f);
    	AnimationExtended anim = new AnimationExtended(PlayMode.LOOP, frameDurations, regions);
    	addRenderComponents(entity, px, py, 0, 0.2f, anim);
    }
    // ********** Rendering section END **********
}