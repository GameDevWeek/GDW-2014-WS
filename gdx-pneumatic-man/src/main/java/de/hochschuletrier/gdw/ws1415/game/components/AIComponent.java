package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;

import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;

public class AIComponent extends Component implements Pool.Poolable
{
    public AIType type;
    public int AIstate = 0;
    public float AItimer = 0.2f;


    @Override
    public void reset() {
        type = AIType.DOG;
        AIstate = 0;
        AItimer = 0.2f;
    }
}
