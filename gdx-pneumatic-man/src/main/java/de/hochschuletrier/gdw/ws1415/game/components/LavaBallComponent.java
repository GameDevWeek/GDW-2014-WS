package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class LavaBallComponent extends Component implements Pool.Poolable{
    
    public float travelLength, startPositionX, startPositionY;

    @Override
    public void reset() {
        travelLength = 0;
        startPositionX = 0;
        startPositionY = 0;
    }

}
