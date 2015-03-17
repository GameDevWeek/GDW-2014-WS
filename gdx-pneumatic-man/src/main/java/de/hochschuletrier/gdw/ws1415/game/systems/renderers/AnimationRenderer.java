package de.hochschuletrier.gdw.ws1415.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedFamilyRenderSystem;

public class AnimationRenderer extends SortedFamilyRenderSystem.Renderer {

    public AnimationRenderer() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    public void render(Entity entity, float deltaTime) {
        // Todo: render
    }
}
