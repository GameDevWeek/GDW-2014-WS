package de.hochschuletrier.gdw.ws1415.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;

/**
 * Handles contacts between falling Rocks and Blocks / Enemies / maybe even player
 */
public class RockContactListener extends PhysixContactAdapter {

    public void beginContact(PhysixContact contact) {
        Entity myEntity = contact.getMyComponent().getEntity();
        Entity otherEntity = contact.getOtherComponent().getEntity();
        FallingRockComponent c = ComponentMappers.rockTraps.get(myEntity);


        Vector2 d = contact.getMyFixture().getBody().getPosition()
                .sub(contact.getOtherFixture().getBody().getPosition()); // vector von other nach rock

        // kommt rock von oben?
        // ja stein kommt von oben:
        if(contact.getWorldManifold().getNormal().y < 0)
        {
            if(ComponentMappers.health.has(otherEntity)) {
                HealthComponent h = ComponentMappers.health.get(otherEntity);
                h.DecrementByValueNextFrame = 1;
                /*
                HealthComponent MyHealth = myEntity.getComponent(HealthComponent.class);
                MyHealth.Value = 0;
                */
                //h.Value = 0;
                //h.Value -= myEntity.getComponent(DamageComponent.class).damage;
            }else if(ComponentMappers.block.has(otherEntity)){
            }
            
        }
    }

}
