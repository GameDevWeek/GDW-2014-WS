package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.Score;
import de.hochschuletrier.gdw.ws1415.game.components.DestructableBlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.GoalComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MinerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;

public class ScoreSystem extends EntitySystem implements EntityListener {

    private static final Logger logger = LoggerFactory.getLogger(ScoreSystem.class);
    
    public int total_miners;
    Family MinerFamily = Family.all(MinerComponent.class).get();
    Family GoalFamily = Family.all(GoalComponent.class).get();
    Family PlayerFamily = Family.all(PlayerComponent.class).get();
    Family BlockFamily = Family.all(DestructableBlockComponent.class).get();
    public Entity goal;
    public Entity player;
    public boolean playerAdded = false;
    float tick = 0;
    public int timeSinceLastCalculation;
    public boolean scoreCanBeRegistered = true;

    public ScoreSystem() {
        super(1);
        Score.scoreSys = this;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family Fam = Family.one(DestructableBlockComponent.class, PlayerComponent.class, MinerComponent.class, GoalComponent.class).get();
        engine.addEntityListener(Fam, this);
    }

    @Override
    public void update(float deltaTime) {
        //if(current_game_time < 5){
        tick += deltaTime;
        if(tick>=1.0f){
            if(player != null)
            {
                if(player.getComponent(PlayerComponent.class) != null)
                {
                    if(player.getComponent(PlayerComponent.class).isSpawned){
                        player.getComponent(PlayerComponent.class).game_time += 1;
                    }
                }
            }
            timeSinceLastCalculation += 1;
            if(timeSinceLastCalculation >= 2){
                scoreCanBeRegistered = true;
            }
            tick-=1.0f;
            if(goal != null && player != null)
            {
                if(goal.getComponent(GoalComponent.class) != null && player.getComponent(PlayerComponent.class) != null)
                {
                    if(goal.getComponent(GoalComponent.class).miners_threshold <= player.getComponent(PlayerComponent.class).saved_miners){
                        goal.getComponent(GoalComponent.class).end_of_level = true;
                        //int saved_miners = player.getComponent(PlayerComponent.class).saved_miners;
                        //int destroyed_blocks = player.getComponent(PlayerComponent.class).destroyed_blocks;
                        //int miners_threshold = goal.getComponent(GoalComponent.class).miners_threshold;
                        //Score.calculate_score(current_game_time, saved_miners, destroyed_blocks, miners_threshold);
                        //logger.info("Your score is: " + Score.score);
                    }
                }
            }
            //logger.info("Time: " + current_game_time);
        }
        /**
        }
        
        if(goal.getComponent(GoalComponent.class).miners_threshold == player.getComponent(PlayerComponent.class).saved_miners)
            goal.getComponent(GoalComponent.class).end_of_level = true;
        
        if(current_game_time == 5){
            if(goal.getComponent(GoalComponent.class).end_of_level){
                this.calculateHighscore();
                logger.info("Your highscore is: " + score);
            }
        }
        **/
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
            //logger.info("Goal was added.");
        }
        if (MinerFamily.matches(entity)) {
            total_miners += 1;
            logger.info("Miners in map: "+total_miners);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (MinerFamily.matches(entity)) {
            if(player != null){
                if(player.getComponent(PlayerComponent.class) != null){
                    player.getComponent(PlayerComponent.class).saved_miners += 1;
                        //play Sound
                        Random rm=new Random();
                        int i=rm.nextInt(3)+1;//1-3
                        System.out.println("saveSaouESCAPEnd "+i);
                        SoundEmitter.playGlobal(EntityCreator.assetManager.getSound("free"+i), false);
                    logger.info("Miners saved: " + player.getComponent(PlayerComponent.class).saved_miners);
                }
            }
        }
        if(BlockFamily.matches(entity)){
            if (player != null){
                if(player.getComponent(PlayerComponent.class) != null){
                    player.getComponent(PlayerComponent.class).destroyed_blocks += 1;
                }
            }
            //logger.info("Destroyed blocks: " + destroyed_blocks);
            //logger.info("A block was destroyed.");
        }
    }
    
    /**
    public void calculateHighscore(){
        int highscore = 1000;
        int bonus_miners = player.getComponent(PlayerComponent.class).saved_miners - goal.getComponent(GoalComponent.class).miners_threshold;
        
        highscore -= current_game_time;
        highscore += destroyed_blocks;
        highscore += (goal.getComponent(GoalComponent.class).miners_threshold * 10);
        if(bonus_miners >= 1){
            highscore += (bonus_miners * 20);
        }
        
        score = highscore;
    }
    **/
}
