package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class LevelMenu extends MenuPage 
{
	
	public LevelMenu(Skin skin, MenuManager menuManager)
	{
		super(skin, "logo");
		
		addCenteredButton(menuManager.getWidth() - 150, 50, 150, 50, "RETURN", () -> menuManager.popPage());
	}
}
