package de.hochschuletrier.gdw.ws1415.game.contactlisteners;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.DamageComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;

/**
 * Handles contacts between player and other entities
 */
public class PlayerContactListener extends PhysixContactAdapter {

    public void beginContact(PhysixContact contact) {

        Entity player = contact.getMyComponent().getEntity();

        if (contact.getOtherComponent() == null)
            return;

        // Entity myEntity = contact.getMyComponent().getEntity(); //
        Entity otherEntity = contact.getOtherComponent().getEntity();
        if (ComponentMappers.enemy.has(otherEntity)) {
            // player touched an enemy

            if (otherEntity.getComponent(DamageComponent.class).damageToPlayer) {
                // Damage system has to reduce the players health by the amount
                // specified in the damage component here.
            }
            player.getComponent(HealthComponent.class);
        }
    }

}
