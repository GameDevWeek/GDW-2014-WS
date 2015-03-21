package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InputComponent extends Component implements Pool.Poolable
{
    public boolean jump = false;
    
    public float direction = 0; // negativ heißt links, 0 heißt stehenbleiben, positiv heißt rechts
    
    @Override
    public void reset()
    {
        direction = 0;
        jump = false;
    }

}
