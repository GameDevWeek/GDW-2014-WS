package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1415.game.components.GoalComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MinerComponent;

public class ScoreSystem extends EntitySystem implements EntityListener {

    public int score;
    public int total_miners;
    Family miners = Family.all(MinerComponent.class).get();
    Family gc = Family.all(GoalComponent.class).get();
    public int zaehler_ate = 0;
    public int zaehler_et = 0;
    public Entity goal;

    public ScoreSystem() {
        super(1);
        score = 0;
    }

    public void addedToEngine(Entity entity) {
        if (miners.matches(entity)) {
            total_miners += 1;
            zaehler_ate += 1;
        }

        if (gc.matches(entity)) {
            goal = entity;
        }
    }

    public void removedfromEngine(Entity entity) {
        if (miners.matches(entity)) {
            goal.getComponent(GoalComponent.class).miners_saved += 1;
        }
    }

    public void update() {

    }

    @Override
    public void entityAdded(Entity entity) {
        if (miners.matches(entity)) {
            total_miners += 1;
            zaehler_et += 1;
        }

        if (gc.matches(entity)) {
            goal = entity;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        // TODO Auto-generated method stub

    }
}
