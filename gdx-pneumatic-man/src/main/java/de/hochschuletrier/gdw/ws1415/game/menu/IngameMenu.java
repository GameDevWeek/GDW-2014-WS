package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1415.states.GameplayState;

public class IngameMenu extends MenuPage
{
    
    DecoImage backToGame = new DecoImage(assetManager.getTexture("resume_button_inactive"));
    DecoImage restart = new DecoImage(assetManager.getTexture("restart_button_inactive"));
    DecoImage optionen = new DecoImage(assetManager.getTexture("optionen_button"));
    DecoImage mainMenu = new DecoImage(assetManager.getTexture("mainmenu_button_inactive"));
    DecoImage quit = new DecoImage(assetManager.getTexture("quit_button_inactive"));
    
    public enum Type
    {
        MAINMENU,
        INGAME
    }

    public IngameMenu(Skin skin, MenuManager menumanager, Type type)
    {
        super(skin, "background_menu"); //später zu Ingame Hintergrund ändern!
        int x=960;
        int i=0;
        int y=800;
        int yStep=100;
        
//            addPageEntry(menumanager,x,y-yStep*(i++),backToGame, new OptionMenu(skin, menumanager));   //Resume Game
            addPageEntryStart(menumanager,x,y-yStep*(i++), backToGame);
//            addPageEntry(menumanager,x,y-yStep*(i++),restart, new OptionMenu(skin, menumanager));           //Restart Level
            addPageEntryStart(menumanager,x,y-yStep*(i++), restart);
            addPageEntry(menumanager,x,y-yStep*(i++),optionen, new OptionMenu(skin, menumanager));
            addPageEntry(menumanager,x,y-yStep*(i++),mainMenu, new MainMenu(skin, menumanager, MainMenu.Type.MAINMENU));
            addPageEntry(menumanager,x,y-yStep*(i++),quit, new OptionMenu(skin, menumanager));
            addCenteredImage((int)(x-(quit.getWidth()/2)),y-yStep*(i++),(int)quit.getWidth(),(int)quit.getHeight()/2,quit,()->System.exit(-1));
    }
    
    protected final void addPageEntry(MenuManager menuManager, int x, int y, DecoImage image, MenuPage page) {
        menuManager.addLayer(page);
        
        addCenteredImage((int)(x-(image.getWidth()/2)), y, (int)image.getWidth(), (int)image.getHeight(), image, () -> menuManager.pushPage(page));
        
    }
    
    protected final void addPageEntryStart(MenuManager menuManager, int x, int y, DecoImage image) {
        //menuManager.addLayer(page);
        
        addCenteredImage((int)(x-(image.getWidth()/2)), y, (int)image.getWidth(), (int)image.getHeight(), image, () ->  main.changeState(new GameplayState(assetManager),new SplitHorizontalTransition(500), null));
        
    }

}
