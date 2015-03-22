package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ws1415.game.menu.MainMenu.Type;

public class WinScreen extends MenuPage
{
    
    private String[] names = { "Santo Pfingsten", "Jerome Jaehnig", "Tobias Meier", "Sandra Weyand", "David Neubauer",
        "Oliver Biwer", "David Liebemann", "Markus Schmieder", "Sebastian Krause", "Sinan Sagunc", "Tobias Thierbach",
        "Hermann Hockemeier", "Dennis Grass", "Tobias Rogalski", "Wladimir Kroeker", "Jeremy Merse", "Yasmin Juncker",
        "Julia Eiden", "Mira Lorenz", "Maximilian Furrer", "Niklas Zorell", "Michael Kramer",  "Reiko Schroth", "Annika Kremer",
        "Kevin Messmer", "Hannes Klein", "Felix Wishnewsky", "Noah Plingen", "Manuel Neugebauer", "Marc Amann", "Julia Wolf",
        "Michelle Kaufmann", "Saskia Hohmann", "Hans Boehme", "Lynn Friedrich", "Maxine Hammen", "Tobias Gepp", "Dominik Petersdorf"};
    private final BitmapFont font_32;
    private final BitmapFont font_75;
    private final BitmapFont font_18;
    
    
    public enum Type
    {
        MAINMENU,
        INGAME
    }
    
    public WinScreen(Skin skin, MenuManager menuManager, Type type)
    {
        super(skin, "winscreen" );
        
        font_32 = assetManager.getFont("orbitron_32");
        font_75 = assetManager.getFont("orbitron_75");
        font_18 = assetManager.getFont("orbitron_18");
        
        font_32.setColor(Color.valueOf("ffe601"));
        font_75.setColor(Color.valueOf("ffe601"));
        font_18.setColor(Color.valueOf("ffe601"));
        
        
        for(int i = 0; i < 5; i++)
        {
            System.out.println(names[(int)(Math.random()*100)%names.length]);
        }
         
        
        
//        for()
        
        
        //fuck off!
        
        
        
    }

}
