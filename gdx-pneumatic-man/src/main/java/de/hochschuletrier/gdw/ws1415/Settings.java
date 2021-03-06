package de.hochschuletrier.gdw.ws1415;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.settings.BooleanSetting;
import de.hochschuletrier.gdw.commons.gdx.settings.IntegerSetting;
import de.hochschuletrier.gdw.commons.gdx.settings.FloatSetting;
import de.hochschuletrier.gdw.commons.gdx.settings.StringSetting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import de.hochschuletrier.gdw.commons.gdx.settings.SettingsUtil;

public class Settings {

    private static final Preferences prefs = Gdx.app.getPreferences(Settings.class.getName() + ".xml");

    public static final BooleanSetting FULLSCREEN = new BooleanSetting(prefs, "fullscreen", false);
    public static final FloatSetting SOUND_VOLUME = new FloatSetting(prefs, "sound_volume", 1.0f);
    public static final FloatSetting MUSIC_VOLUME = new FloatSetting(prefs, "music_volume", 1.0f);
    public static final IntegerSetting CURRENTLY_SELECTED_LEVEL = new IntegerSetting(prefs, "selected_level", 0);
    public static final BooleanSetting GAMEPAD_ENABLED = new BooleanSetting(prefs, "gamepad_enabled", false);
    public static final BooleanSetting LIGHTS = new BooleanSetting(prefs, "lights", true);
    //public static final IntegerSetting CURRENTLY_SELECTED_LEVEL = new IntegerSetting(prefs, "selected_level", 1);
    public static final StringSetting HIGHSCORE = new StringSetting(prefs, "highscore", "blank");

    public static void flush() {
        prefs.flush();
    }

    public static void reset() {
        SettingsUtil.reset(Settings.class);
    }
}
