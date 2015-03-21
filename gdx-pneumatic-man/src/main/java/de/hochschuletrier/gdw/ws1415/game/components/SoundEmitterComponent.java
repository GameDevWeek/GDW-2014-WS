package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;

public class SoundEmitterComponent extends Component implements Pool.Poolable {

    public SoundEmitter emitter = new SoundEmitter();

    @Override
    public void reset() {
        emitter.dispose();
    }
}
