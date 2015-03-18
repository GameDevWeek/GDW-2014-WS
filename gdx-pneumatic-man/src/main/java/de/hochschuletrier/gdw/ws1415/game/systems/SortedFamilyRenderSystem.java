package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import java.util.ArrayList;
import java.util.Comparator;

public class SortedFamilyRenderSystem extends SortedIteratingSystem {

    public static abstract class Renderer {

        private final Family family;

        public Renderer(Family family) {
            this.family = family;
        }

        public abstract void render(Entity entity, float deltaTime);
    }

    private final ArrayList<Renderer> renderers = new ArrayList();

    public SortedFamilyRenderSystem(Family family, Comparator<Entity> comparator) {
        super(family, comparator);
    }

    public SortedFamilyRenderSystem(Family family, Comparator<Entity> comparator, int priority) {
        super(family, comparator, priority);
    }

    public void addRenderer(Renderer renderer) {
        renderers.add(renderer);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        for (Renderer renderer : renderers) {
            if (renderer.family.matches(entity)) {
                renderer.render(entity, deltaTime);
            }
        }
    }
}
