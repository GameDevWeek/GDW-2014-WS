package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;

public class UpdatePositionSystem extends IteratingSystem {

    public UpdatePositionSystem() {
        this(0);
    }

    public UpdatePositionSystem(int priority) {
        super(Family.all(PositionComponent.class, PhysixBodyComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        HealthComponent health = entity.getComponent(HealthComponent.class);
        position.x = physix.getX();
        position.y = physix.getY();
        position.rotation = physix.getAngle() * MathUtils.radiansToDegrees;
        
       if(health != null)
       {
           checkHealth(entity, health, position, deltaTime);
       }
    }

    private void checkHealth(Entity entity, HealthComponent health, PositionComponent position, float deltaTime)
    {
        if(health.Value == HealthComponent.HealthState.DYING)
        {
            if(position.y > Gdx.graphics.getHeight()) // + Entity-height, so it doesnt disappear, while still half on screen)
                position.y = position.y + 1;
            else
                entity.getComponent(HealthComponent.class).Value = HealthComponent.HealthState.DEAD;
        }
        return;
    }

}
