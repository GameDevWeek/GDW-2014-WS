package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class DeathComponent extends Component implements Poolable
{
    // describes a simple Animation for Death. Rotates and Scales from current Position to Goal-values for the time of dyingDuration (in ms)
    public float dyingDuration = 3000;
    public float rotationGoal = 1420;
    public float scaleGoal = 0; // currentScale of Entity (100%) is start-value. scaleBehaviour is in %
   
    @Override
    public void reset()
    {
        dyingDuration = 0;
        rotationGoal = 0;
        scaleGoal = 0;
    }
    
    
    
}
