package de.hochschuletrier.gdw.ws1415.game.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.Settings;

public class LevelMenu extends MenuPage 
{
	int[] test = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };

	private int level;
	private int page = 1;
	private int x = 520;
	private int x_step = 0;
	private int y = Main.WINDOW_HEIGHT / 2 - 100;
	private int y_up = y + 200;
	private int page_count = test.length / 4;
	private boolean extraPage = (test.length % 4 != 0) ? true : false;

	private final Label[] labelArray = new Label[4];
	private final DecoImage[] imageArray = new DecoImage[test.length];
	private DecoImage pointer;

	private final DecoImage head = new DecoImage(
			assetManager.getTexture("levels_button_active"));

	public LevelMenu(Skin skin, MenuManager menuManager) 
	{
		super(skin, "background_menu");

		addImage((int) (Main.WINDOW_WIDTH / 2 - 350), 750, (int) head.getWidth(), (int) head.getHeight(), head);

		if (extraPage) 
		{
			page_count++;
		}

		for (int j = 0; j < labelArray.length; j++) 
		{
			final int newLevel = j;

			labelArray[j] = createLabel(x + x_step, y_up);
			labelArray[j].setText("" + test[j]);

			imageArray[j] = new DecoImage(assetManager.getTexture("level_free"));
			addCenteredImage(x + x_step, y, 108, 108, imageArray[j], () -> levelChange(newLevel));

			x_step += 250;
		}

		addCenteredImage(450, 750, 108, 108, new DecoImage(assetManager.getTexture("back_button")), () -> menuManager.popPage());
		addCenteredImage(Main.WINDOW_WIDTH / 2 - 54, 200, 108, 108, new DecoImage(assetManager.getTexture("down_button")), () -> nextPage());
	}

	private void nextPage() 
	{
		x = 520;
		x_step = 0;

		if (page == page_count) 
		{
			System.out.println("SEITE ERREICHT");
			for (int i = 0; i < imageArray.length; i++) 
			{
				removeActor(imageArray[i]);
			}

			for (int i = 0; i < labelArray.length; i++) 
			{
				System.out.println("SEITE ERREICHT");
				int newLevel = i;
				labelArray[i].setText("" + (test[i]));
				imageArray[i] = new DecoImage(assetManager.getTexture("level_free"));
				addCenteredImage(x + x_step, y, 108, 108, imageArray[i], () -> levelChange(newLevel));
			}
			page = 0;
		}

		if (page < page_count) 
		{
			for (int j = 0; j < page * labelArray.length; j++) 
			{
				if (j < imageArray.length) 
				{
					removeActor(imageArray[j]);
				}
			}

			for (int j = 0; j < labelArray.length; j++) 
			{
				final int newLevel = j + 4 * page;

				if (newLevel < imageArray.length) 
				{
					labelArray[j].setText("" + (test[j] + 4 * page));
					imageArray[newLevel] = new DecoImage(assetManager.getTexture("level_free"));
					addCenteredImage(x + x_step, y, 108, 108, imageArray[newLevel], () -> levelChange(newLevel));
				} else 
				{
					labelArray[j].setText("");
				}
				x_step += 250;
			}
			page++;
		}
	}

	private Label createLabel(int x, int y) 
	{
		Label label = new Label("", skin, "highscore");
		label.setBounds(x, y, 60, 30);
		addActor(label);
		return label;
	}

	private void levelChange(int newLevel) 
	{
		pointer = new DecoImage(assetManager.getTexture("Ico_Level_Active"));

		pointer.setX(imageArray[newLevel].getX());
		pointer.setY(imageArray[newLevel].getY());

		level = newLevel;

		System.out.println("Level ausgewählt: " + (level + 1));
		Settings.CURRENTLY_SELECTED_LEVEL.set(level);
	}

	// Store udn Restore der Daten!!!
	private void storeSettings() 
	{
		Settings.CURRENTLY_SELECTED_LEVEL.set(level);
		Settings.flush();
	}

	private void restoreSettings() 
	{
		level = Settings.CURRENTLY_SELECTED_LEVEL.get();
	}

	@Override
	public void setVisible(boolean visible) 
	{
		if (head != null && isVisible() != visible) 
		{
			if (visible) 
			{
				restoreSettings();
			} 
			else 
			{
				storeSettings();
			}
		}
		super.setVisible(visible);
	}
}
