package de.hochschuletrier.gdw.ws1415.game.menu;


import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Main;
//import de.hochschuletrier.gdw.ws1415.game.Game;

public class LevelMenu extends MenuPage 
{
	boolean levelSelected = false;
    int[] test = {1, 2, 3, 4, 5};
    
	DecoImage[] imageArray = new DecoImage[test.length];
	ImageButton[] buttonArray = new ImageButton[test.length];
	
	DecoImage pointer = new DecoImage(assetManager.getTexture("back_button"));
	
	private final DecoImage head = new DecoImage(assetManager.getTexture("levels_button_active"));


	public LevelMenu(Skin skin, MenuManager menuManager)
	{
		super(skin, "background_menu");
				
		int x = 600;
		int x_step = 0;
		int y = Main.WINDOW_HEIGHT/2 - 200;
		int y_up = y + 100;
		int y_down = y - 20;
				
		//ImageButton tryOut = addImageButton(x + x_step, y + 200, 108, 108, this::levelTest);
		//tryOut.background("back_button");
		//tryOut.setBackground("data/images/back_button.png");
		//tryOut.setBackground("back_button");
		
		addImage((int)(Main.WINDOW_WIDTH/2 - 350), 750, (int) head.getWidth(), (int) head.getHeight(), head);
		
		addCenteredImage(x + x_step, y + 120, 108, 108, pointer, ()-> System.exit(0));
		
		for(int i = 0; i < test.length; i++)
		{
			if (i % 2 == 0)
				createLabel(x + x_step, y_up).setText("" + test[i]);
			else
				createLabel(x + x_step, y_down).setText("" + test[i]);
			
			//buttonArray[i] = addImageButton(x + x_step, y, 108, 108, this::levelTest);
			//buttonGroup.add(buttonArray[i]);
			imageArray[i] = new DecoImage(assetManager.getTexture("back_button"));
			final int d = i;
			addCenteredImage(x + x_step, y, 108, 108, imageArray[i], () -> levelTest(d));
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
	
	private void levelTest(int s)
	{
		//Game.loadLevel();
		levelSelected = true;
		//Game.loadSelectedLevel = true;
		
		pointer.setX(imageArray[s].getX());
		pointer.setY(imageArray[s].getY()+20);

		System.out.println("Level ausgewählt: " + (s+1));
	}
}
