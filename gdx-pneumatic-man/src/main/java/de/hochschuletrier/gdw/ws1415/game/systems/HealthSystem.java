package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;

public class HealthSystem extends EntitySystem implements EntityListener {

    private static final Logger logger = LoggerFactory.getLogger(HealthSystem.class);
    private final ArrayList<Entity> entities = new ArrayList();

    public HealthSystem() {
        super(0);
    }

    public HealthSystem(int Priority) {
        super(Priority);
    }

    Engine CurrentEngine;

    @Override
	public void addedToEngine(Engine engine) {
        CurrentEngine = engine;
        Family family = Family.all(HealthComponent.class).get();
        engine.addEntityListener(family, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        System.out.println("Entity added "+entity.getId());
        entities.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        entities.remove(entity);
    }

    ArrayList<Entity> AddPostUpdate = new ArrayList<>();
    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            HealthComponent Health = entity.getComponent(HealthComponent.class);
            Health.Value = Health.Value - Health.DecrementByValueNextFrame;
            Health.DecrementByValueNextFrame = 0;

            if((Health.Value <= 0))
            {
                if ((entity.getComponent(PlayerComponent.class)!= null)) {
                    entity.getComponent(HealthComponent.class).health = HealthComponent.HealthState.DYING;
                    
                    PositionComponent position = entity.getComponent(PositionComponent.class);
                    EntityCreator.modifyPlayerToDying(entity);
                    //AddPostUpdate.add(EntityCreator.modifyPlayerToDying(entity));
                }
                else
                {
                    CurrentEngine.removeEntity(entity);
                }
            }

        }

        for(Entity e : AddPostUpdate)
        {
             CurrentEngine.addEntity(e);
        }
        AddPostUpdate.clear();
    }
}
