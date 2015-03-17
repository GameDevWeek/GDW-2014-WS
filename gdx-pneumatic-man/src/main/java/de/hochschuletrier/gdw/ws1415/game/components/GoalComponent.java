package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class GoalComponent extends Component implements Pool.Poolable {

    public int mineworkers_saved;
    public int number_of_mineworkers_total;
    public boolean end_of_level;

    @Override
    public void reset() {
        mineworkers_saved = 0;
        number_of_mineworkers_total = 0;
        end_of_level = false;
    }

}
