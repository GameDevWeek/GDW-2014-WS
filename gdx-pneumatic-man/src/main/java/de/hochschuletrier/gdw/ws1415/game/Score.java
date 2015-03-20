package de.hochschuletrier.gdw.ws1415.game;

import de.hochschuletrier.gdw.ws1415.game.systems.ScoreSystem;

public class Score {
    public static int score = 0;
    public static ScoreSystem scoreSys;

    public static void calculate_score(int time, int saved_miners, int destroyed_blocks, int miners_threshold){
        int highscore = 1000;
        int bonus_miners = saved_miners - miners_threshold;
        
        highscore -= time;
        highscore += destroyed_blocks;
        highscore += (miners_threshold * 10);
        if(bonus_miners >= 1){
            highscore += (bonus_miners * 20);
        }
        
        score = highscore;
    }
}
