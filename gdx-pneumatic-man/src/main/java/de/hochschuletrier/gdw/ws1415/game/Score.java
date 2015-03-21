package de.hochschuletrier.gdw.ws1415.game;

import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.ws1415.game.systems.ScoreSystem;

public class Score {
    public static int score = 0;
    public static ScoreSystem scoreSys;
    public static String[] highscores = {"0", "0", "0", "0", "0", "0", "0","0","0", "0"};

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
        compareScores(score);
    }

    public static void readHighscores(){
        for(int i = 0; i <= (highscores.length - 1); i++){
            Settings.HIGHSCORE.set(highscores[i]);
        }
        Settings.flush();
    }

    public static void addHighscore(int n){
        for(int i = n; i <= (highscores.length - 1); i++){
            int newPos = i + 1;
            if(newPos <= (highscores.length - 1)){
                highscores[i] = highscores[newPos];
            }
        }

        highscores[n] = ""+score;
    }

    public static void compareScores(int score){
        int n = -1;

        for(String s : highscores){
            int currentHighscore = Integer.parseInt(s);
            n++;
            if(currentHighscore <= score){
                addHighscore(n);
                break;
            }
        }
    }
}
