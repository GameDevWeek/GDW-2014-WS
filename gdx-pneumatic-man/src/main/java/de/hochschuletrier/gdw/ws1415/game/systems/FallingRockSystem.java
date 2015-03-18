package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.FallingRockComponent;

public class FallingRockSystem extends IteratingSystem{


    public FallingRockSystem() {
        super(Family.all(FallingRockComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FallingRockComponent rockComponent = ComponentMappers.rockTraps.get(entity);
        if(! rockComponent.falling) return;

        PhysixBodyComponent bodyComponent = ComponentMappers.physixBody.get(entity);
        bodyComponent.setLinearVelocityY(bodyComponent.getLinearVelocity().y + GameConstants.GRAVITY_CONSTANT * bodyComponent.getMass());
    }
}
