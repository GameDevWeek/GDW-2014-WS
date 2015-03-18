package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

import de.hochschuletrier.gdw.ws1415.game.utils.EventBoxType;

public class EventBoxTypeComponent extends Component implements Pool.Poolable {
    EventBoxType type;

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        type = null;
    }
}
