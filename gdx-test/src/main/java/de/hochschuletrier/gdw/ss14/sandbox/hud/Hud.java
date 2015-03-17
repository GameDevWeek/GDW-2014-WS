package de.hochschuletrier.gdw.ss14.sandbox.hud;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;


import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;



public class Hud extends SandboxGame
{
    
    private int allMiners;
  
    private SpriteBatch batch;
    private Texture textureMinerLeft;
    private Texture textureMinerFound;
    private ArrayList<Sprite> MinerLeftListe;
    private ArrayList<Sprite> MinerFoundListe;
  
    private Sprite sprite;
    private Stage stage;
    private BitmapFont font;
    
    private int score = 0;
    private int time = 0;
    
    private Timer timer;
  
    
      @Override
      public void init(AssetManagerX assetManager)
      {
          batch = new SpriteBatch();
          stage = new Stage();
          font = new BitmapFont();
          timer = new Timer();
          
          
          initMinerDisplay();
          initTimeDisplay();
          initScoreDisplay();
          
      }
      
      private void initTimeDisplay()
      {
          
          Task task = timer.scheduleTask(new Task() {
              @Override
              public void run() {
                  time += 1;
              }
          }, 1, 1);
          
          
      }
      
      public int getTime()
      {
          return time;
      }
      
      
      private void initScoreDisplay()
      {
          font.setColor(Color.RED);
//          font.setScale(2, 2);
          
      }
      
      
      private void initMinerDisplay()
      {
          
          MinerLeftListe = new ArrayList<>();
          MinerFoundListe = new ArrayList<>();
          
          //besser: file aus json datei nehmen
          textureMinerLeft = new Texture(Gdx.files.internal("data/menuImages/KollegeIconNichtGefunden.png"));
          textureMinerFound = new Texture(Gdx.files.internal("data/menuImages/KollegeIconGefunden.png"));
          
          
          sprite = new Sprite(textureMinerFound);
          
         // allMiners = game.getMinersLeft();
          allMiners = 5;
          
          for (int x = 0; x < allMiners; x++)
          {
              MinerLeftListe.add(new Sprite(textureMinerLeft));
          }
          
          //test Changing when Miner is Found
          minerFound();
      }
    
      
      
    @Override
        public void update(float delta)
      {
          Gdx.gl.glClearColor(0.5f, 1, 0.5f, 1);
          Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);
   
          batch.begin();
          
          font.draw(batch, "Score: " + score, 0, 20);
          
          font.draw(batch, "Time: " + time, stage.getWidth()/2-50, 20);
          
//          sprite.draw(batch);
//          batch.draw(sprite, 200, 200);
          
          
          //test Changing when Miner is Found
          if(Gdx.input.isKeyPressed(Keys.M))
          {
              minerFound();
          }
          //test stop Time
          if(Gdx.input.isKeyPressed(Keys.T))
          {
              timer.stop();
          }
          
          
          
          int x = 10;
          for (Sprite s : MinerLeftListe)
          {
              batch.draw(s, x, stage.getHeight()-60);
              x += 60;
          }

          for (Sprite s : MinerFoundListe)
          {
              batch.draw(s, x, stage.getHeight()-60);
              x += 60;
          }

          
          batch.end();
        
      }
    
      @Override
      public void dispose()
      {
          batch.dispose();
          textureMinerFound.dispose();
          textureMinerLeft.dispose();
        
      }
      
      
      
      public void addScore(int scored)
      {
          score += scored;
      }
      
      public void minerFound()
      {
          if(MinerLeftListe.size() > 0)
          {
              MinerLeftListe.remove(MinerLeftListe.size()-1);
          }
          MinerFoundListe.add(new Sprite(textureMinerFound));
          score += 200;
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
