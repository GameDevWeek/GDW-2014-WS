package de.hochschuletrier.gdw.ws1415.game.contactlisteners;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.*;

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

        // Player collides with lava.
        if (otherEntity.getComponent(KillsPlayerOnContactComponent.class) != null) {
            // Player dies and level resets.
            HealthComponent Health = player.getComponent(HealthComponent.class);
            Health.DecrementByValueNextFrame = Health.Value;
        }

        if (otherEntity.getComponent(FallingRockTriggerComponent.class) != null){
            FallingRockTriggerComponent rockTriggerComponent = otherEntity.getComponent(FallingRockTriggerComponent.class);
            FallingRockComponent rockComponent = ComponentMappers.rockTraps.get(rockTriggerComponent.rockEntity);
            rockComponent.falling = true;
            EntityCreator.engine.removeEntity(otherEntity);
        }

        if (otherEntity.getComponent(DamageComponent.class) != null) {
            // player touched an enemy
            if (otherEntity.getComponent(DamageComponent.class).damageToPlayer) {
                player.getComponent(HealthComponent.class).DecrementByValueNextFrame = otherEntity.getComponent(DamageComponent.class).damage;
            }
        }
        
        Family VulnerableBlockFamily = Family.all(DestructableBlockComponent.class, HealthComponent.class).get();
        if(VulnerableBlockFamily.matches(otherEntity))
        {
            HealthComponent OtherHealth = otherEntity.getComponent(HealthComponent.class);
            OtherHealth.Value -= 1;
        }
        
    }

        // If the contact was with a tile then nothing happens to the player but
        // the tile's health
        // is reduced by 1.

}
