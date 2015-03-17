package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.RotatingDecoImage;


public class MainMenu extends MenuPage{

    public enum Type {
        MAINMENU,
        INGAME
    }
    
    public MainMenu(Skin skin, MenuManager menuManager, Type type)
    {
        super(skin,"logo" );
        addCenteredButton(menuManager.getWidth() - 80, 54, 100, 40, "logo", () -> System.exit(-1));
        
    }
    
    /*protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 300, 40, text, () -> menuManager.pushPage(page));
    }*/
    
}
