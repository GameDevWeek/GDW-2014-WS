package de.hochschuletrier.gdw.ws1415.game.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.ws1415.game.components.GoalComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MinerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;

public class ScoreSystem extends EntitySystem implements EntityListener {

    private static final Logger logger = LoggerFactory.getLogger(ScoreSystem.class);
    
    public int score;
    public int total_miners;
    Family MinerFamily = Family.all(MinerComponent.class).get();
    Family GoalFamily = Family.all(GoalComponent.class).get();
    Family PlayerFamily = Family.all(PlayerComponent.class).get();
    public Entity goal;
    public Entity player;
    public boolean playerAdded = false;
    float n = 0;
    public int current_game_time = 0;

    public ScoreSystem() {
        super(1);
        score = 0;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family Fam = Family.one(PlayerComponent.class, MinerComponent.class, GoalComponent.class).get();
        engine.addEntityListener(Fam, this);
    }

    @Override
    public void update(float deltaTime) {
        n += deltaTime;
        if(n>=1.0f){
            current_game_time += 1;
            n-=1.0f;
            logger.info("Time: " + current_game_time);
        }
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
            logger.info("Miners saved: " + player.getComponent(PlayerComponent.class).saved_miners);
        }
    }
    
    public void calculateHighscore(){
        int highscore = 1000;
        int bonus_miners = player.getComponent(PlayerComponent.class).saved_miners - goal.getComponent(GoalComponent.class).miners_threshold;
        
        highscore -= current_game_time;
        highscore += (goal.getComponent(GoalComponent.class).miners_threshold * 10);
        highscore += (bonus_miners * 20);
        
        score = highscore;
    }
}
