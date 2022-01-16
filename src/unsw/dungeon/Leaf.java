package unsw.dungeon;

public abstract class Leaf implements Component {
    @Override
    public abstract boolean complete(Player player);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
