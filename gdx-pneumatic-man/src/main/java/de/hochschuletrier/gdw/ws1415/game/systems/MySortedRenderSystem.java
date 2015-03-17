package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import java.util.Comparator;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.AnimationRenderer;

public class MySortedRenderSystem extends SortedFamilyRenderSystem {

    public MySortedRenderSystem() {
        super(Family.all(PositionComponent.class).get(), new Comparator<Entity>() {

            @Override
            public int compare(Entity e1, Entity e2) {
                return (int) e1.getId() - (int) e2.getId(); // fixme: sort by zorder?
            }
        });

        // Order of adding = order of rendering
        // addRenderer(new ParticleRenderer());
        addRenderer(new AnimationRenderer());
    }
}
