package de.hochschuletrier.gdw.ss14.sandbox.hud;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;


public class Hud extends SandboxGame
{
    
  private int allMiners;
  
  private SpriteBatch batch;
  private Texture texture;
  private Sprite sprite;
  private Stage stage;
  
      @Override
      public void init(AssetManagerX assetManager)
      {
          batch = new SpriteBatch();
          texture = new Texture(Gdx.files.internal("data/menuImages/dummyMiner.png"));
          sprite = new Sprite(texture);
          stage = new Stage();
         // allMiners = getMinersLeft();
          allMiners = 5;
          //loadMinersPictures();
        
          
      }
    

    @Override
    public void update(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
   
        batch.begin();
        sprite.draw(batch);
        batch.end();
        
    }
    
    @Override
    public void dispose()
    {
        batch.dispose();
        texture.dispose();
        
    }
    
    public void minerRescued()
    {
        
        
    }
    
 /*   
    
    protected Main main = Main.getInstance();
    protected AssetManagerX assetManager;
//    protected final Skin skin;
    
    
    
    private int allMiners;
    
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;
    
//    private Stage stage = new Stage();
    
    public Hud()
    {
//        allMiners = getMinersLeft();
        allMiners = 5;
        stage = new Stage();
        loadMinersPictures();
        
    }
    
    
    public void loadMinersPictures()
    {

        
        while (allMiners > 0 )
        {
            
        }
        
        batch = new SpriteBatch();
        
        sprite = new Sprite(texture);
        
        
    }



    @Override
    public void dispose()
    {
        batch.dispose();
        texture.dispose();
        
    }
    
    public void render() {        
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }



    @Override
    public void init(AssetManagerX assetManager)
    {
        this.assetManager = assetManager;
        
        
    }



    @Override
    public void update(float delta)
    {
        // TODO Auto-generated method stub
        
    }
    
 */   
    
    
/*
    
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "LibGDX Test";
        cfg.width = 1920;
        cfg.height = 1080;
        cfg.useGL30 = false;
        cfg.vSyncEnabled = true;
        cfg.foregroundFPS = 60;
        cfg.backgroundFPS = 60;
        
        
//        initApplication();
        
        new LwjglApplication(getInstance(), cfg);
    }
    
    private static Main instance;
    
    public static Main getInstance() {
        if (instance == null) {
            instance = new Main(); // hello
        }
        return instance;
    }
    
*/

}
