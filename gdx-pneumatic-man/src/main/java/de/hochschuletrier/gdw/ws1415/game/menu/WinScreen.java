package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.ws1415.game.Game;
import de.hochschuletrier.gdw.ws1415.game.Score;
import de.hochschuletrier.gdw.ws1415.game.components.GoalComponent;
import de.hochschuletrier.gdw.ws1415.game.menu.MainMenu.Type;
import de.hochschuletrier.gdw.ws1415.states.GameplayState;

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
        
//        addCenteredImage(1400, 180, 108, 108, new DecoImage(assetManager.getTexture("butt_next_level")), () -> nextLevel());
        
        font_32 = assetManager.getFont("orbitron_32");
        font_75 = assetManager.getFont("orbitron_75");
        font_18 = assetManager.getFont("orbitron_18");
        font_18.setColor(Color.valueOf("ffe601"));
        
//        font_18.draw(DrawUtil.batch , "TEST", 1400, 108);
//        font_18.dispose();
//        font_18.draw(DrawUtil.batch, "" + Score.highscores + "", 1200, 300);
        
        
        
        int nameRandomStart = (int)((Math.random()*100)%(names.length-Score.total_miners+1));
        int minersSaved = Score.player_saved_miners;
        
        int minerLabelHoehe = (Main.WINDOW_HEIGHT/2 + 80);
        for(int i = 0; i <= minersSaved; i++)
        {
            this.createLabel(Main.WINDOW_WIDTH/4 -50, minerLabelHoehe).setText(names[nameRandomStart++]);
            minerLabelHoehe -= 35;
        }
        
        minerLabelHoehe = (Main.WINDOW_HEIGHT/2 + 80) - 300;
        for(int i = 0; i <= minersSaved; i++)
        {
            this.createLabel(Main.WINDOW_WIDTH/4 -50, minerLabelHoehe).setText(names[nameRandomStart++]);
            minerLabelHoehe -= 35;
        }
        
        this.createLabel(Main.WINDOW_WIDTH/4 +500, Main.WINDOW_HEIGHT/2 + 50).setText("" + Score.score);
        
        
        this.createLabel(Main.WINDOW_WIDTH/4 +540, Main.WINDOW_HEIGHT/2 - 100).setText("" + Score.game_time);
        this.createLabel(Main.WINDOW_WIDTH/4 +540, Main.WINDOW_HEIGHT/2 - 190).setText("" + Score.player_destroyed_blocks);
        
        
        
        addCenteredImage(1400, 180, 108, 108, new DecoImage(assetManager.getTexture("butt_next_level")), () -> nextLevel());
       
    }
    
    private Label createLabel(int x, int y)
    {
        Label label = new Label("", skin, "highscore");
        label.setBounds(x, y, 60, 30);
        addActor(label);
        return label;
    }
    
    private void nextLevel()
    {
        Settings.CURRENTLY_SELECTED_LEVEL.set(Math.abs((Settings.CURRENTLY_SELECTED_LEVEL.get() + 1 ) % 8 ));
        main.changeState(new GameplayState(assetManager),new SplitHorizontalTransition(500), null);
    }

}
