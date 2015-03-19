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
        if(! c.falling) return;


        Vector2 d = contact.getMyFixture().getBody().getPosition()
                .sub(contact.getOtherFixture().getBody().getPosition()); // vector von other nach rock

        // kommt rock von oben?
        if(d.dot(Vector2.Y) >= 0) return; // nein tut er nicht


        // ja stein kommt von oben:
        if(ComponentMappers.health.has(otherEntity)) {
            HealthComponent h = ComponentMappers.health.get(contact.getOtherComponent().getEntity());
            h.DecrementByValueNextFrame -= c.damage;
            EntityCreator.engine.removeEntity(myEntity);
        }else if(ComponentMappers.block.has(otherEntity)){
            EntityCreator.engine.removeEntity(myEntity);
        }
    }

}
