package de.hochschuletrier.gdw.ws1415;

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
    public static final BooleanSetting SOUND_MUTE = new BooleanSetting(prefs, "sound_mute", false);
    public static final FloatSetting MUSIC_VOLUME = new FloatSetting(prefs, "music_volume", 1.0f);
    public static final BooleanSetting MUSIC_MUTE = new BooleanSetting(prefs, "music_mute", false);
    public static int CURRENTLY_SELECTED_LEVEL = 0;
    public static final BooleanSetting GAMEPAD_ENABLED = new BooleanSetting(prefs, "gamepad_enabled", false);

    public static void flush() {
        prefs.flush();
    }

    public static void reset() {
        SettingsUtil.reset(Settings.class);
    }
}
