package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;

public class HealthSystem extends EntitySystem implements EntityListener {

    private final ArrayList<Entity> entities = new ArrayList();

    public HealthSystem() {
        super(0);
    }

    public HealthSystem(int Priority) {
        super(Priority);
    }

    Engine CurrentEngine;

    public void addedToEngine(Engine engine) {
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

            if (Health.Value <= 0) {
                CurrentEngine.removeEntity(entity);
            }

        }
    }
}
