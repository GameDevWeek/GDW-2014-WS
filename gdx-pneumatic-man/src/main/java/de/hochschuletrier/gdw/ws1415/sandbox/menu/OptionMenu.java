package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class OptionMenu extends MenuPage
{
	//private final TextButton mainMenuButton;
	
	public OptionMenu(Skin skin, MenuManager menuManager)
	{
		super(skin, "background_menu");
		
		addCenteredButton(menuManager.getWidth() - 150, 50, 150, 50, "RETURN", () -> menuManager.popPage());
	}
}
