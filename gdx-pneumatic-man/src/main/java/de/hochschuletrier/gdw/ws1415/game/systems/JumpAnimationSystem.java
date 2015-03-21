package de.hochschuletrier.gdw.ws1415.game.systems;

import java.time.Clock;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Timer;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.JumpComponent;
import de.hochschuletrier.gdw.ws1415.game.components.JumpableAnimationComponent;

public class JumpAnimationSystem extends IteratingSystem {
    
    long startTimeForJump;
   
    public JumpAnimationSystem(AssetManagerX assetManager, int priority) {
        super(Family.all(JumpableAnimationComponent.class, JumpComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        JumpableAnimationComponent jumpable = entity.getComponent(JumpableAnimationComponent.class);
        JumpComponent jump = entity.getComponent(JumpComponent.class);

        // Charakter has been in midair, check if on ground again
        if(jumpable.midair)
        {
            if(jump.groundContacts > 0)
            {
                // Charakter now on the ground, switch to idle-animation
                jumpable.midair = false;
                AnimationComponent animation = ComponentMappers.animation.get(entity);
                animation.reset();
                animation.animation = jumpable.idle;
                
//                System.out.println("Time for Jump: " + (System.currentTimeMillis() - startTimeForJump));
            }
        }
        // Charakter has been on the ground - check if falling
        else if (jump.groundContacts <= 0)
        {
            // Charakter in midair -> switch to jump-animation
            jumpable.midair = true;
            AnimationComponent animation = ComponentMappers.animation.get(entity);
            animation.reset();
            animation.animation = jumpable.jump;

            startTimeForJump = System.currentTimeMillis();
        }
       
    }
}