package de.hochschuletrier.gdw.ws1415;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationExtendedLoader;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.TiledMapLoader;
import de.hochschuletrier.gdw.commons.gdx.devcon.DevConsoleView;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundDistanceModel;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundInstance;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyManager;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.GdxResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.ws1415.game.systems.UpdateSoundEmitterSystem;
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxCommand;
import de.hochschuletrier.gdw.ws1415.states.LoadGameState;
import de.hochschuletrier.gdw.ws1415.states.MainMenuState;
import de.hochschuletrier.gdw.ws1415.states.WinState;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 * 
 * @author Santo Pfingsten
 */
public class Main extends StateBasedGame {
    
    public static CommandLine cmdLine;

    public static final boolean IS_RELEASE = ClassUtils.getClassUrl(Main.class).getProtocol().equals("jar");


    public static final int WINDOW_HEIGHT = 1080;
    public static final int WINDOW_WIDTH = 1920;

    private static final AssetManagerX assetManager = new AssetManagerX();
    private static Main instance;
    private static LwjglApplicationConfiguration appCfg;

    public final DevConsole console = new DevConsole(16);
    private final DevConsoleView consoleView = new DevConsoleView(console);
    private Skin skin;
    public static final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    public Main() {
        super(new BaseGameState());
    }

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    private void setupDummyLoader() {
    }

    private void loadAssetLists() {
        TextureParameter param = new TextureParameter();
        param.minFilter = param.magFilter = Texture.TextureFilter.Linear;

        assetManager.loadAssetList("data/json/images.json", Texture.class, param);
        assetManager.loadAssetList("data/json/sounds.json", Sound.class, null);
        assetManager.loadAssetList("data/json/music.json", Music.class, null);
        assetManager.loadAssetList("data/json/particles.json", ParticleEffect.class, null);
        assetManager.loadAssetListWithParam("data/json/animations.json", AnimationExtended.class,
                AnimationExtendedLoader.AnimationExtendedParameter.class);
        BitmapFontParameter fontParam = new BitmapFontParameter();
        fontParam.flip = true;
        assetManager.loadAssetList("data/json/fonts.json", BitmapFont.class, fontParam);
    }

    private void setupGdx() {
        KeyUtil.init();
        Gdx.graphics.setContinuousRendering(true);

        Gdx.input.setCatchMenuKey(true);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void create() {
        CurrentResourceLocator.set(new GdxResourceLocator(Files.FileType.Local));
        DrawUtil.init();
        setupDummyLoader();
        loadAssetLists();
        setupGdx();
        SoundInstance.init();
        
        skin = new Skin(Gdx.files.internal("data/skins/sotf.json"));
        consoleView.init(skin);
        addScreenListener(consoleView);
        inputMultiplexer.addProcessor(consoleView.getInputProcessor());
        inputMultiplexer.addProcessor(HotkeyManager.getInputProcessor());

        changeState(new LoadGameState(assetManager, this::onLoadComplete), null, null);
        
        SoundEmitter.setGlobalVolume(Settings.SOUND_VOLUME.get());
        MusicManager.setGlobalVolume(Settings.MUSIC_VOLUME.get());
        
        UpdateSoundEmitterSystem.initCVars();
    }

    private void onLoadComplete() {
        final MainMenuState mainMenuState = new MainMenuState(assetManager);
        addPersistentState(mainMenuState);
        
        final WinState winState = new WinState(assetManager);
        addPersistentState(winState);
        
        changeState(mainMenuState, null, null);
        SandboxCommand.init(assetManager);
        
        if (cmdLine.hasOption("sandbox")) {
            SandboxCommand.runSandbox(cmdLine.getOptionValue("sandbox"));
        }
        Gdx.graphics.setVSync(true);
        appCfg.foregroundFPS = 60;
        appCfg.backgroundFPS = 60;
    }

    @Override
    public void dispose() {
        super.dispose();
        DrawUtil.batch.dispose();
        consoleView.dispose();
        skin.dispose();
        SoundEmitter.disposeGlobal();
    }

    protected void preRender() {
        DrawUtil.clearColor(Color.BLACK);
        DrawUtil.clear();
        DrawUtil.resetColor();

        DrawUtil.batch.begin();
    }

    protected void postRender() {
        DrawUtil.batch.end();
        if (consoleView.isVisible()) {
            consoleView.render();
        }
    }
    
    public AssetManagerX  getAssetManager(){
        return assetManager;
    }
    
    public Skin getSkin() {
        return skin;
    }

    @Override
    protected void preUpdate(float delta) {
        if (consoleView.isVisible()) {
            consoleView.update(delta);
        }
        console.executeCmdQueue();
        SoundEmitter.updateGlobal();

        preRender();
    }

    @Override
    protected void postUpdate(float delta) {
    	MusicManager.update(delta);
        postRender();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public static void main(String[] args) {
        parseOptions(args);
        
        appCfg = new LwjglApplicationConfiguration();
        appCfg.title = "LibGDX Test";
        appCfg.width = WINDOW_WIDTH;
        appCfg.height = WINDOW_HEIGHT;
        if (cmdLine.hasOption("width")) {
            try {
                appCfg.width = Integer.parseInt(cmdLine.getOptionValue("width"));
            } catch(NumberFormatException e) {
                System.out.println("Width must be of type integer");
            }
        }
        if (cmdLine.hasOption("height")) {
            try {
                appCfg.height = Integer.parseInt(cmdLine.getOptionValue("height"));
            } catch(NumberFormatException e) {
                System.out.println("Height must be of type integer");
            }
        }
        appCfg.useGL30 = false;
        appCfg.vSyncEnabled = false;
        appCfg.foregroundFPS = -1;
        appCfg.backgroundFPS = -1;

        new LwjglApplication(getInstance(), appCfg);
    }

    private static void parseOptions(String[] args) throws IllegalArgumentException {
        CommandLineParser cmdLineParser = new PosixParser();

        Options options = new Options();
        createOption(options, "sandbox", "Start a Sandbox Game", "Sandbox Classname");
        createOption(options, "width", "Window width override", "Width in Pixels");
        createOption(options, "height", "Window height override", "Height in Pixels");
        createOption(options, "map", "Map override", "Map file without path and extension");

        try {
            cmdLine = cmdLineParser.parse(options, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createOption(Options options, String name, String description, String argName) throws IllegalArgumentException {
        options.addOption(OptionBuilder.withLongOpt(name)
                .withDescription(description)
                .withType(String.class)
                .hasArg()
                .withArgName(argName)
                .create());
    }
}
