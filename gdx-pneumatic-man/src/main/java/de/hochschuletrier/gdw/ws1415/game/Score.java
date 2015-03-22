package de.hochschuletrier.gdw.ws1415.game;

import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.ScoreSystem;

public class Score {
    public static int score = 0;
    public static ScoreSystem scoreSys;
    public static String[] highscores = {"0", "0", "0", "0", "0", "0", "0","0","0", "0"};
    public static int game_time;
    public static int player_destroyed_blocks;
    public static int player_saved_miners;
    public static int total_miners;

    public static void calculate_score(int time, int saved_miners, int destroyed_blocks, int miners_threshold){
        int highscore = 1000;
        int bonus_miners = saved_miners - miners_threshold;
        highscore -= time;
        highscore += destroyed_blocks;
        highscore += (miners_threshold * 10);

        if(bonus_miners >= 1){
            highscore += (bonus_miners * 20);
        }

        game_time = scoreSys.player.getComponent(PlayerComponent.class).game_time;
        player_destroyed_blocks = scoreSys.player.getComponent(PlayerComponent.class).destroyed_blocks;
        player_saved_miners = scoreSys.player.getComponent(PlayerComponent.class).saved_miners;
        total_miners = scoreSys.total_miners;
        score = highscore;
        compareScores(score);
    }

    //public static void readHighscores(){
    //    for(int i = 0; i <= (highscores.length - 1); i++){
    //        Settings.HIGHSCORE.set(highscores[i]);
    //    }
    //    Settings.flush();
    //}

    public static void addHighscore(int NewHighScoreIdx, int newHighscore){

        int LowestHighscoreIdx = highscores.length-1;
        for(int i=LowestHighscoreIdx; i>NewHighScoreIdx; i--)
        {
            highscores[i] = highscores[i-1];
        }

        highscores[NewHighScoreIdx] = ""+newHighscore;
    }

    public static void compareScores(int score){
        int n = 0;

        for(String s : highscores){
            int currentHighscore = Integer.parseInt(s);
            if(currentHighscore < score){
                addHighscore(n, score);
                break;
            }
            n++;
        }
    }
}
