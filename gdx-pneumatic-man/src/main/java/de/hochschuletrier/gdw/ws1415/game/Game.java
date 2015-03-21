package de.hochschuletrier.gdw.ws1415.game;

import java.util.HashMap;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
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
import de.hochschuletrier.gdw.ws1415.game.components.ExplosionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.FallingRockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SpikeComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.ExplosionContactListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.FallingTrapContactListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.PlayerContactListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ws1415.game.systems.AISystem;
import de.hochschuletrier.gdw.ws1415.game.systems.CameraSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.DestroyBlocksSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.HealthSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.HudRenderSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputGamepadSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputKeyboardSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.JumpAnimationSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.LavaFountainSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.MovementSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.PlatformSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.ScoreSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedRenderSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.UpdateSoundEmitterSystem;
import de.hochschuletrier.gdw.ws1415.game.utils.InputManager;
import de.hochschuletrier.gdw.ws1415.game.utils.MapLoader;


public class Game {

    private static boolean loadSelectedLevel = false;
    private final CVarBool physixDebug = new CVarBool("physix_debug", !Main.IS_RELEASE, 0, "Draw physix debug");
    private final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);
    private final Hotkey toggleReload = new Hotkey(() -> loadSelectedLevel = true, Input.Keys.NUM_0);
    private final Hotkey toggleLight = new Hotkey(() -> Settings.LIGHTS.set(!Settings.LIGHTS.get()), Input.Keys.L);

    private  PooledEngine engine;
    private  PhysixSystem physixSystem;

    private  JumpAnimationSystem jumpAnimationSystem;
    private  PlatformSystem platformSystem = new PlatformSystem(physixSystem);
    private  ScoreSystem _ScoreSystem;
    private  HealthSystem _HealthSystem;
    private  PhysixDebugRenderSystem physixDebugRenderSystem;
    private  CameraSystem cameraSystem;
    private  RayHandler rayHandler;
    private  SortedRenderSystem renderSystem;
    private  HudRenderSystem hudRenderSystem;
    private  UpdatePositionSystem updatePositionSystem;
    private  MovementSystem movementSystem;
    private  InputKeyboardSystem inputKeyboardSystem;
    private  InputGamepadSystem inputGamepadSystem;
    private  LavaFountainSystem lavaFountainSystem;
    private  DestroyBlocksSystem destroyBlocksSystem;
    private  AISystem aisystems ;
    private InputManager inputManager = new InputManager();
    private final InputForwarder inputForwarder = new InputForwarder();
    
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
            toggleReload.register();
        }
        
        toggleLight.register();

        EntityCreator.engine = this.engine;
        EntityCreator.physixSystem = this.physixSystem;
        
    }

    public void dispose() {
        togglePhysixDebug.unregister();
        inputManager.close();
    }

    
    // Only called from MainMenu
    public void init(AssetManagerX assetManager) {
           
        System.out.println("Init called");
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
        if(engine != null)
        {
            engine.removeAllEntities();
            removeSystems();
        }
        Main.getInstance().console.unregister(physixDebug);

        addSystems();
        EntityCreator.engine = engine;
        EntityCreator.physixSystem = this.physixSystem;

        Main.getInstance().removeScreenListener(cameraSystem.getCamera());
        Main.getInstance().addScreenListener(cameraSystem.getCamera());
        
        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));
        physixDebugRenderSystem.setProcessing(physixDebug.get());
        
        // Load Map
        String file = levelFilePath;
        if(Main.cmdLine.hasOption("map")) {
            file = "data/maps/" + Main.cmdLine.getOptionValue("map") + ".tmx";
        }
        map = loadMap(file);
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        cameraSystem.adjustToMap(map);
        
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);
        setupPhysixWorld();

        addContactListeners();
        
        
        MapLoader.generateWorldFromTileMapX(engine, physixSystem, map, cameraSystem);
        // TODO: Move to better place or remove later
        //EntityCreator.createTestBackground(map.getWidth()* map.getTileWidth(), map.getHeight() * map.getTileHeight());
        EntityCreator.createTestBackground();
        inputManager.init();
    }

    private void selectPathFromSettings()
    {
        int selectedLevel = Settings.CURRENTLY_SELECTED_LEVEL.get();
        switch (selectedLevel)
        {
            case 0: 
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

        jumpAnimationSystem = new JumpAnimationSystem(assetManager, GameConstants.PRIORITY_ANIMATIONS);
        engine = new PooledEngine(GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
                GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE);
        physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE, GameConstants.VELOCITY_ITERATIONS,
            GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX);
        _ScoreSystem = new ScoreSystem();
        _HealthSystem = new HealthSystem();
        physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
        cameraSystem = new CameraSystem(GameConstants.PRIORITY_CAMERA_SYSTEM);
        rayHandler = new RayHandler(physixSystem.getWorld());
        renderSystem = new SortedRenderSystem(cameraSystem, rayHandler, GameConstants.PRIORITY_RENDER_SYSTEM);
        hudRenderSystem = new HudRenderSystem();
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
        inputForwarder.set(inputKeyboardSystem);
        
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(cameraSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(jumpAnimationSystem);
        engine.addSystem(hudRenderSystem);
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
        engine.addSystem(new UpdateSoundEmitterSystem());
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
        engine.removeSystem(aisystems);
        if(renderSystem != null && renderSystem.rayHandler != null)
            renderSystem.rayHandler.removeAll();
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
        contactListener.addListener(PlayerComponent.class, new PlayerContactListener(engine));
        contactListener.addListener(FallingRockComponent.class, new FallingTrapContactListener());
        contactListener.addListener(SpikeComponent.class, new FallingTrapContactListener());
        contactListener.addListener(ExplosionComponent.class, new ExplosionContactListener());
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
        
        if(GameConstants.pause || GameConstants.menuOpen)
        {
            renderSystem.update(0);
        }
        else
        {
            engine.update(delta);
        }


        // Level reset Testing    
        if(loadSelectedLevel){
            loadSelectedLevel = false;
            System.out.println("Restart Level"); 

            // Level Reset
            loadCurrentlySelectedLevel();
            // Controls work now
            //
            // Light cannot be reseted
            // Render-Team is on it
        }
    }

    public InputProcessor getInputProcessor() {
        return inputForwarder;
    }
}
