package de.hochschuletrier.gdw.ss14.sandbox.hud;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import de.hochschuletrier.gdw.ss14.sandbox.maptest.MapTest;



public class Hud extends MapTest
{
    
    private int allMiners;
    private Stage stage;
    
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
            //Lade Bilder!
            
        }
        
        
        
        
    }



    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }



    @Override
    public void init(AssetManagerX assetManager)
    {
        // TODO Auto-generated method stub
        
    }



    @Override
    public void update(float delta)
    {
        // TODO Auto-generated method stub
        
    }
    
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "LibGDX Test";
        cfg.width = WINDOW_WIDTH;
        cfg.height = WINDOW_HEIGHT;
        cfg.useGL30 = false;
        cfg.vSyncEnabled = true;
        cfg.foregroundFPS = 60;
        cfg.backgroundFPS = 60;

        parseOptions(args);
        new LwjglApplication(getInstance(), cfg);
    }


}
