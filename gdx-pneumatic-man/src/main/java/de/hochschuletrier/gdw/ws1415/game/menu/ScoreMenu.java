package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Main;

public class ScoreMenu extends MenuPage
{   
    int[] highscore = new int[4]; //Hier sollen die Highscore gespeichert werden.
	int[] testScore = {7890, 3566, 1000, 245, 10, 1};
    
	private final DecoImage head = new DecoImage(assetManager.getTexture("score_button_active"));


	public ScoreMenu(Skin skin, MenuManager menuManager)
	{
		super(skin, "background_menu");
		createLabel(Main.WINDOW_WIDTH/2, Main.WINDOW_HEIGHT/2 + 300).setText("HIGHSCORE");
		
		int x = Main.WINDOW_WIDTH/2 - 50;
		int y = 700; 
		int y_step = 0;
		
		
		int x = Main.WINDOW_WIDTH/2 - 50;
		int y = 650; 
		int y_step = 0;
		
		createLabel(menuManager.getWidth()/2, menuManager.getHeight()/2).setText("HIGHSCORE");
		
		for (int i = 0; i < testScore.length; i++)
		{
			createLabel(x, y - y_step).setText("" + testScore[i]);
			y_step += 50;
		}
		
		
		addImage((int)(Main.WINDOW_WIDTH/2 - 550), 750, (int) head.getWidth(), (int) head.getHeight(), head);

		for (int i = 0; i < testScore.length; i++)
		{
			createLabel(x, y - y_step).setText("" + (i+1) + ". " + testScore[i]);
			y_step += 50;
		}
		
		addCenteredImage(450, 750, 108, 108, new DecoImage(assetManager.getTexture("back_button")), () -> menuManager.popPage());
	}
	
	private Label createLabel(int x, int y)
	{
	    Label label = new Label("", skin);
	    label.setBounds(x, y, 60, 30);
	    addActor(label);
	    return label;
	}
}
