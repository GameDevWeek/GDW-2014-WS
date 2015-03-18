package de.hochschuletrier.gdw.ws1415.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.ParticleComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedFamilyRenderSystem;

public class ParticleRenderer extends SortedFamilyRenderSystem.Renderer
{
    @SuppressWarnings("unchecked")
    public ParticleRenderer()
    {
        super(Family.all(PositionComponent.class, ParticleComponent.class).get());
    }

    @Override
    public void render(Entity entity, float deltaTime)
    {
        PositionComponent position = ComponentMappers.position.get(entity);
        ParticleComponent particle = ComponentMappers.particle.get(entity);
        
        if(particle.loop){
           for(ParticleEmitter pe: particle.particleEffect.getEmitters()){
               pe.durationTimer=0;
           }
        }
        particle.particleEffect.setPosition(position.x, position.y);
        particle.particleEffect.update(deltaTime);
        particle.particleEffect.draw(DrawUtil.batch);
    }

}
