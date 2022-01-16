package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

public abstract class Composite implements Component {

    List<Component> components = new ArrayList<>();

    public void add(Component child) {
        components.add(child);
    }

    @Override
    public abstract boolean complete(Player player);

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [components=" + components + "]";
    }
}
