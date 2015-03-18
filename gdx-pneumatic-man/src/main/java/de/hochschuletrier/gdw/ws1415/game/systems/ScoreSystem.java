package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1415.game.components.GoalComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MinerComponent;

public class ScoreSystem extends EntitySystem implements EntityListener {

    public int score;
    public int total_miners;
    Family MinerFamily = Family.all(MinerComponent.class).get();
    Family GoalFamily = Family.all(GoalComponent.class).get();
    public Entity goal;

    public ScoreSystem() {
        super(1);
        score = 0;
    }

    @Override
    public void addedToEngine(Engine engine) {
    }

    public void removedfromEngine(Entity entity) {
        if (MinerFamily.matches(entity)) {
            goal.getComponent(GoalComponent.class).miners_saved += 1;
        }
    }

    public void update() {

    }

    @Override
    public void entityAdded(Entity entity) {
        if(GoalFamily.matches(entity))
        {
            goal = entity;
        }
        if (MinerFamily.matches(entity)) {
            total_miners += 1;
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        // TODO Auto-generated method stub

    }
}
