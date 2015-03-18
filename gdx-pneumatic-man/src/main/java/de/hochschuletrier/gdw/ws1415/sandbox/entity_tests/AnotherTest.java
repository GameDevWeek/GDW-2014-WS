package de.hochschuletrier.gdw.ws1415.sandbox.entity_tests;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationExtendedLoader;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SpawnComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.CameraSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedRenderSystem;
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxGame;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnotherTest extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(AnotherTest.class);

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

    private TiledMap map;
    private PhysixBodyComponent playerBody;
    private final CameraSystem cameraSystem = new CameraSystem();
    private final SortedRenderSystem  renderSystem = new SortedRenderSystem(cameraSystem);
    
    private Entity player;
    
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap<>();
    
    public AnotherTest() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(renderSystem);
    }

    @Override
    public void init(AssetManagerX assetManager) {
        map = loadMap("data/maps/demo.tmx");
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            Texture tex = new Texture(filename);
            tileset.setAttachment(tex);
            tilesetImages.put(tileset, tex);
        }
        
        for(Layer layer : map.getLayers()) {
        	TileInfo[][] tiles = layer.getTiles();
        	
            for (int x = 0; x < map.getWidth(); x++) 
                for (int y = 0; y < map.getHeight(); y++)  {
                	TileInfo info = tiles[x][y];
                	
                	if(info == null)
                		continue;

                    TileSet tileset = map.findTileSet(info.globalId);
                    Texture image = (Texture) tileset.getAttachment();
                    
                    int sheetX = tileset.getTileX(tiles[x][y].localId);
                    int sheetY = tileset.getTileY(tiles[x][y].localId);

                    int mapTileWidth = map.getTileWidth();
                    int mapTileHeight = map.getTileHeight();
                    
                    int tileOffsetY = tileset.getTileHeight() - mapTileHeight;

                    float px = (x * mapTileWidth);
                    float py = (y * mapTileHeight) - tileOffsetY;
                    
                    int coordX = (int) (sheetX * tileset.getTileWidth()); 
                    coordX += tileset.getTileMargin() + sheetX * tileset.getTileSpacing();
                    int coordY = ((int) sheetY * tileset.getTileHeight());
                    coordY += tileset.getTileMargin() + sheetY * tileset.getTileSpacing();                

                    TextureRegion region = new TextureRegion(image);
                    region.setRegion(coordX, coordY, tileset.getTileWidth(), tileset.getTileHeight());
                    createTileEntity(px, py, 0, 0.2f, image, region);
                }
        }

        //Create a SpawnPoint
        Entity spawn = engine.createEntity();
        PositionComponent spawnPoint = engine.createComponent(PositionComponent.class);
        spawnPoint.x = 200;
        spawnPoint.y = 100;
        SpawnComponent spawnflag = engine.createComponent(SpawnComponent.class);
        spawnflag.reset();
        spawn.add(spawnflag);
        spawn.add(spawnPoint);

        engine.addEntity(spawn);

        // create a simple player ball
        player = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        player.add(modifyComponent);

        modifyComponent.schedule(() -> {
            playerBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(spawn.getComponent(PositionComponent.class).x,
                    spawn.getComponent(PositionComponent.class).y).fixedRotation(true);
            playerBody.init(bodyDef, physixSystem, player);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(30);
            playerBody.createFixture(fixtureDef);
            player.add(playerBody);
        });
        
        PositionComponent posComponent = spawn.getComponent(PositionComponent.class);
        player.add(posComponent);
        
        AnimationComponent animComp = engine.createComponent(AnimationComponent.class);
        animComp.reset();
        assetManager.loadAssetListWithParam("data/json/animations.json", AnimationExtended.class,
        AnimationExtendedLoader.AnimationExtendedParameter.class);        
        animComp.animation = assetManager.getAnimation("walking");
        player.add(animComp);
        
        LayerComponent layer = engine.createComponent(LayerComponent.class);
        layer.layer = 5;
        layer.parallax = 1f;
        player.add(layer);
        
        engine.addEntity(player);

        addBackgroundEntity(0, 0, 2, 1f, assetManager);
        
        addBackgroundEntity(100.f, 100.f, 1, 0.5f, assetManager);
        
        // Setup camera
        Main.getInstance().addScreenListener(cameraSystem.getCamera());
        cameraSystem.adjustToMap(map);
        
        cameraSystem.follow(player);
    }
    
    private void createTileEntity(float x, float y, int layer, float parallax, Texture texture, TextureRegion region) {
    	Entity tileEntity = engine.createEntity();
    	
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
        
        tileEntity.add(pos);
        tileEntity.add(entityLayer);
        tileEntity.add(tex);
        
        engine.addEntity(tileEntity);
    }
    
    private void addBackgroundEntity(float x, float y, int layer, float parallax, AssetManagerX assetManager) {
        Entity backgroundEntity = engine.createEntity();
        LayerComponent backgroundLayer = engine.createComponent(LayerComponent.class);
        backgroundLayer.layer = layer;
        backgroundLayer.parallax = parallax;
        
        TextureComponent backgroundTex = engine.createComponent(TextureComponent.class);
        backgroundTex.texture = assetManager.getTexture("logo");
        
        PositionComponent backgroundPos = engine.createComponent(PositionComponent.class);
        backgroundPos.x = x;
        backgroundPos.y = y;
        backgroundPos.rotation = 0;
        
        backgroundEntity.add(backgroundPos);
        backgroundEntity.add(backgroundLayer);
        backgroundEntity.add(backgroundTex);
        
        engine.addEntity(backgroundEntity);
    }

    @Override
    public void dispose() {
        Main.getInstance().removeScreenListener(cameraSystem.getCamera());
        tilesetImages.values().forEach(Texture::dispose);
    }

    public TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

    @Override
    public void update(float delta) {
        engine.update(delta);

        if (playerBody != null) {
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
            PositionComponent pos = player.getComponent(PositionComponent.class);
            pos.x = playerBody.getPosition().x;
            pos.y = playerBody.getPosition().y;
        }
    }
}

