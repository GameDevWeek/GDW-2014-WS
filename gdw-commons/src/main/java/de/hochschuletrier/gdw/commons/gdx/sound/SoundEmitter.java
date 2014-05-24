package de.hochschuletrier.gdw.commons.gdx.sound;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.hochschuletrier.gdw.commons.utils.Recycler;
import java.util.ArrayList;
import java.util.Iterator;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alListener3f;

/**
 * Like a speaker in the world, can be moved.
 * OpenAL backend required.
 *
 * @author Santo Pfingsten
 */
public class SoundEmitter implements Disposable {

    public enum Mode {

        STEREO,
        MONO;
    }

    protected static final Recycler<SoundInstance> recycler = new Recycler<SoundInstance>(SoundInstance.class);
    protected final ArrayList<SoundInstance> instances = new ArrayList<SoundInstance>();
    protected static Mode mode = Mode.STEREO;
    private static final Vector3 listenerPosition = new Vector3();

    public static void setListenerPosition(float x, float y, float z, Mode mode) {
        SoundEmitter.mode = mode;
        if (mode == Mode.STEREO) {
            alListener3f(AL_POSITION, x, y, z);
        } else {
            listenerPosition.set(x, y, z);
            alListener3f(AL_POSITION, 0, 0, 0);
        }
    }

    public void update() {
        Iterator<SoundInstance> it = instances.iterator();
        while (it.hasNext()) {
            SoundInstance si = it.next();
            if (si.isStopped()) {
                it.remove();
                recycler.recycle(si);
            }
        }
    }

    public void setPosition(float x, float y, float z) {
        if (mode == Mode.STEREO) {
            for (SoundInstance si : instances) {
                si.setPosition(x, y, z);
            }
        } else {
            float distance = listenerPosition.dst(x, y, z);

            for (SoundInstance si : instances) {
                si.setPosition(0, 0, distance);
            }
        }
    }

    public SoundInstance play(Sound sound, boolean loop) {
        SoundInstance si = recycler.get();
        si.init(sound, loop);
        instances.add(si);
        return si;
    }

    @Override
    public void dispose() {
        for (SoundInstance si : instances) {
            si.stop();
            recycler.recycle(si);
        }
        instances.clear();
    }
}