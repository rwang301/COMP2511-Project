package unsw.dungeon;

public abstract class Character extends Entity implements Observer {
    Strategy strategy;

    public Character(int x, int y) {
        super(x, y);
    }

    abstract void collide(Player player);
    abstract void initialise(Player player);
}