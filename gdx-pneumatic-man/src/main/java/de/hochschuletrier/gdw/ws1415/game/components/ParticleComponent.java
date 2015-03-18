package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

public class ParticleComponent extends Component implements Pool.Poolable
{ 
    public ParticleEffect particleEffect;
    public boolean loop = false; 
    
    @Override
    public void reset()
    {
        this.particleEffect = null;
        this.loop = false;
    }
}
