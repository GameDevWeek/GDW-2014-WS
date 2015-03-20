package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
//import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Main;
//import de.hochschuletrier.gdw.ws1415.game.Game;

public class LevelMenu extends MenuPage 
{
	boolean levelSelected = false;
    private final ButtonGroup<TextButton> buttonGroup = new ButtonGroup<TextButton>();

	public LevelMenu(Skin skin, MenuManager menuManager)
	{
		super(skin, "background_menu");
				
		int x = 600;
		int x_step = 0;
		int y = Main.WINDOW_HEIGHT/2;
		int y_up = y + 100;
		int y_down = y - 20;
		
		int[] test = {1, 2, 3, 4, 5};
		DecoImage[] imageArray = new DecoImage[test.length];
		
		createLabel(Main.WINDOW_WIDTH/2, Main.WINDOW_HEIGHT/2 + 300).setText("LEVEL");
		
		for(int i = 0; i < test.length; i++)
		{
			if (i % 2 == 0)
				createLabel(x + x_step, y_up).setText("" + test[i]);
			else
				createLabel(x + x_step, y_down).setText("" + test[i]);
			
			imageArray[i] = new DecoImage(assetManager.getTexture("back_button"));
			//add
			addCenteredImage(x + x_step, y, 108, 108, imageArray[i], () -> levelTest());
			x_step += 150;
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
	
	private void levelTest()
	{
		//Game.loadLevel();
		levelSelected = true;
		//Game.loadSelectedLevel = true;
		System.out.println("LEVEL AUSGEWÄHLT");
	}
}
