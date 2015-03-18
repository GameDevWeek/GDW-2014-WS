package de.hochschuletrier.gdw.ws1415.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.*;

/**
 * Handles contacts between falling Rocks and Blocks / Enemies / maybe even player
 */
public class RockContactListener extends PhysixContactAdapter {

    public void beginContact(PhysixContact contact) {
        Entity myEntity = contact.getMyComponent().getEntity();
        Entity otherEntity = contact.getOtherComponent().getEntity();
        FallingRockComponent c = ComponentMappers.rockTraps.get(myEntity);
        if(! c.falling) return;


        if(ComponentMappers.health.has(otherEntity)) {
            HealthComponent h = ComponentMappers.health.get(contact.getOtherComponent().getEntity());
            h.DecrementByValueNextFrame -= c.damage;
            EntityCreator.engine.removeEntity(myEntity);
        }else if(ComponentMappers.block.has(otherEntity)){
            EntityCreator.engine.removeEntity(myEntity);
        }
    }

}
