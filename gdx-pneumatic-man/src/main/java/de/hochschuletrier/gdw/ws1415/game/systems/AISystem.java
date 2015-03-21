package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;

public class AISystem extends IteratingSystem {

    public AISystem(PhysixSystem physixSystem) {
        this(0, physixSystem);
    }

    private PhysixSystem physixSystem;

    public AISystem(int priority, PhysixSystem physixSystem) {
        super(Family.all(AIComponent.class,
                PhysixBodyComponent.class,
                MovementComponent.class,
                JumpComponent.class,
                DirectionComponent.class).get(), priority);
        this.physixSystem = physixSystem;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        /*PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
         MovementComponent movement = ComponentMappers.movement.get(entity);
         DirectionComponent direction = entity.getComponent(DirectionComponent.class);
         AIComponent ai = ComponentMappers.AI.get(entity);
         if (ai.type == AIType.DOG) {
         DogBehavior(physix, movement, direction);
         } else {
         if (ai.AItimer <= 0f) {
         ChameleonBehavior(physix, movement, direction, ai);
         ai.AItimer = 0.2f + (float) Math.random() / 20f;
         } else {
         ai.AItimer -= deltaTime;
         }
         //DogBehavior(physix, movement, direction);
         }*/

        MovementComponent movementComponent = ComponentMappers.movement.get(entity);
        PhysixBodyComponent physicBodyComponent = ComponentMappers.physixBody.get(entity);
        AIComponent aiComponent = ComponentMappers.AI.get(entity);
        DirectionComponent directionComponent = ComponentMappers.direction.get(entity);
        JumpComponent jumpComponent = ComponentMappers.jump.get(entity);

        // search if left/right is blocked with RayCasts
        aiComponent.leftBlocked = false;
        aiComponent.rightBlocked = false;
        Vector2 p1, p2, p3;
        p1 = physicBodyComponent.getBody().getPosition();
        p2 = new Vector2(p1).add(Direction.LEFT.toVector2().scl(1.4f)); // FIXME MAGIC NUMBER
        p3 = new Vector2(p1).add(Direction.RIGHT.toVector2().scl(1.4f)); // FIXME MAGIC NUMBER

        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
            PhysixBodyComponent otherPhysicsBody = (PhysixBodyComponent) fixture.getBody().getUserData();
            if (otherPhysicsBody.getEntity().getComponent(PlayerComponent.class) == null) {
                aiComponent.leftBlocked = true;

            }
            return 0;
        }, p1, p2);
        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
            PhysixBodyComponent otherPhysicsBody = (PhysixBodyComponent) fixture.getBody().getUserData();
            if (otherPhysicsBody.getEntity().getComponent(PlayerComponent.class) == null) {
                aiComponent.rightBlocked = true;
            }
            return 0;
        }, p1, p3);

        // search ifleft/right is a block to walk on
        aiComponent.leftGroundPresent = false;
        aiComponent.rightGroundPresent = false;
        p1 = physicBodyComponent.getBody().getPosition();
        p2 = new Vector2(p1).add(Direction.LEFT.toVector2().scl(2f))
                .add(Direction.DOWN.toVector2().scl(3f)); // FIXME MAGIC NUMBER
        p3 = new Vector2(p1).add(Direction.RIGHT.toVector2().scl(2f)
                .add(Direction.DOWN.toVector2().scl(3.0f))); // FIXME MAGIC NUMBER

        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
            PhysixBodyComponent otherPhysicsBody = (PhysixBodyComponent) fixture.getBody().getUserData();
            if (otherPhysicsBody.getEntity().getComponent(DestructableBlockComponent.class) != null
                    || otherPhysicsBody.getEntity().getComponent(IndestructableBlockComponent.class) != null) {
                aiComponent.leftGroundPresent = true;

            }
            return 0;
        }, p1, p2);
        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
            PhysixBodyComponent otherPhysicsBody = (PhysixBodyComponent) fixture.getBody().getUserData();
            if (otherPhysicsBody.getEntity().getComponent(DestructableBlockComponent.class) != null
                    || otherPhysicsBody.getEntity().getComponent(IndestructableBlockComponent.class) != null) {
                aiComponent.rightGroundPresent = true;
            }
            return 0;
        }, p1, p3);

        // dog
        if (aiComponent.type == AIType.DOG) {
            if (directionComponent.facingDirection.equals(Direction.RIGHT)
                    && (aiComponent.rightBlocked
                    || !aiComponent.rightGroundPresent)) {
                directionComponent.facingDirection = directionComponent.facingDirection.rotate180();
            } else if (directionComponent.facingDirection.equals(Direction.LEFT)
                    && (aiComponent.leftBlocked
                    || !aiComponent.leftGroundPresent)) {
                directionComponent.facingDirection = directionComponent.facingDirection.rotate180();
            }

            movementComponent.velocity.set(movementComponent.speed * directionComponent.facingDirection.toVector2().x, movementComponent.velocity.y);

        }

        //chameleon
        if (aiComponent.type == AIType.CHAMELEON) {
            aiComponent.AItimer -= deltaTime;
            if (aiComponent.AItimer <= 0) {
                movementComponent.setVelocity(Vector2.Zero);
                physicBodyComponent.applyImpulse(0, jumpComponent.jumpSpeed);
                aiComponent.AItimer = 5.0f + (float) Math.random() / 20f;
            } else {

                if (directionComponent.facingDirection.equals(Direction.RIGHT)
                        && (aiComponent.rightBlocked
                        || !aiComponent.rightGroundPresent)) {
                    directionComponent.facingDirection = directionComponent.facingDirection.rotate180();
                } else if (directionComponent.facingDirection.equals(Direction.LEFT)
                        && (aiComponent.leftBlocked
                        || !aiComponent.leftGroundPresent)) {
                    directionComponent.facingDirection = directionComponent.facingDirection.rotate180();
                }

                movementComponent.velocity.set(movementComponent.speed * directionComponent.facingDirection.toVector2().x, movementComponent.velocity.y);
            }
        }
    }

    private void ChameleonBehavior(PhysixBodyComponent physix, MovementComponent movement, DirectionComponent direction, AIComponent ai) {
        Vector2 dir = direction.facingDirection.toVector2();
        Vector2 p1, p2;
        final boolean[] clear = {false, false};
        switch (ai.AIstate) {
            case (0):    // chameleon stands on ground → can jump?
                p1 = physix.getBody().getPosition();
                p2 = new Vector2(p1).add(dir.scl(2f)).add(Direction.UP.toVector2().scl(2f)); // FIXME MAGIC NUMBER

                clear[0] = true; //front, ground → used in lambda → needs to be final
                EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
                    Object bodyUserData = fixture.getBody().getUserData();
                    if (bodyUserData instanceof PhysixBodyComponent) {
                        if (ComponentMappers.player.has(((PhysixBodyComponent) bodyUserData).getEntity())) {
                            return 1;
                        }
                    }
                    clear[0] = false;
                    return 0;
                }, p1, p2);
                if (clear[0]) {
                    clear[0] = false;
                    p1 = p2;
                    p2 = new Vector2(p1).add(dir.scl(2f)).add(Direction.DOWN.toVector2().scl(3f)); // FIXME MAGIC NUMBER
                    EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
                        Object bodyUserData = fixture.getBody().getUserData();
                        if (bodyUserData instanceof PhysixBodyComponent) {
                            if (ComponentMappers.player.has(((PhysixBodyComponent) bodyUserData).getEntity())) {
                                return 1;
                            }
                        }
                        if (fraction >= 0.9f) {
                            if (bodyUserData instanceof PhysixBodyComponent && (ComponentMappers.iblock.has(((PhysixBodyComponent) bodyUserData).getEntity()) || //indestructabl
                                    ComponentMappers.block.has(((PhysixBodyComponent) bodyUserData).getEntity()))) //destructable
                            {
                                clear[0] = true;
                            }
                            return 0;
                        }
                        return 0;
                    }, p1, p2);
                }
                if (clear[0]) {
                    JumpComponent jump = ComponentMappers.jump.get(physix.getEntity());
                    physix.applyImpulse(0, jump.jumpSpeed);
                    ai.AIstate = 1;
                } else {
                    ai.AIstate = 3;
                }
                break;
            case (2): // in air → landing
                movement.velocity.set(movement.speed * direction.facingDirection.toVector2().x, movement.velocity.y);
            case (1): // in air → jump start
                p1 = physix.getBody().getPosition();
                p2 = new Vector2(p1).add(Direction.DOWN.toVector2().scl(2f)); // FIXME MAGIC NUMBER
                clear[0] = ai.AIstate == 1; // first time true, second time false
                // first time, check for air under AI (clear true)
                // second time, check for block under AI (clear false)
                EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
                    Object bodyUserData = fixture.getBody().getUserData();
                    if (bodyUserData instanceof PhysixBodyComponent) {
                        if (ComponentMappers.player.has(((PhysixBodyComponent) bodyUserData).getEntity())) {
                            return 0;
                        }
                    }
                    clear[0] = !clear[0];
                    // 1st time: from true→false if theres a block
                    // 2nd time: stays false till theres a block
                    return 0;
                }, p1, p2);
                if (clear[0]) {
                    ai.AIstate++;
                }
                break;
            case (3):
                direction.facingDirection = direction.facingDirection.rotate180();
                ai.AIstate = 0;
                break;
        }
    }

    private void DogBehavior(PhysixBodyComponent physix, MovementComponent movement, DirectionComponent direction) {
        Vector2 dir = direction.facingDirection.toVector2();
        Vector2 p1, p2;
        p1 = physix.getBody().getPosition();
        p2 = new Vector2(p1).add(dir.scl(1.5f)); // FIXME MAGIC NUMBER

        final boolean[] clear = {true, false}; //front, ground → used in lambda → needs to be final
        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
            Object bodyUserData = fixture.getBody().getUserData();
            if (bodyUserData instanceof PhysixBodyComponent) {
                if (ComponentMappers.player.has(((PhysixBodyComponent) bodyUserData).getEntity())) {
                    return 0;
                }
            }
            clear[0] = false;
            return 0;
        }, p1, p2);

        p2 = new Vector2(p2).add(Direction.DOWN.toVector2().scl(1.5f)); // FIXME MAGIC NUMBER

        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
            clear[1] = true;
            return 0;
        }, p1, p2);

        if (clear[0] && clear[1]) {
            movement.velocity.set(movement.speed * direction.facingDirection.toVector2().x, movement.velocity.y);
        } else {
            direction.facingDirection = direction.facingDirection.rotate180();
        }
    }
}
