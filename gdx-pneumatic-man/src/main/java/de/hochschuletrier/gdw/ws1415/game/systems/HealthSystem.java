package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.DestructableBlockComponent;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
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
        entities.add(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        entities.remove(entity);
    }

    @Override
    public void update(float deltaTime) {
//        for (Entity entity : entities) {
//            HealthComponent Health = entity.getComponent(HealthComponent.class);
//            Health.Value = Health.Value - Health.DecrementByValueNextFrame;
//            Health.DecrementByValueNextFrame = 0;
//
//            if((Health.Value <= 0))
//            {
//                if ((entity.getComponent(PlayerComponent.class)!= null)) {
//                    entity.getComponent(HealthComponent.class).health = HealthComponent.HealthState.DYING;
//                    
//                    PositionComponent position = entity.getComponent(PositionComponent.class);
//                    EntityCreator.modifyPlayerToDying(entity);
//                }
//                else
//                {
//                    logger.info(entity.getId() + " removed");
//                    CurrentEngine.removeEntity(entity);
//                }
//            }
//
//        }
    	
    	for (Entity entity : entities) {
            HealthComponent Health = ComponentMappers.health.get(entity);
            Health.Value -= Health.DecrementByValueNextFrame;
            Health.DecrementByValueNextFrame = 0;

            if((Health.Value <= 0))
            {
                if (ComponentMappers.player.has(entity)) {
                    Health.health = HealthComponent.HealthState.DYING;
                    //PositionComponent position = entity.getComponent(PositionComponent.class);
                    EntityCreator.modifyPlayerToDying(entity);
                }else if(ComponentMappers.block.has(entity)){
                    PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
                    DestructableBlockComponent block = ComponentMappers.block.get(entity);
                    Vector2 p1 = physix.getBody().getPosition();
                    Vector2 p2 = new Vector2(p1).add(Direction.DOWN.toVector2().scl(1.5f)); // FIXME MAGIC NUMBER

                    EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
                        PhysixBodyComponent bodyComponent = fixture.getUserData() instanceof PhysixBodyComponent ?
                                (PhysixBodyComponent) fixture.getUserData() : null;
                        if (bodyComponent != null && ComponentMappers.spikes.has(bodyComponent.getEntity())) {
                            PhysixModifierComponent modifierComponent = new PhysixModifierComponent();
                            modifierComponent.schedule(new Runnable() {
                                                           @Override
                                                           public void run() {
                                                               bodyComponent.getBody().setGravityScale(1);
                                                               bodyComponent.getBody().setAwake(true);
                                                               bodyComponent.getBody().setActive(true);
                                                               bodyComponent.getBody().getFixtureList().forEach(f->f.setSensor(false));
                                                           }
                                                       });
                            entity.add(modifierComponent);
                        }
                        return 0;
                    }, p1, p2);

                    if(block.deathTimer <= 0)
                        CurrentEngine.removeEntity(entity);
                    else
                        block.deathTimer -= deltaTime;
                }
                else
                {
                    logger.info(entity.getId() + " removed");
                    CurrentEngine.removeEntity(entity);
                }
            }

        }
    }
}
