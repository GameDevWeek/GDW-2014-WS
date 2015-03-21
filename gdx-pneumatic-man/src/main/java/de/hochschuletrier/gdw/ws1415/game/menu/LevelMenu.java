package de.hochschuletrier.gdw.ws1415.game.menu;


import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.Settings;

public class LevelMenu extends MenuPage 
{
    int[] test = {1, 2, 3, 4};
    
	private final DecoImage[] imageArray = new DecoImage[test.length];
	private final DecoImage pointer;
		
	private final DecoImage head = new DecoImage(assetManager.getTexture("levels_button_active"));


	public LevelMenu(Skin skin, MenuManager menuManager)
	{
		super(skin, "background_menu");
				
		int x = 490;
		int x_step = 0;
		int y = Main.WINDOW_HEIGHT/2 - 100;
		int y_up = y + 200;
		
		addImage((int)(Main.WINDOW_WIDTH/2 - 350), 750, (int) head.getWidth(), (int) head.getHeight(), head);
		
		for(int i = 0; i < test.length; i++)
		{
			createLabel(x + x_step, y_up).setText("" + test[i]);
			imageArray[i] = new DecoImage(assetManager.getTexture("level_free"));
			final int newLevel = i;
			
			addCenteredImage(x + x_step, y, 108, 108, imageArray[i], () -> levelChange(newLevel));
			x_step += 250;
		}
		
		pointer = new DecoImage(assetManager.getTexture("level_locked"));
		addImage(x, y, (int)imageArray[0].getWidth(), (int)imageArray[0].getHeight(), pointer);

		addCenteredImage(450, 750, 108, 108, new DecoImage(assetManager.getTexture("back_button")), () -> menuManager.popPage());
	}
	
	private Label createLabel(int x, int y)
	{
	    Label label = new Label("", skin);
	    label.setBounds(x, y, 60, 30);
	    addActor(label);
	    return label;
	}
	
	private void levelChange(int newLevel)
	{		
		pointer.setX(imageArray[newLevel].getX());
		pointer.setY(imageArray[newLevel].getY());

		//System.out.println("Level ausgewählt: " + (newLevel+1));
		Settings.CURRENTLY_SELECTED_LEVEL.set(newLevel);
	}
}
