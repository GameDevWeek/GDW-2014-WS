package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.DeathComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;

public class DeathAnimationSystem extends EntitySystem implements EntityListener
{
    private final ArrayList<Entity> entities = new ArrayList();

    public DeathAnimationSystem() {
        super(0);
    }

    public DeathAnimationSystem(int Priority) {
        super(Priority);
    }

    PooledEngine CurrentEngine;
    public void addedToEngine(PooledEngine engine) {
        CurrentEngine = engine;
        Family family = Family.all(DeathComponent.class).get();
        engine.addEntityListener(family, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        entities.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        entities.remove(entity);
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            DeathComponent death = entity.getComponent(DeathComponent.class);
            PositionComponent position = entity.getComponent(PositionComponent.class);

            if (death.duration > 0) 
            {
                
            }
            else
            {
                CurrentEngine.removeEntity(entity);
                // RESTART HANDLING
            }

        }
    }
}
