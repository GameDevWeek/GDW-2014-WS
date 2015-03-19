package de.hochschuletrier.gdw.ws1415.sandbox.hud;

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
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxGame;



public class Hud extends SandboxGame
{
    
    private int allMiners;
  
    private SpriteBatch batch;
    private Texture textureMinerLeft;
    private Texture textureMinerFound;
    private ArrayList<Sprite> MinerLeftListe;
    private ArrayList<Sprite> MinerFoundListe;
  
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
      
      public int getTime()
      {
          return time;
      }
      
      
      
      
      private void initMinerDisplay(AssetManagerX manager)
      {
          
          MinerLeftListe = new ArrayList<>();
          MinerFoundListe = new ArrayList<>();
          
          //old (without json)
//          textureMinerLeft = new Texture(Gdx.files.internal("data/Images/KollegeIconNichtGefunden.png"));
//          textureMinerFound = new Texture(Gdx.files.internal("data/menuImages/KollegeIconGefunden.png"));          
          
          textureMinerFound = manager.getTexture("miner_found");
          textureMinerLeft = manager.getTexture("miner_notfound");
          
          
         // allMiners = game.getMinersLeft();       //oder setVisibleMiners(int miners) die vom level aufgerufen wird
          allMiners = 5;
          
          for (int x = 0; x < allMiners; x++)
          {
              MinerLeftListe.add(new Sprite(textureMinerLeft));
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
              minerFound();
          }
          //test stop Time
          if(Gdx.input.isKeyJustPressed(Keys.P))
          {
              timer.stop();
              
          }
          //test continue Time
          if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
          {
              timer.start();
          }
          //test Block destroyed Counter
          if(Gdx.input.isKeyJustPressed(Keys.B))
          {
              addDestroyedBlock();
          }
          
          
          
          int x = 5;
          for (Sprite s : MinerFoundListe)
          {
              batch.draw(s, x, 0);
              x += 70;
          }
          
          for (Sprite s : MinerLeftListe)
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
      
      
      
      public void minerFound()
      {
          if(MinerLeftListe.size() > 0)
          {
              MinerLeftListe.remove(MinerLeftListe.size()-1);
          }
          MinerFoundListe.add(new Sprite(textureMinerFound));
      }

      
      public void addDestroyedBlock()
      {
          blocks++;
      }

}
