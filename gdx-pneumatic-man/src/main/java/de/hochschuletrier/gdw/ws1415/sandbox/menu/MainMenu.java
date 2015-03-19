package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1415.states.GameplayState;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.RotatingDecoImage;

import java.awt.Cursor;


public class MainMenu extends MenuPage{
    
    DecoImage start=new DecoImage(assetManager.getTexture("start_button"));
    DecoImage optionen=new DecoImage(assetManager.getTexture("optionen_button"));
    DecoImage credits=new DecoImage(assetManager.getTexture("credits_button"));
    DecoImage ende=new DecoImage(assetManager.getTexture("beenden_button"));
    //DecoImage hand=new DecoImage(assetManager.getTexture("zeigefinger"));

    public enum Type {
        MAINMENU,
        INGAME
    }
    
    public MainMenu(Skin skin, MenuManager menuManager, Type type)
    {
        super(skin,"background_menu" );
        int x=960;
        int i=0;
        int y=510;
        int yStep=100;
        
            addPageEntryStart(menuManager,x,y-yStep*(i++),start);
            addPageEntry(menuManager,x,y-yStep*(i++),optionen, new OptionMenu(skin, menuManager));
            addPageEntry(menuManager,x,y-yStep*(i++),credits, new OptionMenu(skin, menuManager));
            //addPageEntry(menuManager,x,y-yStep*(i++),ende, new OptionMenu(skin, menuManager));
            addCenteredImage((int)(x-(ende.getWidth()/2)),y-yStep*(i++),(int)ende.getWidth(),(int)ende.getHeight()/2,ende,()->System.exit(-1));
        //addCenteredButton(menuManager.getWidth() - 80, 54, 100, 40, "Testbutton", () -> System.exit(-1));
            //addGear(hand);
            
        
    }
    
    protected final void addPageEntry(MenuManager menuManager, int x, int y, DecoImage image, MenuPage page) {
        menuManager.addLayer(page);
        
        addCenteredImage((int)(x-(image.getWidth()/2)), y, (int)image.getWidth(), (int)image.getHeight(), image, () -> menuManager.pushPage(page));
        
    }
    
    protected final void addPageEntryStart(MenuManager menuManager, int x, int y, DecoImage image) {
        //menuManager.addLayer(page);
        
        addCenteredImage((int)(x-(image.getWidth()/2)), y, (int)image.getWidth(), (int)image.getHeight(), image, () ->  main.changeState(new GameplayState(assetManager),new SplitHorizontalTransition(500), null));
        
    }
   /*private void addGear(DecoImage image) {
        //final Texture texture = assetManager.getTexture(name);
        //final DecoImage decoImage = new DecoImage(texture);
        //
        addListener(new InputListener() {
            public void mouseMoved(MouseEvent e) {
                image.setPosition(MouseInfo.getPointerInfo().getLocation().x,MouseInfo.getPointerInfo().getLocation().y); 
               
            }
        });
        addActor(image);
    }*/
    
}
