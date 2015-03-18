package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

public class ParticleComponent extends Component implements Pool.Poolable
{
    
    ParticleEffect pe;

    @Override
    public void reset()
    {
        pe = null;
    }

}
