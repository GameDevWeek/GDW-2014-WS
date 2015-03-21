package de.hochschuletrier.gdw.ws1415.game.contactlisteners;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.DamageComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;

/**
 * 
 * @author David Liebemann
 *
 */
public class ExplosionContactListener extends PhysixContactAdapter{
    
    @Override
    public void beginContact(PhysixContact contact) {
        Entity otherEntity = contact.getOtherComponent().getEntity();
        Entity myEntity = contact.getMyComponent().getEntity();
        
        HealthComponent health = ComponentMappers.health.get(otherEntity);
        DamageComponent damage = ComponentMappers.damage.get(myEntity);
        if(health != null && damage != null){
            health.DecrementByValueNextFrame += damage.damage;
        }
    }

}
