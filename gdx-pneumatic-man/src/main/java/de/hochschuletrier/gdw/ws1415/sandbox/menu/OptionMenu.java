package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;

public class OptionMenu extends MenuPage
{	
	//Musik/Sound laut/leise
	//Musik/Sound mute
	//Gamepad/Keyboard
	
	private final Slider soundSlider;
	private final Slider musicSlider;
	
	public OptionMenu(Skin skin, MenuManager menuManager)
	{ 
		super(skin, "logo");
		
		soundSlider = null; //createSlider(100, 300, this);
		musicSlider = null; //createSlider(100, 200, this);
		
		createLabel(100, 400).setText("Sound: ");
		
		addCenteredButton(menuManager.getWidth() - 150, 50, 150, 50, "RETURN", () -> menuManager.popPage());
	}
	
	private Label createLabel(int x, int y)
	{
	    Label label = new Label("", skin);
	    label.setBounds(x, y, 60, 30);
	    addActor(label);
	    return label;
	}
	
	private Slider createSlider(int x, int y, Runnable runnable) {
        Slider slider = new Slider(0, 1, 0.01f, false, skin);
        slider.setBounds(x, y, 200, 30);
        addActor(slider);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                runnable.run();
            }
        });
        return slider;
    }
}
