package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1415.game.components.GoalComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MinerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;

public class ScoreSystem extends EntitySystem implements EntityListener {

    public int score;
    public int total_miners;
    Family MinerFamily = Family.all(MinerComponent.class).get();
    Family GoalFamily = Family.all(GoalComponent.class).get();
    Family PlayerFamily = Family.all(PlayerComponent.class).get();
    public Entity goal;
    public Entity player;
    public boolean playerAdded = false;

    public ScoreSystem() {
        super(1);
        score = 0;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family Fam = Family.one(PlayerComponent.class, MinerComponent.class, GoalComponent.class).get();
        engine.addEntityListener(Fam, this);
    }

    public void update() {

    }

    @Override
    public void entityAdded(Entity entity) {
        if(PlayerFamily.matches(entity)){
            player = entity;
            playerAdded = true;
        }
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
        if (MinerFamily.matches(entity)) {
            player.getComponent(PlayerComponent.class).saved_miners += 1;
        }
    }
    
    public void calculateHighscore(){
        //TODO
    }
}
