package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.Score;

public class ScoreMenu extends MenuPage
{

    private final DecoImage head = new DecoImage(assetManager.getTexture("score_button_active"));
    public Label[] highscoreLabels; 

    public ScoreMenu(Skin skin, MenuManager menuManager)
    {
        super(skin, "background_menu");
        addImage((int)(Main.WINDOW_WIDTH/2 - 550), 750, (int) head.getWidth(), (int) head.getHeight(), head);
        addCenteredImage(450, 750, 108, 108, new DecoImage(assetManager.getTexture("back_button")), () -> menuManager.popPage());
        highscoreLabels = new Label[10];
        createHighscoreList();
    }

    private Label createLabel(int x, int y)
    {
        Label label = new Label("", skin);
        label.setBounds(x, y, 60, 30);
        addActor(label);
        return label;
    }
    
    @Override
  public void setVisible(boolean visible) {
        if(highscoreLabels != null && highscoreLabels[0] != null){
            updateHighscoreList();
        }
        super.setVisible(visible);
  }

    public void createHighscoreList(){
        for(int i = 0; i <= (Score.highscores.length - 1); i++){
            highscoreLabels[i] = this.createLabel(Main.WINDOW_WIDTH/2, (Main.WINDOW_HEIGHT/2 + 100) - i*40);
        }
        updateHighscoreList();
    }
    
    public void updateHighscoreList(){
        for(int i = 0; i <= (Score.highscores.length - 1); i++){
            highscoreLabels[i].setText(Score.highscores[i]);
        }
    }
}
