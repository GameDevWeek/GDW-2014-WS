package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;

public class IngameMenu extends MenuPage
{
    
    DecoImage backToGame = new DecoImage(assetManager.getTexture("start_button"));
    DecoImage restart = new DecoImage(assetManager.getTexture("credits_button"));
    DecoImage optionen = new DecoImage(assetManager.getTexture("optionen_button"));
    DecoImage mainMenu = new DecoImage(assetManager.getTexture("credits_button"));
    DecoImage quit = new DecoImage(assetManager.getTexture("beenden_button"));
    
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
        
            addPageEntry(menumanager,x,y-yStep*(i++),backToGame, new OptionMenu(skin, menumanager));
            addPageEntry(menumanager,x,y-yStep*(i++),restart, new OptionMenu(skin, menumanager));
            addPageEntry(menumanager,x,y-yStep*(i++),optionen, new OptionMenu(skin, menumanager));
            addPageEntry(menumanager,x,y-yStep*(i++),mainMenu, new OptionMenu(skin, menumanager));
            addPageEntry(menumanager,x,y-yStep*(i++),quit, new OptionMenu(skin, menumanager));
    }
    
    protected final void addPageEntry(MenuManager menuManager, int x, int y, DecoImage image, MenuPage page) {
        menuManager.addLayer(page);
        
        addCenteredImage((int)(x-(image.getWidth()/2)), y, (int)image.getWidth(), (int)image.getHeight(), image, () -> menuManager.pushPage(page));
        
    }

}
