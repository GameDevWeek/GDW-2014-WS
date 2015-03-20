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
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;

public class PlatformSystem extends IteratingSystem {

    public PlatformSystem(PhysixSystem physixSystem) {
        this(0, physixSystem);
    }

    private PhysixSystem physixSystem;

    public PlatformSystem(int priority, PhysixSystem physixSystem) {
        super(Family.all(PhysixBodyComponent.class, DirectionComponent.class, PlatformComponent.class).get(), priority);
        this.physixSystem = physixSystem;
    }

    

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        DirectionComponent direction = entity.getComponent(DirectionComponent.class);
        PlatformComponent platform = entity.getComponent(PlatformComponent.class);

        platform.traveledDistanceVector.set(physix.getPosition().x, physix.getPosition().y);
        platform.traveledDistanceVector.sub(platform.startPos).len2();
        
        if(platform.traveledDistanceVector.len2() <= platform.travelDistance*platform.travelDistance){
            
        }else{
            platform.startPos.add(platform.traveledDistanceVector);
            direction.facingDirection = direction.facingDirection.rotate180();
        }
        
        platform.velocity.set(direction.facingDirection.toVector2().x, direction.facingDirection.toVector2().y);
        platform.velocity.scl(platform.platformSpeed * 64);
        
        physix.setLinearVelocity(platform.velocity);
    }
}
