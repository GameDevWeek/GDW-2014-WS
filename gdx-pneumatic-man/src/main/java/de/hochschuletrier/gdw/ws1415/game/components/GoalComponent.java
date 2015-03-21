package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class GoalComponent extends Component implements Pool.Poolable {

    public int miners_threshold;
    public boolean end_of_level;

    @Override
    public void reset() {
        miners_threshold = 0;
        end_of_level = false;
    }

}
