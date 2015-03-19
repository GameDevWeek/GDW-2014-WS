package de.hochschuletrier.gdw.ws1415.game.hud;

import java.util.ArrayList;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;





import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.Game;
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxGame;



public class Hud extends Game
{
    
    private int allMiners;
  
    private SpriteBatch batch;
    private Texture textureMinerLeft;
    private Texture textureMinerFound;
    private ArrayList<Sprite> minerLeftListe;
    private ArrayList<Sprite> minerFoundListe;
  
    private BitmapFont font;
    
    private int time = 0;
    private int blocks = 0;
    
    private Timer timer;
  
    
      @Override
      public void init(AssetManagerX assetManager)
      {
          batch = DrawUtil.batch;
          font = assetManager.getFont("orbitron");
          timer = new Timer();
          
          
          initMinerDisplay(assetManager);
          initTimeDisplay();
          
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
      
      
      
      private void initMinerDisplay(AssetManagerX manager)
      {
          
          minerLeftListe = new ArrayList<>();
          minerFoundListe = new ArrayList<>();
          
          //old (without json)
//          textureMinerLeft = new Texture(Gdx.files.internal("data/Images/KollegeIconNichtGefunden.png"));
//          textureMinerFound = new Texture(Gdx.files.internal("data/menuImages/KollegeIconGefunden.png"));          
          
          textureMinerFound = manager.getTexture("miner_found");
          textureMinerLeft = manager.getTexture("miner_notfound");
          
          
         // allMiners = game.getMinersLeft();       //oder setVisibleMiners(int miners) die vom level aufgerufen wird
          allMiners = 5;
          
          for (int x = 0; x < allMiners; x++)
          {
              minerLeftListe.add(new Sprite(textureMinerLeft));
          }
          
          //test Changing when Miner is Found
//          minerFound();
      }
    
      
      
    @Override
        public void update(float delta)
      {
          Main.getInstance().screenCamera.bind();
          
          
          font.draw(batch, "Time: " + time, Gdx.graphics.getWidth()/2-40, Gdx.graphics.getHeight()-30);
          
          font.draw(batch, "Destroyed Blocks: " + blocks, 10, Gdx.graphics.getHeight()-30);
          
          
          //test Miner found Counter
          if(Gdx.input.isKeyJustPressed(Keys.M))
          {
              addMinerFound();
          }
          //test stop Time
          if(Gdx.input.isKeyJustPressed(Keys.P))
          {
              stopHudTimer();
              
          }
          //test continue Time
          if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
          {
              startHudTimer();
          }
          //test Block destroyed Counter
          if(Gdx.input.isKeyJustPressed(Keys.B))
          {
              addDestroyedBlock();
          }
          //test reset
          if(Gdx.input.isKeyJustPressed(Keys.R))
          {
              resetHud();
          }
          
          
          
          int x = 5;
          for (Sprite s : minerFoundListe)
          {
              batch.draw(s, x, 0);
              x += 70;
          }
          
          for (Sprite s : minerLeftListe)
          {
              batch.draw(s, x, 0);
              x += 70;
          }

        
      }
    
      @Override
      public void dispose()
      {
          textureMinerFound.dispose();
          textureMinerLeft.dispose();
        
      }
      
      
      public void addMinerFound()
      {
          if(minerLeftListe.size() > 0)
          {
              minerLeftListe.remove(minerLeftListe.size()-1);
          }
          minerFoundListe.add(new Sprite(textureMinerFound));
      }

      
      public void addDestroyedBlock()
      {
          blocks++;
      }
      
      public void startHudTimer()
      {
          timer.start();
      }
      public void stopHudTimer()
      {
          timer.stop();
      }
      public void resetHud()
      {
          time = 0;
          blocks = 0;
          minerLeftListe.clear();
          minerFoundListe.clear();
      }
      public void setVisibleMiners(int miners)
      {
          allMiners = miners;
      }
      
      

}
