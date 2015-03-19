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
import de.hochschuletrier.gdw.ws1415.game.systems.*;
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
import de.hochschuletrier.gdw.ws1415.game.utils.MapLoader;
import de.hochschuletrier.gdw.ws1415.game.utils.PlatformMode;


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
    private final LavaFountainSystem lavaFountainSystem = new LavaFountainSystem(GameConstants.PRIORITY_ENTITIES+3);
    private final DestroyBlocksSystem destroyBlocksSystem = new DestroyBlocksSystem();
    private final AISystem aisystems = new AISystem(
            GameConstants.PRIORITY_PHYSIX + 1,
            physixSystem
    );

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
        
   
        MapLoader.generateWorldFromTileMap(engine, physixSystem, map, cameraSystem);

       
        inputManager.init();
       
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
        engine.addSystem(destroyBlocksSystem);
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
        if(inputKeyboardSystem.pause || (inputGamepadSystem.pause && inputGamepadSystem.active))
        {
            renderSystem.update(delta);
        }
        else
        {
            engine.update(delta);
        }
    }
}
