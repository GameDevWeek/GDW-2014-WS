package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;
//import de.hochschuletrier.gdw.ws1415.game.components.BouncingComponent;
import de.hochschuletrier.gdw.ws1415.game.components.JumpComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlatformComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SoundEmitterComponent;

public class MovementSystem extends IteratingSystem {

    private static final float EPSILON = 1f / 64f;

    public MovementSystem() {
        this(0);
    }

    public MovementSystem(int priority) {
        super(Family
                .all(PhysixBodyComponent.class)
                .one(MovementComponent.class, JumpComponent.class,
                        InputComponent.class, PlayerComponent.class).get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        // BouncingComponent bouncing = ComponentMappers.bouncing.get(entity);
        JumpComponent jump = ComponentMappers.jump.get(entity);
        PlayerComponent playerComp = ComponentMappers.player.get(entity);
        
        if (movement != null) {
            
            if (input != null) {
                movement.velocity.set(movement.speed * input.direction,
                        movement.velocity.y);
            }
            Vector2 lowestPlatformVelocity = new Vector2(0,0);
            if(playerComp != null) {
                
                for (Entity platform : playerComp.platformContactEntities) {
                    Vector2 vel = platform.getComponent(PlatformComponent.class).velocity;
                    if(vel.len2() > lowestPlatformVelocity.len2())
                        lowestPlatformVelocity = vel;
                }
                
            }
            physix.setLinearVelocity(movement.velocity.x  + lowestPlatformVelocity.x,
                    physix.getLinearVelocity().y
                            + (movement.velocity.y ) );
            
            
        }
        /*
         * deprecated if (bouncing != null) { // if entity is on the ground and
         * the timeToNextBounce has surpassed // the current restingTime if
         * (physix.getLinearVelocity().y < EPSILON &&
         * physix.getLinearVelocity().y > -EPSILON && bouncing.timeToNextBounce
         * > bouncing.restingTime) { physix.applyImpulse(0,
         * bouncing.bouncingImpulse); } // if entity is on the ground, add
         * deltaTime to timeToNextBounce else if (physix.getLinearVelocity().y <
         * EPSILON && physix.getLinearVelocity().y > -EPSILON) {
         * bouncing.timeToNextBounce += deltaTime; } // if entity is in the air
         * else { bouncing.timeToNextBounce = 0; } }
         */
        if (jump != null) {
            // if jump was called
            if (input != null) {

                jump.justJumped = false;
                if (jump.groundContacts > 0) {
                    if (input.jump) {
                        SoundEmitterComponent se = ComponentMappers.soundEmitter.get(entity);
                        if(se != null){
                            int ji = (int) (Math.random() * 10 % 6) +1;
                            se.emitter.play(EntityCreator.assetManager.getSound("jump" + ji), false);
                        }
                        jump.justJumped = true;
                        physix.setLinearVelocityY(-jump.jumpSpeed);
                    }
                }

                // Vector2 p1 = physix.getBody().getPosition();
                //
                // Vector2 p2 = new Vector2(p1).add(Direction.DOWN.toVector2()
                // .scl(1.45f));
                //
                // jump.jumpTimer += deltaTime;
                // if (jump.jumpTimer > 0.1f) {
                // EntityCreator.physixSystem
                // .getWorld()
                // .rayCast(
                // (fixture, point, normal, fraction) -> {
                // PhysixBodyComponent bodyComponent = fixture.getUserData()
                // instanceof PhysixBodyComponent ? (PhysixBodyComponent)
                // fixture.getUserData()
                // : null;
                // if (fixture.getBody() == physix.getBody())
                // return 1;
                // if (bodyComponent != null) {
                // jump.inAir = false;
                // if (input.jump) {
                // if (jump.doJump) {
                // physix.applyImpulse(0,
                // jump.jumpImpulse);
                // jump.inAir = true;
                // jump.doJump = false;
                // jump.jumpTimer = 0;
                //
                // if (ComponentMappers.block.has(bodyComponent.getEntity())
                // && ComponentMappers.health.has(bodyComponent.getEntity())) {
                // HealthComponent healthComponent =
                // ComponentMappers.health.get(bodyComponent.getEntity());
                // healthComponent.DecrementByValueNextFrame += 2;
                // return 0;
                // }
                // } else {
                // physix.setLinearVelocityY(0);
                // jump.doJump = true;
                // }
                // }else{
                // jump.jumpTimer += deltaTime;
                // }
                //
                // }
                // return 0;
                // }, p1, p2);
                // }

                /*
                 * jump.doJump = true; // if entity is on the ground and the
                 * timeToNextBounce has // surpassed the current restingTime -->
                 * jump! if (physix.getLinearVelocity().y < EPSILON &&
                 * physix.getLinearVelocity().y > -EPSILON &&
                 * jump.timeToNextJump > jump.restingTime) {
                 * 
                 * physix.applyImpulse(0, jump.jumpImpulse); // reset doJump!
                 * jump.doJump = false; } // if entity is on the ground, add
                 * deltaTime to timeToNextBounce else if
                 * (physix.getLinearVelocity().y < EPSILON &&
                 * physix.getLinearVelocity().y > -EPSILON) {
                 * jump.timeToNextJump += deltaTime; }else{ jump.timeToNextJump
                 * = 0; }
                 */
            }

        }
    }

}
