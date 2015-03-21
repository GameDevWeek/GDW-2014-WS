package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.Score;

public class ScoreMenu extends MenuPage
{

    private final DecoImage head = new DecoImage(assetManager.getTexture("score_head"));
    private final DecoImage overlay = new DecoImage(assetManager.getTexture("score_overlay"));
    public Label[] highscoreLabels; 
    private int x_step = 0;
	private int step = 0;
	
    public ScoreMenu(Skin skin, MenuManager menuManager)
    {
        super(skin, "background_menu");
        addImage((int)(Main.WINDOW_WIDTH/4 + 120), 750, (int) head.getWidth(), (int) head.getHeight(), head);
        addImage(Main.WINDOW_WIDTH/10 - 200, Main.WINDOW_HEIGHT/2-300, (int) overlay.getWidth(), (int) overlay.getHeight(), overlay);
        addCenteredImage(450, 750, 108, 108, new DecoImage(assetManager.getTexture("back_button")), () -> menuManager.popPage());
        highscoreLabels = new Label[10];
        createHighscoreList();
    }

    private Label createLabel(int x, int y)
    {
        Label label = new Label("", skin, "highscore");
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
        	int y_step = 40;
        	if(i == 3)
        	{
        		x_step += 600;
        		y_step = 0*40;
        	}
        	if (i >= 3)
        	{
        		this.createLabel(Main.WINDOW_WIDTH/4 + x_step-50, (Main.WINDOW_HEIGHT/2 + 80) - step*y_step - 47).setText("" + (i+1) + ". ");
        		highscoreLabels[i] = this.createLabel(Main.WINDOW_WIDTH/4 + x_step, (Main.WINDOW_HEIGHT/2 + 80) - step*y_step - 47);
        		step++;
        	}
        	else
        	{
        		this.createLabel(Main.WINDOW_WIDTH/4 + x_step-50, (Main.WINDOW_HEIGHT/2) - i*y_step-47).setText("" + (i+1) + ". ");
        		highscoreLabels[i] = this.createLabel(Main.WINDOW_WIDTH/4 + x_step, (Main.WINDOW_HEIGHT/2) - i*y_step-47);
        	}
        }
        updateHighscoreList();
    }
    
    public void updateHighscoreList(){
        for(int i = 0; i <= (Score.highscores.length - 1); i++){
            highscoreLabels[i].setText(Score.highscores[i]);
        }
    }
}
