package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1415.states.GameplayState;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;

public class MainMenu extends MenuPage{
    
    DecoImage start = new DecoImage(assetManager.getTexture("start_button"));
    DecoImage optionen = new DecoImage(assetManager.getTexture("optionen_button"));
    DecoImage credits = new DecoImage(assetManager.getTexture("credits_button"));
    DecoImage ende = new DecoImage(assetManager.getTexture("back_button"));
    DecoImage highscore = new DecoImage(assetManager.getTexture("score_button"));
    DecoImage levels = new DecoImage(assetManager.getTexture("levels_button"));
    
    public enum Type {
        MAINMENU,
        INGAME
    }
    
    public MainMenu(Skin skin, MenuManager menuManager, Type type)
    {
        super(skin,"background_menu" );
        int x = 960;
        int i = 0;
        int y = 670;
        int yStep = 113;
        
        addPageEntryStart(menuManager,x,y-yStep*(i++), start);
        addPageEntry(menuManager, x, y-yStep*(i++), levels, new LevelMenu(skin, menuManager));
        addPageEntry(menuManager,x, y-yStep*(i++), optionen, new OptionMenu(skin, menuManager));
        addPageEntry(menuManager, x, y-yStep*(i++), highscore, new ScoreMenu(skin, menuManager));
        addPageEntry(menuManager, x, y-yStep*(i++), credits, new CreditsMenu(skin, menuManager));
        addCenteredImage(450, 750, 108, 108, ende, () -> System.exit(0));
    }

    
    protected final void addPageEntry(MenuManager menuManager, int x, int y, DecoImage image, MenuPage page) {
        menuManager.addLayer(page);
        
        addCenteredImage((int)(x-(image.getWidth()/2)), y, (int)image.getWidth(), (int)image.getHeight(), image, () -> menuManager.pushPage(page));
        
    }
    
    protected final void addPageEntryStart(MenuManager menuManager, int x, int y, DecoImage image) {        
        addCenteredImage((int)(x-(image.getWidth()/2)), y, (int)image.getWidth(), (int)image.getHeight(), image, () ->  main.changeState(new GameplayState(assetManager),new SplitHorizontalTransition(500), null));
        
    }  
}
