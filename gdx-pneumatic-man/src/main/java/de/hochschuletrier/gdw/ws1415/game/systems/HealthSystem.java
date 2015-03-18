package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;

import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;

public class HealthSystem extends EntitySystem implements EntityListener {

    private final ArrayList<Entity> entities = new ArrayList();

    public HealthSystem() {
        super(0);
    }

    public HealthSystem(int Priority) {
        super(Priority);
    }

    PooledEngine CurrentEngine;

    public void addedToEngine(PooledEngine engine) {
        CurrentEngine = engine;
        Family family = Family.all(HealthComponent.class).get();
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
            HealthComponent Health = entity.getComponent(HealthComponent.class);
            Health.Value = Health.Value - Health.DecrementByValueNextFrame;
            Health.DecrementByValueNextFrame = 0;

            if ((entity.getComponent(PlayerComponent.class)!= null) && (Health.Value <= 0)) {
                entity.getComponent(HealthComponent.class).health = HealthComponent.HealthState.DYING;
                
                PositionComponent position = entity.getComponent(PositionComponent.class);
                EntityCreator.createAndAddDyingCharacter(entity); 
                entity.removeAll();
                CurrentEngine.removeEntity(entity);
            }

        }
    }
}
