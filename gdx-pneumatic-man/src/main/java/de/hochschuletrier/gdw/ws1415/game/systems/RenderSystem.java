package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DestructableBlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.renderers.AnimationRenderer;

/**
 * Renders all renderable components by using the Renderers.
 * 
 *
 */
@Deprecated
public class RenderSystem extends EntitySystem implements EntityListener {

    private static final EntityComparator comparator = new EntityComparator();
    private final ArrayList<Entity> entities = new ArrayList<>();
    private boolean resort;

    private AnimationRenderer animationRenderSystem = new AnimationRenderer();
    //private AnimationRenderSubsystem animationRenderSystem = new AnimationRenderSubsystem();
    private DestructableBlockRenderSubsystem destructableBlockRenderSystem = new DestructableBlockRenderSubsystem();
    private TextureRenderSubsystem textureSystem = new TextureRenderSubsystem();
    private CameraSystem cameraSystem = new CameraSystem();

    public RenderSystem() {
        super(0);
    }

    public RenderSystem(int priority) {
        super(priority);
    }
    
    public LimitedSmoothCamera getCamera() {
    	return cameraSystem.getCamera();
    }

    @Override
    public void addedToEngine(Engine engine) {
        // one() bekommt später noch eine geplante TextureComponent
        @SuppressWarnings("unchecked")
        Family family = Family
                .all(PositionComponent.class, LayerComponent.class)
                .one(AnimationComponent.class, TextureComponent.class).get();
        engine.addEntityListener(family, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        entities.add(entity);
        resort = true;
    }

    @Override
    public void entityRemoved(Entity entity) {
        entities.remove(entity);
    }

    @Override
    public void update(float deltaTime) {
        if (resort) {
            entities.sort(comparator);
            resort = false;
        }

        // Go through all entities and use subsystems to render them depending
        // on their
        // components. Possible alternative: create RenderComponent with a
        // RenderType enum for a
        // simple switch.
        for (Entity entity : entities) {
            AnimationComponent animation = ComponentMappers.animation.get(entity);
            DestructableBlockComponent block = ComponentMappers.block.get(entity);
            HealthComponent health = ComponentMappers.health.get(entity);
            TextureComponent tex = ComponentMappers.texture.get(entity);
            
//            cameraSystem.preParallax(entity);

            if (animation != null) {
                if (block != null && health != null) {
                    destructableBlockRenderSystem.render(entity, deltaTime);
                } else {
                    animationRenderSystem.render(entity, deltaTime);
                }
            } else if (tex != null) {
            	textureSystem.render(entity, deltaTime);
            }

//            cameraSystem.postParallax();
        }
    }

    private static class EntityComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity a, Entity b) {
            LayerComponent ac = ComponentMappers.layer.get(a);
            LayerComponent bc = ComponentMappers.layer.get(b);
            return ac.layer > bc.layer ? 1 : (ac.layer == bc.layer) ? 0 : -1;
        }
    }
}
