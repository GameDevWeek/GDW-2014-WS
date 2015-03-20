package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;

import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;

public class AIComponent extends Component implements Pool.Poolable
{
    public AIType type;
    public int AIstate = 0;


    @Override
    public void reset() {
        type = AIType.DOG;
    }
}
