package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent.HealthState;
import de.hochschuletrier.gdw.ws1415.game.components.lights.PointLightComponent;
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

    ArrayList<Entity> PostUpdateRemovals = new ArrayList<Entity>();
    @Override
    public void update(float deltaTime) {
        for(Entity entity : entities) {
            HealthComponent Health = ComponentMappers.health.get(entity);
            //Info log for damages
            if(Health.DecrementByValueNextFrame>0)
            {
                /*
                 * spielt sound zum  blockbeschödigen
                 */
                if(ComponentMappers.block.has(entity)){
                 // ***** Sound *****
                    Random rm=new Random();
                    int i=rm.nextInt(5)+1;//1-5dd
                    //logger.info("Debris "+i);
                    SoundEmitter.playGlobal(EntityCreator.assetManager.getSound("cracks"+i), false);
                }
                //logger.info("Damaging "+entity.getId()+" with " + Health.DecrementByValueNextFrame + " damage. New Health: "+ (Health.Value - Health.DecrementByValueNextFrame));
            }

            
            Health.Value -= Health.DecrementByValueNextFrame;
            Health.DecrementByValueNextFrame = 0;

            if((Health.Value <= 0))
            {

                //******states****
                if(ComponentMappers.deathTimer.has(entity))
                {
                    DeathTimerComponent deathTimer = ComponentMappers.deathTimer.get(entity);
                    if(deathTimer.deathTimer <= 0) {
                        if(ComponentMappers.bomb.has(entity)){
//                            System.out.println("bombTicks");
                            PositionComponent pos = ComponentMappers.position.get(entity);
                            SoundEmitter.playGlobal(EntityCreator.assetManager.getSound("bombTicks"), false, pos.x, pos.y, 0); 
                        }
                        Health.health = HealthComponent.HealthState.DEAD;
                    }else {
                        Health.health = HealthComponent.HealthState.DYING;
                        deathTimer.deathTimer -= deltaTime;
                        if(ComponentMappers.bomb.has(entity))
                        {
                            entity.getComponent(AnimationComponent.class).IsActive = true;
                            entity.getComponent(PointLightComponent.class).pointLight.setActive(true);
                        }
                    }
                }
                else
                {
                    Health.health = HealthState.DEAD;
                }
   
                if (ComponentMappers.AI.has(entity)){
                    Health.health = HealthComponent.HealthState.DYING;
//                    System.out.println("guardDie");
                    SoundEmitter.playGlobal(EntityCreator.assetManager.getSound("guardDie"),false);
//                    SoundEmitter.playGlobal(EntityCreator.assetManager.getSound("alienDie"),false);
                    EntityCreator.modifyEnemyToDying(entity);
                } else
                if (ComponentMappers.player.has(entity)) {
                    Health.health = HealthComponent.HealthState.DYING;
                    
                    EntityCreator.modifyPlayerToDying(entity);
                }
                else if(ComponentMappers.bomb.has(entity) && Health.health == HealthState.DEAD)
                {
                    PositionComponent pos = ComponentMappers.position.get(entity);
                    SoundEmitter.playGlobal(EntityCreator.assetManager.getSound("bomb"),false, pos.x, pos.y, 0);
//                    System.out.println("Explosion");
                    
                    EntityCreator.modifyBombToExplode(entity);
                }else if(ComponentMappers.block.has(entity)){
                    PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);

                    Vector2 p1 = physix.getBody().getPosition();
                    Vector2 p2 = new Vector2(p1).add(Direction.DOWN.toVector2().scl(1.5f)); // FIXME MAGIC NUMBER
                    


                    if(Health.health == HealthComponent.HealthState.DEAD) {
//                        System.out.println("Healthcomponent dead");
                        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
                            Object bodyUserData = fixture.getBody().getUserData();
                            PhysixBodyComponent bodyComponent = bodyUserData instanceof PhysixBodyComponent ?
                                    (PhysixBodyComponent) bodyUserData : null;
                            if (bodyComponent != null && ComponentMappers.spikes.has(bodyComponent.getEntity())) {

                                bodyComponent.setGravityScale(1.8f);
                                bodyComponent.setAwake(true);
                                bodyComponent.setActive(true);
                                bodyComponent.getBody().getFixtureList().forEach(f -> f.setSensor(false));


                                try {
                                    AnimationComponent animationComponent = ComponentMappers.animation.get(bodyComponent.getEntity());
                                    animationComponent.stateTime = animationComponent.animation.animationDuration / 3;
                                    animationComponent.permanent_stateTime = animationComponent.animation.animationDuration / 3;
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                            return 0;
                        }, p1, p2);
                        PostUpdateRemovals.add(entity);
                    }
                }
                else if ( ComponentMappers.AI.has(entity) && Health.health == HealthState.DEAD )
                {

                    entity.getComponent(AnimationComponent.class).IsActive = true;
                }else
                {
                   if(Health.health != HealthState.DYING)
                    {
                        logger.info(entity.getId() + " removed");
                        PostUpdateRemovals.add(entity);
                    }
                }
            }

        }
        for(Entity entity : PostUpdateRemovals)
        {
            CurrentEngine.removeEntity(entity);
        }
        PostUpdateRemovals.clear();
    }
        

}
