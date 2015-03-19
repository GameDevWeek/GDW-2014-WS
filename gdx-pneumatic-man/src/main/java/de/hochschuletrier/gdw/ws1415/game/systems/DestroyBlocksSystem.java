package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
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


    public DestroyBlocksSystem() {
        super(Family.all(PlayerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent ani = entity.getComponent(AnimationComponent.class);
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);

        if(ani != null) {
            // if (ani.animationFinished) {
            Vector2 p1 = physix.getBody().getPosition();
            Vector2 p2 = new Vector2(p1).add( Direction.DOWN.toVector2().scl(GameConstants.getTileSizeY()*GameConstants.getTileSizeY()) ); // FIXME MAGIC NUMBER
            p1.x = EntityCreator.physixSystem.toBox2D(p1.x);    p1.y = EntityCreator.physixSystem.toBox2D(p1.y);
            p2.x = EntityCreator.physixSystem.toBox2D(p2.x);    p2.y = EntityCreator.physixSystem.toBox2D(p2.y);

            EntityCreator.physixSystem.getWorld().rayCast(new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                    PhysixBodyComponent bodyComponent = fixture.getUserData() instanceof PhysixBodyComponent ?
                            (PhysixBodyComponent)fixture.getUserData() : null;
                    if(bodyComponent != null){
                        if( ComponentMappers.block.has(bodyComponent.getEntity())
                                && ComponentMappers.health.has(bodyComponent.getEntity())){
                            HealthComponent healthComponent = ComponentMappers.health.get(bodyComponent.getEntity());
                            healthComponent.DecrementByValueNextFrame += 1;
                            return 0;
                        }
                    }
                    return 1;
                }
            }, p1, p2);

        }
    }
}

