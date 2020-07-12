package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public abstract class Composite implements Component {
    List<Component> components = new ArrayList<>();

    void add (Component c){
        components.add(c);
    }
}