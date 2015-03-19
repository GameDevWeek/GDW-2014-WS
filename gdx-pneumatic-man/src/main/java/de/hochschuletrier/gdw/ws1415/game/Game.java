package de.hochschuletrier.gdw.ws1415.game;

import box2dLight.RayHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.components.FallingRockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.PlayerContactListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.RockContactListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ws1415.game.systems.AISystem;
import de.hochschuletrier.gdw.ws1415.game.systems.CameraSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.HealthSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputGamepadSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputKeyboardSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.LavaFountainSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.MovementSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.ScoreSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedRenderSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;
import de.hochschuletrier.gdw.ws1415.game.utils.InputManager;
import de.hochschuletrier.gdw.ws1415.game.utils.NoGamepadException;
import de.hochschuletrier.gdw.ws1415.game.utils.PlatformMode;






import java.nio.file.AccessDeniedException;
import java.util.HashMap;





import java.nio.file.AccessDeniedException;
import java.util.HashMap;

public class Game {

    private final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    private final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);

    private final PooledEngine engine = new PooledEngine(GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE);

    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE, GameConstants.VELOCITY_ITERATIONS,
            GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX);

    private final ScoreSystem _ScoreSystem = new ScoreSystem();
    private final HealthSystem _HealthSystem = new HealthSystem();
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final CameraSystem cameraSystem = new CameraSystem();
    private final RayHandler rayHandler = new RayHandler(physixSystem.getWorld());
    private final SortedRenderSystem renderSystem = new SortedRenderSystem(cameraSystem, rayHandler);
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1);
    private final MovementSystem movementSystem = new MovementSystem(GameConstants.PRIORITY_PHYSIX + 2);
    private final InputKeyboardSystem inputKeyboardSystem = new InputKeyboardSystem();
    private final InputGamepadSystem inputGamepadSystem = new InputGamepadSystem();
    private final AISystem aisystems = new AISystem(GameConstants.PRIORITY_PHYSIX + 1, physixSystem);
    private final LavaFountainSystem lavaFountainSystem = new LavaFountainSystem(GameConstants.PRIORITY_ENTITIES+3);

    private InputManager inputManager = new InputManager();
    
    private Sound impactSound;
    private AnimationExtended ballAnimation;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap<>();

    public Game() {
        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE) {
            togglePhysixDebug.register();
        }

        EntityCreator.engine = this.engine;
        EntityCreator.physixSystem = this.physixSystem;
        
    }

    public void dispose() {
        togglePhysixDebug.unregister();
        inputManager.close();
    }

    public void init(AssetManagerX assetManager) {
        Main.getInstance().addScreenListener(cameraSystem.getCamera());
    	
        EntityCreator.assetManager = assetManager;
                

        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));

        addSystems();

        map = loadMap("data/maps/Testkarte_19.03.tmx");
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);
        cameraSystem.adjustToMap(map);
        
        setupPhysixWorld();

        addContactListeners();
        Main.inputMultiplexer.addProcessor(inputKeyboardSystem);
        
        if(Controllers.getControllers().size > 0)
        {
            inputKeyboardSystem.setProcessing(false);
            inputGamepadSystem.setProcessing(true);
        }
        else
        {
            inputGamepadSystem.setProcessing(false);
            inputKeyboardSystem.setProcessing(true);
        }
        generateWorldFromTileMap();

        if (Controllers.getControllers().size > 0) {
            inputKeyboardSystem.setProcessing(false);
            inputGamepadSystem.setProcessing(true);
        } else {
            inputGamepadSystem.setProcessing(false);
            inputKeyboardSystem.setProcessing(true);
        }
        inputManager.init();
       
    }

    private void generateWorldFromTileMap() {
        try {
            GameConstants.setTileSizeX(map.getTileWidth());
            GameConstants.setTileSizeY(map.getTileHeight());
        }catch (AccessDeniedException e){
            e.printStackTrace();
        }
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (Layer layer, TileInfo info) -> {
                    return info.getBooleanProperty("Invulnerable", false)
                            && info.getProperty("Type", "").equals("Floor");
                },
                EntityCreator::createAndAddInvulnerableFloor);
        
        generator.generate(map,
                (Layer layer, TileInfo info) -> {
                    return info.getBooleanProperty("Invulnerable", false)
                            && info.getProperty("Type", "").equals("Lava");
                },
                EntityCreator::createAndAddLava);
        
        
        HashMap<Integer, Entity> rocks = new HashMap<>();
        for (Layer layer : map.getLayers()) {
            if(layer.isObjectLayer()){
                /// pre filtering important objects
                for(LayerObject obj : layer.getObjects()){
                    if(obj.getName().equalsIgnoreCase("Rock")){
                        int RockId = obj.getIntProperty("TriggerId", 0);
                        rocks.put(RockId, EntityCreator.createTrapBlock(obj.getX(), obj.getY(), RockId));
                    }
                }
                for(LayerObject obj : layer.getObjects()){
                    if(obj.getProperty("Name", "").equalsIgnoreCase("Platform")){
                        PlatformMode mode = PlatformMode.valueOf(obj.getProperty("Mode", PlatformMode.ALWAYS.name()).toUpperCase());
                        Direction dir = Direction.valueOf(obj.getProperty("Direction", Direction.UP.name()).toUpperCase()); // "Direction"
                        int distance = obj.getIntProperty("Distance", 0);
                        int hitpoints = obj.getIntProperty("Hitpoints", 0);
                        float speed = obj.getFloatProperty("Speed", 0);
                        if(hitpoints == 0)
                            EntityCreator.IndestructablePlattformBlock(obj.getX(), obj.getY(), distance, dir, speed, mode);
                        else
                            EntityCreator.DestructablePlattformBlock(obj.getX(), obj.getY(), distance, dir, speed, mode, hitpoints);
                    }else if(obj.getProperty("Name", "").equalsIgnoreCase("Rock")){
                        // DO NOTHING - rocks are handled prior
                    }else if(obj.getProperty("Name", "").equalsIgnoreCase("RockTrigger")){
                            int RockId = obj.getIntProperty("Id", 0);
                            Entity e = rocks.get(RockId);
                        EntityCreator.createTrapSensor(
                                obj.getX() - obj.getWidth()/2, obj.getY() + obj.getHeight()/2,
                                obj.getWidth(), obj.getHeight(), e);
                    }
                    else if(obj.getName().equalsIgnoreCase("Player")) {
                            EntityCreator.createAndAddPlayer(obj.getX(), obj.getY(), 0);
                    else if(obj.getProperty("Name", "").equalsIgnoreCase("Player")) {
                            
                    }
                    else if(obj.getProperty("Name", "").equalsIgnoreCase("PlayerSpawn")){
                        //TESTS FOR LIGHT
                        EntityCreator.createConeLight(obj.getX(), obj.getY()-500f, new Color(1f, 1f, 1f, 1f), 50f, 90f, 45f);
                        //EntityCreator.createChainLight(obj.getX(), obj.getY(), new Color(1f, 1f, 1f, 1f), 100f, true, new float[]{50f, -300f, 500f, -300f}/*new float[]{obj.getX()+20f, obj.getY()-20f,obj.getX()+40f, obj.getY()-20f}*/);
                        //EntityCreator.createDirectionalLight(obj.getX(), obj.getY(), new Color(1f, 1f, 1f, 1f), 45f);
                    }
                    else if(obj.getProperty("Name", "").equalsIgnoreCase("LevelEnd")){
                        EntityCreator.createAndAddEventBox(obj.getX(), obj.getY());
                    }
                    else if(obj.getProperty("Name", "").equalsIgnoreCase("Enemy")){
                        Direction dir = Direction.valueOf(obj.getProperty("Direction", Direction.LEFT.name()).toUpperCase());
                        AIType type = AIType.valueOf(obj.getProperty("Type", AIType.CHAMELEON.name()).toUpperCase());
                    }

                }
                // Spawning LavaFountains here
                for(LayerObject obj : layer.getObjects()){
                    if(obj.getName().equalsIgnoreCase("LavaFountain")){
                        float positionX = obj.getX();
                        float positionY = obj.getY();
                        float height = obj.getFloatProperty("Height", 1.0f);
                        float intervall = obj.getFloatProperty("Intervall", 1.0f);
                        float intervallOffset = obj.getFloatProperty("IntervallOffset", 0.0f);
                        float length = obj.getFloatProperty("Length", 1.0f);
                        EntityCreator.createLavaFountain(positionX, positionY, height, intervall, intervallOffset, length);
                    }
                }
                continue; // because it was a object layer
            }
            
            // TODO: Move this code to another class: EntityMapCreator maybe? 
            // is tile layer:
            TileInfo[][] tiles = layer.getTiles();
            for (int i = 0; i < map.getWidth(); i++) {
                for (int j = 0; j < map.getHeight(); j++) {
                    if (tiles != null && tiles[i] != null && tiles[i][j] != null) {
                        if (tiles[i][j].getIntProperty("Hitpoint", 0) != 0
                                && tiles[i][j].getProperty("Type", "").equals("Floor")) {
                            TileInfo info = tiles[i][j];
                            EntityCreator.createAndAddVulnerableFloor(
                                    i * map.getTileWidth() + 0.5f * map.getTileWidth(),
                                    j * map.getTileHeight() + 0.5f * map.getTileHeight(),
                                    map, info, i, j);
                        }
                        if (tiles[i][j].getProperty("Type", "").equals("SpikeLeft")) {
                            TileInfo info = tiles[i][j];
                            EntityCreator.createAndAddSpike(engine,
                                    physixSystem,
                                    i * map.getTileWidth() + 0.5f * map.getTileWidth(),
                                    j * map.getTileHeight() + 0.5f * map.getTileHeight(),
                                    map.getTileWidth(),
                                    map.getTileHeight(),
                                    tiles[i][j].getProperty("Type", ""),
                                    map, info, i, j);
                        }
                        if (tiles[i][j].getProperty("Type", "").equals("SpikeTop")) {
                            TileInfo info = tiles[i][j];
                            EntityCreator.createAndAddSpike(engine,
                                    physixSystem,
                                    i * map.getTileWidth() + 0.5f * map.getTileWidth(),
                                    j * map.getTileHeight() + 0.5f * map.getTileHeight(),
                                    map.getTileWidth(),
                                    map.getTileHeight(),
                                    tiles[i][j].getProperty("Type", ""),
                                    map, info, i, j);
                        }
                        if (tiles[i][j].getProperty("Type", "").equals("SpikeRight")) {
                            TileInfo info = tiles[i][j];
                            EntityCreator.createAndAddSpike(engine,
                                    physixSystem,
                                    i * map.getTileWidth() + 0.5f * map.getTileWidth(),
                                    j * map.getTileHeight() + 0.5f * map.getTileHeight(),
                                    map.getTileWidth(),
                                    map.getTileHeight(),
                                    tiles[i][j].getProperty("Type", ""),
                                    map, info, i, j);
                        }
                        if (tiles[i][j].getProperty("Type", "").equals("SpikeDown")) {
                            TileInfo info = tiles[i][j];
                            EntityCreator.createAndAddSpike(engine,
                                    physixSystem,
                                    i * map.getTileWidth() + 0.5f * map.getTileWidth(),
                                    j * map.getTileHeight() + 0.5f * map.getTileHeight(),
                                    map.getTileWidth(),
                                    map.getTileHeight(),
                                    tiles[i][j].getProperty("Type", ""),
                                    map, info, i, j);
                        }
                        if (tiles[i][j].getProperty("Type", "").equals("SpikeDown")) {
                            TileInfo info = tiles[i][j];
                            EntityCreator.createAndAddSpike(engine,
                                    physixSystem,
                                    i * map.getTileWidth() + 0.5f * map.getTileWidth(),
                                    j * map.getTileHeight() + 0.5f * map.getTileHeight(),
                                    map.getTileWidth(),
                                    map.getTileHeight(),
                                    tiles[i][j].getProperty("Type", ""),
                                    map, info, i, j);
                        }
                        if (tiles[i][j].getBooleanProperty("Invulnerable", false)
                                && tiles[i][j].getProperty("Type", "").equals("Floor")) {
                            TileInfo info = tiles[i][j];
                            EntityCreator.createAndAddVisualEntity(map, info, i, j);
                        }
                        
                        if (tiles[i][j].getBooleanProperty("Invulnerable", false)
                                && tiles[i][j].getProperty("Type", "").equals("Lava")) {
                            TileInfo info = tiles[i][j];
                            EntityCreator.createAndAddVisualEntity(map, info, i, j);
                        }
                    }
                }
            }
        }
    }

    public static TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Map konnte nicht geladen werden: " + filename);
        }
    }

    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(updatePositionSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(inputKeyboardSystem);
        engine.addSystem(inputGamepadSystem);
        engine.addSystem(aisystems);
        engine.addSystem(_HealthSystem);
        engine.addSystem(_ScoreSystem);
        engine.addSystem(lavaFountainSystem);
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
        contactListener.addListener(PlayerComponent.class, new PlayerContactListener());
        contactListener.addListener(FallingRockComponent.class, new RockContactListener());
        physixSystem.getWorld().setContactListener(contactListener);
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, GameConstants.GRAVITY_CONSTANT);
    }

    public void update(float delta) {
        // Main.getInstance().screenCamera.bind();
        // for (Layer layer : map.getLayers()) {
        // mapRenderer.render(0, 0, layer);
        // }
        //
        // mapRenderer.update(delta);
        if(inputGamepadSystem.pause || inputKeyboardSystem.pause)
        {
            renderSystem.update(delta);
        }
        else
        {
            engine.update(delta);
        }
    }
}
