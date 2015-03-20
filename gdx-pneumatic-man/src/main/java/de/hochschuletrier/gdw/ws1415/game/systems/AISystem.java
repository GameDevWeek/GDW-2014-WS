package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
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
        super(Family.all(AIComponent.class).get(), priority);
        this.physixSystem = physixSystem;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        DirectionComponent direction = entity.getComponent(DirectionComponent.class);
        AIComponent ai = ComponentMappers.AI.get(entity);
        if(ai.type == AIType.DOG); //TODO: do some stuff based on AIType

        Vector2 dir = direction.facingDirection.toVector2();
        Vector2 p1, p2;

        p1 = physix.getBody().getPosition();
        p2 = new Vector2(p1).add(dir.scl(1.5f)); // FIXME MAGIC NUMBER
                                //front, ground
        final boolean[] clear = {true, false};
        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
            if(fixture.getUserData() instanceof PhysixBodyComponent)
                if(ComponentMappers.player.has(((PhysixBodyComponent)fixture.getUserData()).getEntity()) )
                    return 0;
            clear[0] = false;
            return 0;
        }, p1, p2);

        p2 = new Vector2(p2).add(Direction.DOWN.toVector2().scl(1.5f)); // FIXME MAGIC NUMBER

        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
            clear[1] = true;
            return 0;
        }, p1, p2);

//        if(Math.random() <= 0.005f){
//            JumpComponent jump = entity.getComponent(JumpComponent.class);
//            physix.applyImpulse(0, jump.jumpImpulse);
//            jump.doJump = false;
//        }

        if(clear[0] && clear[1]){

            if(direction.facingDirection == Direction.LEFT) {
                movement.velocity.set(movement.speed * direction.facingDirection.toVector2().x, movement.velocity.y);
            }else if(direction.facingDirection == Direction.RIGHT) {
                movement.velocity.set(movement.speed * direction.facingDirection.toVector2().x, movement.velocity.y);
            }
        }
        else{
            direction.facingDirection = direction.facingDirection.rotate180();
        }
//        if(checkInFront(physix, dir, 0)){ // TODO: replace 0 with jump-width (movement component)
//            direction.facingDirection = Direction.fromVector2(dir.scl(-1));
//        }else if(checkBottomFront(physix, dir, 0)){ // TODO: replace 0 with jump-width (movement component)
//            // move forward
//
//
//        }else if(checkBottomFront(physix, dir, 0)){ // TODO: replace 0 with jump-width (movement component)
//            // move forward
//
//        }

    }
}
