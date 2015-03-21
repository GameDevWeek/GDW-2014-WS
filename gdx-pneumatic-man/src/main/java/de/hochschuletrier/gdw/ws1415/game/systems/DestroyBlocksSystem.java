package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class DestroyBlocksSystem extends IteratingSystem {

    public final static float RAY_LENGTH_JUMP = 2.4f;
    public final static float RAY_LENGTH_GROUNDED = 1.4f;

    public DestroyBlocksSystem() {
        super(Family.all(PlayerComponent.class).get());
    }

    void sendBlockRaycastBelowAndDamageBlock(Entity raycastSender, int DamageValue, float RayScale, float Facing)
    {
        Vector2 RayDir = new Vector2( Facing * 0.1f, 1 );
        RayDir.nor();
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(raycastSender);
        Vector2 p1 = physix.getBody().getPosition();
        Vector2 p2 = new Vector2(p1).add(RayDir.scl(RayScale)); 

        EntityCreator.physixSystem.getWorld().rayCast((fixture, point, normal, fraction) -> {
            Object bodyUserData = fixture.getBody().getUserData();
            if (bodyUserData instanceof PhysixBodyComponent) 
            {
                PhysixBodyComponent bodyComponent = (PhysixBodyComponent) bodyUserData;
                if (ComponentMappers.block.has(bodyComponent.getEntity())
                        && ComponentMappers.health.has(bodyComponent.getEntity())) 
                {
                    HealthComponent healthComponent = ComponentMappers.health.get(bodyComponent.getEntity());
                    healthComponent.DecrementByValueNextFrame += DamageValue;
                    

                    return 0;
                }
            }
            return 1;
        }, p1, p2);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent ani = entity.getComponent(AnimationComponent.class);
        float Facing = ani.flipX ? 1 : -1;

        JumpComponent Jump = entity.getComponent(JumpComponent.class);
        if(Jump != null)
        {
            if(Jump.groundContacts > 0) // Wenn Grounded, dann 
            {
                if(Jump.justLanded)
                {
                    //System.out.println("Damage on Land");
                    Jump.justLanded = false;
                    sendBlockRaycastBelowAndDamageBlock(entity, 2, RAY_LENGTH_JUMP, Facing);
                    ani.stateTime = 0.0f;
                    ani.animationFinished = false;
                    ani.permanent_stateTime = 0.0f;
                    /*
                    */
                }
                else
                {
                    if(ani != null) {
                        if (ani.animationFinished) {
                            sendBlockRaycastBelowAndDamageBlock(entity, 1, RAY_LENGTH_GROUNDED, Facing);
                            ani.animationFinished = false;
                        }
                    }
                }
            }
            else // In Air
            {
                if(Jump.justJumped) // Gerade erst losgesprungen
                {
                    //System.out.println("Damage on Jump");
                    sendBlockRaycastBelowAndDamageBlock(entity, 2, RAY_LENGTH_JUMP, Facing);
                }
            }
        }
    }
}

