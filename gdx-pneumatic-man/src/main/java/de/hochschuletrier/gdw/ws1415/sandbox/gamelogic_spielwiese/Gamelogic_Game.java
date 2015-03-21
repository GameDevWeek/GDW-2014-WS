package de.hochschuletrier.gdw.ws1415.sandbox.gamelogic_spielwiese;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;

import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.systems.AISystem;
import de.hochschuletrier.gdw.ws1415.game.systems.CameraSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.HealthSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputGamepadSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputKeyboardSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.MovementSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedRenderSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1415.game.utils.MapLoader;
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxGame;

public class Gamelogic_Game extends SandboxGame {

    private final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    private final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );

    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    

    private final HealthSystem _HealthSystem = new HealthSystem();
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final CameraSystem cameraSystem = new CameraSystem();
    private final SortedRenderSystem renderSystem = new SortedRenderSystem(cameraSystem);
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1);
    private final MovementSystem movementSystem = new MovementSystem(GameConstants.PRIORITY_PHYSIX + 2);
    private final InputKeyboardSystem inputKeyboardSystem = new InputKeyboardSystem();
    private final InputGamepadSystem inputGamepadSystem = new InputGamepadSystem();
    private final AISystem aisystems = new AISystem(
            GameConstants.PRIORITY_PHYSIX + 1,
            physixSystem
    );

    TiledMap Map;
    

    public Gamelogic_Game()
    {
        EntityCreator.engine = engine;
        EntityCreator.physixSystem = this.physixSystem;
    }

    
    @Override
    public void init(AssetManagerX assetManager) {
        Main.getInstance().addScreenListener(cameraSystem.getCamera());
        
        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));
        
        addSystems();

        // MapLoader erstelt etwas ungültiges
        MapLoader mapLoader = new MapLoader(engine, physixSystem, "data/maps/Testkarte_17.03.tmx");
        Map = mapLoader.getTiledMap();
        /*
        map = loadMap("data/maps/Testkarte_17.03.tmx");
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        */
        //mapRenderer = new TiledMapRendererGdx(map, tilesetImages);

        //setupPhysixWorld();
        //generateWorldFromTileMap();

        //addContactListeners();
        Main.inputMultiplexer.addProcessor(inputKeyboardSystem);

        Controllers.addListener(inputGamepadSystem);
        
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
    }

    @Override
    public void update(float delta) {
        engine.update(delta);

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }


    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(updatePositionSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(inputKeyboardSystem);
        engine.addSystem(inputGamepadSystem);
        engine.addSystem(aisystems);
        engine.addSystem(_HealthSystem);
    }
}
