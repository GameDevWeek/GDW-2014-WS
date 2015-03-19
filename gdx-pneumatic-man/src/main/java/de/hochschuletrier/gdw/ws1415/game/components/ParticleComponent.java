package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

/**
 * Create a new Entity for a Particle-Effect, 
 * don't attach this Component to a Entity with a Texture- or Animation-Component. 
 * Add a LayerComponent with the highest Level to this Entity.
 * @author Dennis
 *
 */
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
