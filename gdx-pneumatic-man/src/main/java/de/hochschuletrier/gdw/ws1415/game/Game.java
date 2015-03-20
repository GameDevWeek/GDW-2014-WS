package de.hochschuletrier.gdw.ws1415.game;

import java.util.HashMap;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.PlayerContactListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.FallingTrapContactListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ws1415.game.systems.*;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;
import de.hochschuletrier.gdw.ws1415.game.utils.InputManager;
import de.hochschuletrier.gdw.ws1415.game.utils.MapLoader;


public class Game {

    private static boolean loadSelectedLevel = false;
    private final CVarBool physixDebug = new CVarBool("physix_debug", !Main.IS_RELEASE, 0, "Draw physix debug");
    private final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);

    private  PooledEngine engine = new PooledEngine(GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE);

    private  PhysixSystem physixSystem;


    private  PlatformSystem platformSystem = new PlatformSystem(physixSystem);
    private  ScoreSystem _ScoreSystem;
    private  HealthSystem _HealthSystem;
    private  PhysixDebugRenderSystem physixDebugRenderSystem;
    private  CameraSystem cameraSystem;
    private  RayHandler rayHandler;
    private  SortedRenderSystem renderSystem;
    private  UpdatePositionSystem updatePositionSystem;
    private  MovementSystem movementSystem;
    private  InputKeyboardSystem inputKeyboardSystem;
    private  InputGamepadSystem inputGamepadSystem;
    private  LavaFountainSystem lavaFountainSystem;
    private  DestroyBlocksSystem destroyBlocksSystem;
    private  AISystem aisystems ;
    private InputManager inputManager = new InputManager();
    
    private Sound impactSound;
    private AnimationExtended ballAnimation;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap<>();
    
    private String levelFilePath = "data/maps/Testkarte_19.03.tmx";
    private AssetManagerX assetManager;

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

    
    // Only called from MainMenu
    public void init(AssetManagerX assetManager) {
             
        EntityCreator.assetManager = assetManager;
        
        this.assetManager = assetManager;
        
        selectPathFromSettings();
        
        loadCurrentlySelectedLevel();
        
        loadSelectedLevel = false;
    }

    
    // Only called from Ingame and PauseMenu (both are GameplayState)
    private void loadCurrentlySelectedLevel()
    {
        GameConstants.pause = false;
        engine.removeAllEntities();
        removeSystems();
        Main.inputMultiplexer.removeProcessor(inputKeyboardSystem);
        Main.getInstance().console.unregister(physixDebug);
        
        

        addSystems();
        EntityCreator.physixSystem = this.physixSystem;

        Main.getInstance().removeScreenListener(cameraSystem.getCamera());
        Main.getInstance().addScreenListener(cameraSystem.getCamera());
        
        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));
        physixDebugRenderSystem.setProcessing(physixDebug.get());
        
        // Load Map
        map = loadMap(levelFilePath);
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);
        setupPhysixWorld();

        addContactListeners();
        Main.inputMultiplexer.addProcessor(inputKeyboardSystem);
        
        
        MapLoader.generateWorldFromTileMapX(engine, physixSystem, map, cameraSystem);
       
        inputManager.init();
    }

    private void selectPathFromSettings()
    {
        int selectedLevel = Settings.CURRENTLY_SELECTED_LEVEL;
        switch (selectedLevel)
        {
            case 1: 
                levelFilePath = "data/maps/Testkarte_19.03.tmx";
                break;
            default:
                System.out.println("Warning: Error in Level Selection");
        }
        
//        String levelName = Settings.CURRENTLY_SELECTED_LEVEL;
//        System.out.println("CurrentlySelectedLevel: " + levelName);
//        switch (levelName)
//        {
//            case "Test":
//                levelFilePath = "data/maps/Testkarte_19.03.tmx";
//                break;
//            case "Test2":
//                levelFilePath = "data/maps/Testkarte_19.03.tmx";
//                break;
//            default:
//                System.out.println("No Level has been set");
//        }
    }

    public static TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("Map konnte nicht geladen werden: ");
            
        }
    }

    private void addSystems() {

        physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE, GameConstants.VELOCITY_ITERATIONS,
            GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX);
        _ScoreSystem = new ScoreSystem();
        _HealthSystem = new HealthSystem();
        physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
        cameraSystem = new CameraSystem();
        rayHandler = new RayHandler(physixSystem.getWorld());
        renderSystem = new SortedRenderSystem(cameraSystem, rayHandler);
        updatePositionSystem = new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1);
        movementSystem = new MovementSystem(GameConstants.PRIORITY_PHYSIX + 2);
        inputKeyboardSystem = new InputKeyboardSystem();
        inputGamepadSystem = new InputGamepadSystem();
        lavaFountainSystem = new LavaFountainSystem(GameConstants.PRIORITY_ENTITIES+3);
        destroyBlocksSystem = new DestroyBlocksSystem();
        aisystems = new AISystem(
                GameConstants.PRIORITY_PHYSIX + 1,
                physixSystem
        );
        
        
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
        engine.addSystem(platformSystem);
    }
    private void removeSystems(){
        engine.removeSystem(physixSystem);
        engine.removeSystem(physixDebugRenderSystem);
        engine.removeSystem(cameraSystem);
        engine.removeSystem(renderSystem);
        engine.removeSystem(updatePositionSystem);
        engine.removeSystem(movementSystem);
        engine.removeSystem(inputKeyboardSystem);
        engine.removeSystem(inputGamepadSystem);
        engine.removeSystem(aisystems);
        engine.removeSystem(_HealthSystem);
        engine.removeSystem(_ScoreSystem);
        engine.removeSystem(lavaFountainSystem);
        engine.removeSystem(destroyBlocksSystem);
        
        if(renderSystem != null && renderSystem.rayHandler != null)
            renderSystem.rayHandler.removeAll();

    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
        contactListener.addListener(PlayerComponent.class, new PlayerContactListener());
        contactListener.addListener(FallingRockComponent.class, new FallingTrapContactListener());
        contactListener.addListener(SpikeComponent.class, new FallingTrapContactListener());
        physixSystem.getWorld().setContactListener(contactListener);
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, GameConstants.GRAVITY_CONSTANT);
    }
    
    public static void loadLevel()
    {
        loadSelectedLevel = true;
    }

    public void update(float delta) {
        // Main.getInstance().screenCamera.bind();
        // for (Layer layer : map.getLayers()) {
        // mapRenderer.render(0, 0, layer);
        // }
        //
        // mapRenderer.update(delta);
        
        if(loadSelectedLevel==false)
        {
            if(GameConstants.pause)
            {
                renderSystem.update(0);
            }
            else
            {
                engine.update(delta);
            }
                
            
            // Level reset Testing    
            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
                System.out.println("Restart Level"); 
                
                // Level Reset
                loadCurrentlySelectedLevel();
                // Controls work now
                //
                // Light cannot be reseted
                // Render-Team is on it
            }
        }
        else
        {
            loadSelectedLevel = false;
            loadCurrentlySelectedLevel();
        }
        
    }
}
