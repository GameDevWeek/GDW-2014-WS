package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;

public class ScoreMenu extends MenuPage
{
	public ScoreMenu(Skin skin, MenuManager menuManager)
	{
		super(skin, "background_menu");
		createLabel(menuManager.getWidth()/2, menuManager.getHeight()/2).setText("HIGHSCORE");
		
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
